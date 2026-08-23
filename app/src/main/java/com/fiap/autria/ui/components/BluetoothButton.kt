package com.fiap.autria.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fiap.autria.R
import com.fiap.autria.ui.theme.Orange40

@Composable
fun BluetoothButton(
    isConnecting: Boolean,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {

        if (isConnecting) {

            val infiniteTransition = rememberInfiniteTransition(
                label = "bluetoothPulse"
            )

            val pulse = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200),
                    repeatMode = RepeatMode.Restart
                ),
                label = "pulse"
            )

            Canvas(
                modifier = Modifier.size(120.dp)
            ) {

                val radius = (size.minDimension / 2f) *
                        (0.60f + (pulse.value * 0.40f))

                drawCircle(
                    color = Color.Black.copy(
                        alpha = 1f - (pulse.value * 0.8f)
                    ),
                    radius = radius,
                    style = Stroke(
                        width = 5.dp.toPx()
                    )
                )
            }
        }

        FloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            modifier = Modifier.size(90.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.autrialogo),
                contentDescription = "Conectar aos óculos via Bluetooth",
                tint = Color.White
            )
        }
    }
}