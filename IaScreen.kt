package com.fiap.autria.ui.screens.ia

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fiap.autria.R
import com.fiap.autria.data.models.NavigationResponse
import com.fiap.autria.ui.theme.Orange40
import com.fiap.autria.ui.viewmodels.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IaScreen(
    onBackClick: () -> Unit,
    context: Context = LocalContext.current
) {
    val navigationViewModel = remember {
        NavigationViewModel(
            context = context,
            backendUrl = "ws://10.0.2.2:8000"
        )
    }

    val navigationState by navigationViewModel.navigationState.collectAsStateWithLifecycle()
    val connectionStatus by navigationViewModel.connectionStatus.collectAsStateWithLifecycle()
    val errorMessage by navigationViewModel.errorMessage.collectAsStateWithLifecycle()
    val isNavigating by navigationViewModel.isNavigating.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        navigationViewModel.clearError()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Assistente de IA",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.sfpro))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigationViewModel.stopNavigation()
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(12.dp)
                            .background(
                                color = when {
                                    isNavigating && connectionStatus == "Conectado" -> Color.Green
                                    connectionStatus == "Conectando..." -> Color(0xFFFFD700)
                                    else -> Color.Red
                                },
                                shape = CircleShape
                            )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Card de Status
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = when (connectionStatus) {
                                        "Conectado" -> Color.Green
                                        "Conectando..." -> Color(0xFFFFD700)
                                        else -> Color.Red
                                    },
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Status: $connectionStatus",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage!!,
                                fontSize = 12.sp,
                                color = Color.Red,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card de Instruções
            if (navigationState != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            navigationState!!.urgent -> Color(0xFFFF6B6B)
                            navigationState!!.riskLevel == "critical" -> Color(0xFFFF6B6B)
                            navigationState!!.riskLevel == "high" -> Color(0xFFFFA94D)
                            navigationState!!.riskLevel == "medium" -> Color(0xFFFFD43B)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = when (navigationState!!.action) {
                                        "FORWARD" -> Color(0xFF51CF66)
                                        "STOP" -> Color(0xFFFF6B6B)
                                        else -> Color(0xFF4ECDC4)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🎯 ${navigationState!!.action}",
                                fontSize = 24.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.sfpro))
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "💬 ${navigationState!!.message}",
                            fontSize = 16.sp,
                            color = if (navigationState!!.urgent || navigationState!!.riskLevel == "critical")
                                Color.White
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = FontFamily(Font(R.font.sfpro))
                        )

                        if (navigationState!!.urgent) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Badge(text = "🚨 URGENTE!", backgroundColor = Color(0xFFCC0000))
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Frame: ${navigationState!!.frameId}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "👋",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Olá, usuário",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = FontFamily(Font(R.font.sfpro))
                        )
                        Text(
                            text = "Como posso te ajudar hoje?",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card de Telemetria
            navigationState?.let { response ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "📊 Telemetria",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.sfpro)),
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TelemetryItem("📏 Distância", "${response.distanceCm} cm")
                            TelemetryItem("⚡ Bateria", "${response.batteryPercent}%")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TelemetryItem(
                                "🔌 Sensor",
                                if (response.sensorValid) "✓" else "✗"
                            )
                            TelemetryItem("⚠️ Risco", response.riskLevel.uppercase())
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card de Detecções
            navigationState?.let { response ->
                if (response.detections.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "👁️ Objetos Detectados",
                                fontSize = 14.sp,
                                fontFamily = FontFamily(Font(R.font.sfpro)),
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            response.detections.forEach { detection ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = detection.objectName,
                                        fontSize = 12.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    LinearProgressIndicator(
                                        progress = detection.confidence,
                                        modifier = Modifier
                                            .width(60.dp)
                                            .height(4.dp),
                                        color = Color.Green,
                                        trackColor = Color.LightGray
                                    )
                                    Text(
                                        text = "${(detection.confidence * 100).toInt()}%",
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botões de Controle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { /* Implementar entrada por teclado */ },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Digitar",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .background(
                            color = if (isNavigating) Color.Red else MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            if (isNavigating) {
                                navigationViewModel.stopNavigation()
                            } else {
                                navigationViewModel.startNavigation()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = if (isNavigating) "Parar" else "Iniciar",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TelemetryItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )
    }
}

@Composable
private fun Badge(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.sfpro))
        )
    }
}