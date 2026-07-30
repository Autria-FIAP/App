package com.fiap.autria.ui.screens

import com.fiap.autria.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        bottomBar = {
        },
        topBar = {
            TopAppBar(
                title = {},
                colors =  TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFDF0D5),
            )
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFFfdf0d5)),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(Color(0xFFcc8400), CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Que bom te ver, usuário!",
                    fontSize = 20.sp
                )
            }

            Text(
                text = "Pronto para te acompanhar",
                fontSize = 50.sp,
                color = Color(0xFF1B53BD),
                fontFamily = FontFamily(Font(R.font.sfpro))
            )
            Image(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                alignment = Alignment.Center,
                painter = painterResource(id = R.drawable.imgoculos),
                contentDescription = "Oculos da Autria"
            )
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .size(60.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(12.dp)
                        )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth()
                        .size(60.dp)
                        .border(
                            width = 2.dp,
                            color = Color(0xFF1B53BD),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(
                            color = Color(0xFF1B53BD),
                            shape = RoundedCornerShape(12.dp)
                        )
                )
            }

        }

    }
}
