package com.fiap.autria.data.network

import android.content.Context
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.util.Log
import com.google.gson.Gson
import com.fiap.autria.data.models.NavigationResponse
import com.fiap.autria.data.models.NavigationRequest
import com.fiap.autria.data.models.Metadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.Response
import java.util.concurrent.TimeUnit
import java.util.Locale

/**
 * Serviço de WebSocket para comunicação em tempo real com backend
 */
class NavigationWebSocketService(
    private val context: Context,
    private val backendUrl: String = "ws://10.0.2.2:8000",
    private val onConnectionChanged: ((Boolean) -> Unit)? = null
){

    private var webSocket: WebSocket? = null
    private val gson = Gson()
    private val responseChannel = Channel<NavigationResponse>(Channel.BUFFERED)
    private var textToSpeech: TextToSpeech? = null
    private var vibrator: Vibrator? = null
    private var frameId = 0
    private var isConnected = false

    companion object {
        private const val TAG = "NavigationWebSocket"
        private const val WEBSOCKET_ENDPOINT = "/api/v1/app/tempo-real"
        private const val CONNECTION_TIMEOUT_SECONDS = 30L
        private const val READ_TIMEOUT_SECONDS = 0L
    }

    init {
        initializeTextToSpeech()
        initializeVibrator()
    }

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.apply {
                    language = Locale("pt", "BR")
                }
                Log.d(TAG, "✅ TextToSpeech inicializado")
            } else {
                Log.e(TAG, "❌ Erro ao inicializar TextToSpeech")
            }
        }
    }

    private fun initializeVibrator() {
        vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    /**
     * Conecta ao backend e começa a receber mensagens de navegação
     */
    fun connect(): Flow<NavigationResponse> = flow {
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "🔄 Conectando ao backend: $backendUrl$WEBSOCKET_ENDPOINT")

                val client = OkHttpClient.Builder()
                    .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()

                val request = Request.Builder()
                    .url("$backendUrl$WEBSOCKET_ENDPOINT")
                    .build()

                webSocket = client.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        isConnected = true
                        onConnectionChanged?.invoke(true)
                        Log.d(TAG, "✅ Conectado ao backend!")

                        // Envia o primeiro frame assim que conectar
                        sendFrameRequest(0)
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        try {
                            Log.d(TAG, "📨 Mensagem: $text")
                            val response = gson.fromJson(text, NavigationResponse::class.java)
                            responseChannel.trySend(response).getOrNull()
                            Log.d(TAG, "✅ Mensagem processada: frame=${response.frameId}")
                        } catch (e: Exception) {
                            Log.e(TAG, "❌ Erro ao parsear JSON: ${e.message}")
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        isConnected = false
                        onConnectionChanged?.invoke(false)
                        Log.e(TAG, "❌ Erro na conexão: ${t.message}")
                        t.printStackTrace()
                        responseChannel.close(t)
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        isConnected = false
                        onConnectionChanged?.invoke(false)
                        Log.d(TAG, "🔌 Desconectado. Code: $code, Reason: $reason")
                        responseChannel.close()
                    }
                })

                for (response in responseChannel) {
                    emit(response)

                    if (response.message.isNotEmpty()) {
                        speakMessage(response.message)
                    }

                    if (response.urgent) {
                        vibrateUrgent()
                    } else {
                        vibrateFeedback(response)
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "❌ Exceção na conexão: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Envia um novo frame para processamento
     */
    fun sendFrameRequest(frameId: Int) {
        if (!isConnected) {
            Log.w(TAG, "⚠️ Não conectado ao backend")
            return
        }

        try {
            this.frameId = frameId
            val request = NavigationRequest(
                frameId = frameId,
                metadata = Metadata(
                    timestamp = System.currentTimeMillis().toString()
                )
            )

            val jsonData = gson.toJson(request)
            Log.d(TAG, "📤 Enviando: $jsonData")
            webSocket?.send(jsonData)

        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao enviar frame: ${e.message}")
        }
    }

    /**
     * Reproduz mensagem via síntese de voz
     */
    private fun speakMessage(message: String) {
        if (textToSpeech == null) {
            Log.w(TAG, "⚠️ TextToSpeech não inicializado")
            return
        }

        try {
            Log.d(TAG, "🔊 Falando: $message")
            textToSpeech?.speak(message, TextToSpeech.QUEUE_FLUSH, null)
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao reproduzir áudio: ${e.message}")
        }
    }

    /**
     * Vibração urgente (padrão SOS)
     */
    private fun vibrateUrgent() {
        try {
            val pattern = longArrayOf(0, 100, 100, 100, 100, 100, 200, 200, 200, 100, 100, 100)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val audioAttributes = android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_ALARM)
                    .build()
                vibrator?.vibrate(
                    android.os.VibrationEffect.createWaveform(pattern, -1),
                    audioAttributes
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao gerar vibração urgente: ${e.message}")
        }
    }

    /**
     * Vibração de feedback baseada na ação
     */
    private fun vibrateFeedback(response: NavigationResponse) {
        try {
            val pattern = when (response.action) {
                "LEFT" -> longArrayOf(0, 50, 50, 50)
                "RIGHT" -> longArrayOf(0, 50, 50, 50)
                "FORWARD" -> longArrayOf(0, 100)
                "STOP" -> longArrayOf(0, 50, 100, 50)
                else -> longArrayOf(0, 50)
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val audioAttributes = android.media.AudioAttributes.Builder()
                    .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                vibrator?.vibrate(
                    android.os.VibrationEffect.createWaveform(pattern, -1),
                    audioAttributes
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(pattern, -1)
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao gerar vibração: ${e.message}")
        }
    }

    /**
     * Desconecta do backend
     */
    fun disconnect() {
        try {
            Log.d(TAG, "🔌 Desconectando...")
            webSocket?.close(1000, "Desconectando do backend")
            isConnected = false
            textToSpeech?.shutdown()
            responseChannel.close()
            Log.d(TAG, "✅ Desconectado com sucesso")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao desconectar: ${e.message}")
        }
    }

    /**
     * Verifica se está conectado
     */
    fun isConnected(): Boolean = isConnected
}