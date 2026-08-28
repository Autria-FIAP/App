package com.fiap.autria.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.autria.data.models.NavigationResponse
import com.fiap.autria.data.network.NavigationWebSocketService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gerenciar estado de navegação em tempo real
 */
class NavigationViewModel(
    private val context: Context,
    private val backendUrl: String = "ws://10.0.2.2:8000"
) : ViewModel() {

    companion object {
        private const val TAG = "NavigationViewModel"
    }


    // Estado de navegação atual
    private val _navigationState = MutableStateFlow<NavigationResponse?>(null)
    val navigationState = _navigationState.asStateFlow()

    // Status da conexão
    private val _connectionStatus = MutableStateFlow("Desconectado")
    val connectionStatus = _connectionStatus.asStateFlow()

    private val webSocketService = NavigationWebSocketService(
        context = context,
        backendUrl = backendUrl,
        onConnectionChanged = { connected ->
            _connectionStatus.value = if (connected) {
                "Conectado"
            } else {
                "Desconectado"
            }
        }
    )

    // Mensagem de erro
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Indica se está navegando
    private val _isNavigating = MutableStateFlow(false)
    val isNavigating = _isNavigating.asStateFlow()

    // Contador de frames
    private var frameCounter = 0
    private var navigationJob: kotlinx.coroutines.Job? = null

    /**
     * Inicia navegação em tempo real
     */
    fun startNavigation() {
        if (_isNavigating.value) {
            Log.w(TAG, "⚠️ Navegação já está ativa")
            return
        }

        Log.d(TAG, "🚀 Iniciando navegação...")
        _isNavigating.value = true
        _connectionStatus.value = "Conectando..."
        frameCounter = 0

        navigationJob = viewModelScope.launch {
            try {
                webSocketService.connect().collect { response ->
                    _navigationState.value = response
                    _connectionStatus.value = "Conectado"
                    _errorMessage.value = null

                    Log.d(TAG, "📍 Frame ${response.frameId}: ação=${response.action}, urgente=${response.urgent}")

                    // Enviar próximo frame
                     sendFrame()
                }
            } catch (e: CancellationException) {
                Log.d(TAG, "⏹️ Navegação cancelada")
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro durante navegação: ${e.message}")
                _errorMessage.value = "Erro: ${e.message}"
                _connectionStatus.value = "Erro"
                _isNavigating.value = false
            }
        }
    }

    /**
     * Para navegação em tempo real
     */
    fun stopNavigation() {
        Log.d(TAG, "⏹️ Parando navegação...")
        _isNavigating.value = false
        navigationJob?.cancel()
        webSocketService.disconnect()
        _connectionStatus.value = "Desconectado"
    }

    /**
     * Envia novo frame para processamento
     */
    fun sendFrame() {
        if (!_isNavigating.value) {
            Log.w(TAG, "⚠️ Navegação não está ativa")
            return
        }

        frameCounter++
        Log.d(TAG, "📤 Enviando frame $frameCounter...")
        webSocketService.sendFrameRequest(frameCounter)
    }

    /**
     * Limpar estado e recursos
     */
    fun clearError() {
        _errorMessage.value = null
    }

    /**
     * Obter informações do estado atual
     */
    fun getCurrentState(): NavigationResponse? = _navigationState.value

    /**
     * Verificar se conectado
     */
    fun isConnected(): Boolean = webSocketService.isConnected()

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "🗑️ ViewModel cleared")
        stopNavigation()
    }
}