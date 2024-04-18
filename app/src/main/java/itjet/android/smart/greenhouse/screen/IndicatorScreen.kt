package itjet.android.smart.greenhouse.screen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import itjet.android.smart.R
import itjet.android.smart.greenhouse.Destination
import itjet.android.smart.greenhouse.model.SensorLive
import itjet.android.smart.greenhouse.model.SettingsLive

class IndicatorScreen(activity: Activity) {

    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    var humidity1 = sharedPref.getString("humidity1", "Wilgotność gruntu-1")!!
    var humidity2 = sharedPref.getString("humidity2", "Wilgotność gruntu-2")!!
    var humidity3 = sharedPref.getString("humidity3", "Wilgotność gruntu-3")!!

    @Composable
    fun Indicators(navController: NavHostController) {

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
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 150.dp, 0.dp, 0.dp)
        ) {
            val metric = LocalContext.current.getResources().getDisplayMetrics()
            val height = metric.heightPixels.toFloat()
            val width = metric.widthPixels.toFloat()
            Row(
                modifier = Modifier
                    .height((height * 0.08).dp)
                    .padding((width * 0.05).dp, (height * 0.01).dp, 0.dp, 0.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),

                ) {
                AirTemperature()
                GroundTemperature()
            }
            Row(
                modifier = Modifier
                    .height((height * 0.05).dp)
                    .padding((width * 0.02).dp, (height * 0.01).dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.08).dp),

                ) {
                Text(text = "Temperatura powietrza", fontSize = 15.sp, color = Color.White)
                Text(text = "Temperatura gruntu", fontSize = 15.sp, color = Color.White)

            }
            Row(
                modifier = Modifier
                    .height((height * 0.08).dp)
                    .padding((width * 0.05).dp, (height * 0.01).dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),

                ) {
                AirHumidity()
                GroundHumidity1()
            }
            Row(
                modifier = Modifier
                    .height((height * 0.05).dp)
                    .padding((width * 0.02).dp, 0.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.07).dp),

                ) {
//                Text(text = "Wilgotność powietrza", fontSize = 15.sp, color = Color.White)
                TextField(
                    modifier = Modifier
                        .size((width * 0.25).dp, 50.dp)
                        .height(20.dp)
                        .align(Alignment.Top),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    value = "Wilgotność powietrza",
                    onValueChange = {}
                )
//                Text(text = "Wilgotność gruntu-1", fontSize = 15.sp, color = Color.White)
                var humidity by remember { mutableStateOf(humidity1) }
                TextField(
                    modifier = Modifier
                        .size((width * 0.2).dp, 50.dp)
                        .height(20.dp)
                        .align(Alignment.Top),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    value = humidity,
                    onValueChange = {
                        humidity = it
                        sharedPref.edit().putString("humidity1", it).apply()
                    },
                )
            }
            Row(
                modifier = Modifier
                    .height((height * 0.06).dp)
                    .padding((width * 0.05).dp, 0.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.3).dp),

                ) {
                GroundHumidity2()
                GroundHumidity3()
            }
            Row(
                modifier = Modifier
                    .height((height * 0.05).dp)
                    .padding((width * 0.02).dp, (height * 0.01).dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy((width * 0.07).dp),

                ) {
                var humidity2 by remember { mutableStateOf(humidity2) }
                TextField(
                    modifier = Modifier
                        .size((width * 0.25).dp, 50.dp)
                        .height(20.dp)
                        .align(Alignment.Top),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    value = humidity2,
                    onValueChange = {
                        humidity2 = it
                        sharedPref.edit().putString("humidity2", it).apply()
                    },
                )
//                Text(text = "Wilgotność gruntu-1", fontSize = 15.sp, color = Color.White)
                var humidity3 by remember { mutableStateOf(humidity3) }
                TextField(
                    modifier = Modifier
                        .size((width * 0.2).dp, 50.dp)
                        .height(20.dp)
                        .align(Alignment.Top),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    value = humidity3,
                    onValueChange = {
                        humidity3 = it
                        sharedPref.edit().putString("humidity3", it).apply()
                    },
                )
            }
            Row(
                modifier = Modifier
                    .padding((width * 0).dp, (height * 0.01).dp, 0.dp, 0.dp),
            ) {
//                Spacer(modifier = Modifier.padding((width*0.04).dp,80.dp,0.dp,80.dp))
                SystemStatus()
            }
            Row(
                modifier = Modifier
                    .padding((width * 0.44).dp, 0.dp, (width * 0.02).dp, 0.dp),
            ) {
                Settings(navController)
            }
        }


    }

    @Composable
    fun GroundHumidity3() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        val humidity = screenState.groundHumidity3.toFloat()
        var customColor = Color(0xFF5FB32B)


        if (humidity < 50) {
            customColor = Color(0xFF0EC5BE)
        } else if (humidity >= 50 && humidity < 70) {
            customColor = Color(0xFF5FB32B)
        } else if (humidity >= 70 && humidity < 85) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = humidity / 100
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                }
                it.nativeCanvas.drawText(
                    "$humidity%",
                    width / 10F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun GroundHumidity2() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        val humidity = screenState.groundHumidity2.toFloat()
        var customColor = Color(0xFF5FB32B)


        if (humidity < 50) {
            customColor = Color(0xFF0EC5BE)
        } else if (humidity >= 50 && humidity < 70) {
            customColor = Color(0xFF5FB32B)
        } else if (humidity >= 70 && humidity < 85) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = humidity / 100
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                }
                it.nativeCanvas.drawText(
                    "$humidity%",
                    width / 10F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun GroundHumidity1() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        val humidity = screenState.groundHumidity1.toFloat()
        var customColor = Color(0xFF5FB32B)

        if (humidity < 50) {
            customColor = Color(0xFF0EC5BE)
        } else if (humidity >= 50 && humidity < 70) {
            customColor = Color(0xFF5FB32B)
        } else if (humidity >= 70 && humidity < 85) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = humidity / 100
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                }
                it.nativeCanvas.drawText(
                    "$humidity%",
                    width / 10F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun AirHumidity() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        val humidity = screenState.airHumidity.toFloat()
        var customColor = Color(0xFF5FB32B)


        if (humidity < 55) {
            customColor = Color(0xFF0EC5BE)
        } else if (humidity >= 55 && humidity < 65) {
            customColor = Color(0xFF5FB32B)
        } else if (humidity >= 65 && humidity < 75) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = humidity / 100
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                }
                it.nativeCanvas.drawText(
                    "$humidity%",
                    width / 10F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun AirTemperature() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        var temperature = screenState.airTemperature.toFloat()
        var customColor = Color(0xFF5FB32B)

        if (temperature > 50) {
            temperature = 50F
        } else if (temperature < -20) {
            temperature = -20F
        }

        if (temperature < 17) {
            customColor = Color(0xFF0EC5BE)
        } else if (temperature >= 17 && temperature < 27) {
            customColor = Color(0xFF5FB32B)
        } else if (temperature >= 27 && temperature < 30) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = (20 + temperature) / 70
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .wrapContentSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                }
                it.nativeCanvas.drawText(
                    "$temperature\u00B0C",
                    width / 10F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun GroundTemperature() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
        var temperature = screenState.groundTemperature.toFloat()
        var customColor = Color(0xFF5FB32B)

        if (temperature > 50) {
            temperature = 50F
        } else if (temperature < -20) {
            temperature = -20F
        }

        if (temperature < 15) {
            customColor = Color(0xFF0EC5BE)
        } else if (temperature >= 15 && temperature < 18) {
            customColor = Color(0xFF5FB32B)
        } else if (temperature >= 18 && temperature < 22) {
            customColor = Color(0xFFE9B80A)
        } else {
            customColor = Color(0xFFD10747)
        }

        val sliderValue = (20 + temperature) / 70
        val context = LocalContext.current;
        val metric = context.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val convertedValue = sliderValue * 180
            val sizeArc = Size(width / 3, height / 5)
            drawArc(
                brush = SolidColor(customColor),
                startAngle = 180f,
                sweepAngle = convertedValue,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                style = Stroke(15f),
                size = sizeArc
            )
            drawIntoCanvas {
                val paint = Paint().asFrameworkPaint()
                paint.apply {
                    isAntiAlias = true
                    textSize = 35f
                    color = context.getColor(R.color.white)
                    textAlign = android.graphics.Paint.Align.CENTER
                }
                it.nativeCanvas.drawText(
                    "$temperature\u00B0C",
                    size.width / 2F,
                    height / 10.8F,
                    paint
                )
            }
        }
    }

    @Composable
    fun Settings(navController: NavHostController) {
        val vm = viewModel<SettingsLive>()
        val screenState by vm.settingsState.collectAsState()
        val settingsState = screenState
        Button(
            onClick = {
                navController.navigate(Destination.Settings.route)
            },
            shape = CircleShape,
            enabled = settingsState.updated
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = null, Modifier.then(Modifier.size((Dp(40F))))
            ) // decorative element, "Large floating action button")
        }
    }

    @Composable
    fun SystemStatus() {
        val vm = viewModel<SensorLive>()
        val screenState by vm.screenState.collectAsState()
//        val status = screenState.systemStatus.status
//        if(status.equals("UP")){
//            Image(painterResource(R.drawable.baseline_gpp_good_24), "content description")
//        }else{
//            Image(painterResource(R.drawable.baseline_warning_amber_24), "content description")
//        }
        val metric = LocalContext.current.getResources().getDisplayMetrics()
        val height = metric.heightPixels.toFloat()
        val width = metric.widthPixels.toFloat()
        if(false) {
            Image(painterResource(R.drawable.baseline_electric_bolt_24), "content description")
            Spacer(modifier = Modifier.padding((width*0.21).dp, 0.dp, 0.dp, 0.dp))
//            Spacer(modifier = Modifier.padding((width*0.26).dp, 0.dp, 0.dp, 0.dp))
            Image(painterResource(R.drawable.baseline_gpp_good_24), "content description")
        }
    }
}