    package com.fiap.autria.ui.screens.home

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
    import com.fiap.autria.ui.theme.Blue40
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.FloatingActionButton
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
    import androidx.compose.material3.Scaffold
    import androidx.compose.material3.Text
    import androidx.compose.material3.TopAppBar
    import androidx.compose.material3.TopAppBarDefaults
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.Font
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.fiap.autria.ui.theme.BackgroundDark
    import com.fiap.autria.ui.theme.Blue40
    import com.fiap.autria.ui.theme.Blue40
    import com.fiap.autria.ui.theme.Border40
    import com.fiap.autria.ui.theme.Orange40

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun HomeScreen(
        onSettingsClick: () -> Unit
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        IconButton(
                            onClick = {
                                onSettingsClick()
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_settings_24),
                                contentDescription = "Configurações",
                                tint = Color.Black
                            )
                        }
                    },
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFcc8400), CircleShape)

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Que bom te ver, usuário!",
                        fontSize = 25.sp,
                        fontFamily = FontFamily(Font(R.font.sfpro))
                    )
                }

                Text(
                    text = "Pronto para te acompanhar",
                    fontSize = 50.sp,
                    color = Blue40,
                    modifier = Modifier.padding(start = 16.dp),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .size(60.dp)
                            .border(
                                width = 1.dp,
                                color = Border40,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Blue40, CircleShape)

                        )
                            Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desconectado",
                            color = BackgroundDark,
                            fontSize = 21.sp,
                            fontFamily = FontFamily(Font(R.font.sfpro))
                        )
                    }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .size(60.dp)
                            .border(
                                width = 2.dp,
                                color = Blue40,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                color = _root_ide_package_.com.fiap.autria.ui.theme.Blue40,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Assistente de IA",
                            color = Color.White,
                            fontSize = 21.sp,
                            fontFamily = FontFamily(Font(R.font.sfpro))
                        )
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                    FloatingActionButton(
                        onClick = {

                        },
                        containerColor = _root_ide_package_.com.fiap.autria.ui.theme.Orange40,
                        shape = CircleShape,
                        modifier = Modifier.size(90.dp)

                    ) {
                        Icon(
                            painter = painterResource(R.drawable.autrialogo),
                            contentDescription = "Conectar ao Bluetooth",
                            tint = Color.White
                        )
                    }
                }

            }

        }
    }
