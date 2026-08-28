package com.fiap.autria.data.models

import com.google.gson.annotations.SerializedName

/**
 * Resposta completa de navegação do backend IA
 */
data class NavigationResponse(
    @SerializedName("frame_id")
    val frameId: Int,

    @SerializedName("action")
    val action: String, // "LEFT", "RIGHT", "FORWARD", "STOP"

    @SerializedName("message")
    val message: String,

    @SerializedName("urgent")
    val urgent: Boolean,

    @SerializedName("distance_cm")
    val distanceCm: Float,

    @SerializedName("sensor_valid")
    val sensorValid: Boolean,

    @SerializedName("battery_percent")
    val batteryPercent: Int,

    @SerializedName("connection_status")
    val connectionStatus: String,

    @SerializedName("risk_level")
    val riskLevel: String,

    @SerializedName("detections")
    val detections: List<Detection> = emptyList()
)

data class Detection(
    @SerializedName("object")
    val objectName: String,

    @SerializedName("confidence")
    val confidence: Float,

    @SerializedName("box")
    val box: List<Int>,

    @SerializedName("occupancy")
    val occupancy: Float
)

data class NavigationRequest(
    @SerializedName("frame_id")
    val frameId: Int,

    @SerializedName("metadata")
    val metadata: Metadata = Metadata()
)

data class Metadata(
    @SerializedName("timestamp")
    val timestamp: String = System.currentTimeMillis().toString()
)

enum class NavigationAction(val value: String) {
    LEFT("LEFT"),
    RIGHT("RIGHT"),
    FORWARD("FORWARD"),
    STOP("STOP")
}

enum class RiskLevel(val value: String) {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high"),
    CRITICAL("critical")
}

enum class ConnectionStatus(val value: String) {
    CONNECTED("connected"),
    CONNECTING("connecting"),
    DISCONNECTED("disconnected"),
    ERROR("error")
}

fun String.toNavigationAction(): NavigationAction = when (this.uppercase()) {
    "LEFT" -> NavigationAction.LEFT
    "RIGHT" -> NavigationAction.RIGHT
    "FORWARD" -> NavigationAction.FORWARD
    "STOP" -> NavigationAction.STOP
    else -> NavigationAction.STOP
}

fun String.toRiskLevel(): RiskLevel = when (this.lowercase()) {
    "low" -> RiskLevel.LOW
    "medium" -> RiskLevel.MEDIUM
    "high" -> RiskLevel.HIGH
    "critical" -> RiskLevel.CRITICAL
    else -> RiskLevel.LOW
}

fun String.toConnectionStatus(): ConnectionStatus = when (this.lowercase()) {
    "connected" -> ConnectionStatus.CONNECTED
    "connecting" -> ConnectionStatus.CONNECTING
    "disconnected" -> ConnectionStatus.DISCONNECTED
    "error" -> ConnectionStatus.ERROR
    else -> ConnectionStatus.DISCONNECTED
}