package itjet.android.smart

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import itjet.android.smart.greenhouse.GreenhouseActivity

class NavigatorScreen {

    @Composable
    fun navigator() {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF3B568D),
                                Color(0xFF151A3F),
                                Color(0xFF1B3157),
                            )
                        )
                    )
            ) {
            }
            ButtonPane()
        }
    }

    @Composable
    fun ButtonPane() {
        val context = LocalContext.current
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(Dp(20F)),

                ) {

                LargeFloatingActionButton(
                    onClick = {
                        startGreenhouse(context)
                    },
                    shape = CircleShape,
                    containerColor = Color.White
                ) {
                    Icon(Icons.Filled.Home, "", Modifier.then(Modifier.size((Dp(60F)))))
                }
                LargeFloatingActionButton(
                    onClick = {

                    },
                    shape = CircleShape,
                    containerColor = Color.White
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_fence_24),
                        contentDescription = null, Modifier.then(Modifier.size((Dp(60F))))
                    ) // decorative element, "Large floating action button")
                }
            }
        }
    }

    private fun startGreenhouse(context: Context) {
        context.startActivity(Intent(context, GreenhouseActivity::class.java))
    }
}