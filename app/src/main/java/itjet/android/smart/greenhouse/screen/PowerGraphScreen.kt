package itjet.android.smart.greenhouse.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import itjet.android.smart.R
import itjet.android.smart.greenhouse.model.DayPowerUsage
import itjet.android.smart.greenhouse.model.LogLive
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class PowerGraphScreen(private val context: Context) {

    val months: HashMap<Int, String> = hashMapOf(
        1 to context.getString(R.string.jan),
        2 to context.getString(R.string.feb),
        3 to context.getString(R.string.mar),
        4 to context.getString(R.string.apr),
        5 to context.getString(R.string.may),
        6 to context.getString(R.string.jun),
        7 to context.getString(R.string.jul),
        8 to context.getString(R.string.aug),
        9 to context.getString(R.string.sep),
        10 to context.getString(R.string.oct),
        11 to context.getString(R.string.nov),
        12 to context.getString(R.string.dec)
    )
    
    @Composable
    fun Screen(){
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
                val vm = viewModel<LogLive>()
                val screenState by vm.screenState.collectAsState()
                val powerUsage = screenState.powerUsage
                var maxMonthPowerUsage = 0
                for(dayUsage in powerUsage){
                    if(maxMonthPowerUsage < dayUsage.workTime)
                      maxMonthPowerUsage = dayUsage.workTime
                }
                Row(modifier = Modifier.padding(60.dp,20.dp,0.dp,100.dp)) {
                Text(
                    text ="Zużycie energi za : "+ months.get(LocalDate.now().monthValue),
                    textAlign = TextAlign.Right,
                    fontSize = 20.sp,
                    color = Color.White
                )
                }
                Row(modifier = Modifier.padding(0.dp,100.dp,0.dp,100.dp)) {
                    MonthUsage(powerUsage, maxMonthPowerUsage)
                }

            }
        }
    }

    @Composable
    fun MonthUsage(powerUsage :  List<DayPowerUsage>, maxMonthPowerUsage: Int ){
        LazyColumn(Modifier.fillMaxSize()) {
            items(powerUsage) {
                Day(it, maxMonthPowerUsage)
            }

        }
    }

    @Composable
    fun Day(data: DayPowerUsage, maxMonthPowerUsage : Int) {

        Divider(color = Color.White, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = data.date,
                modifier = Modifier
                    .padding(10.dp, 4.dp, 20.dp, 0.dp),
                textAlign = TextAlign.Start,
                fontSize = 15.sp,
                color = Color.White
            )
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
            )
            val metric = LocalContext.current.getResources().getDisplayMetrics()
            val width = metric.widthPixels.toFloat()
            Divider(
                modifier = Modifier
                    .padding(3.dp, 7.dp, 5.dp, 0.dp)
                    .width((((width * 0.20) * data.workTime / maxMonthPowerUsage)).dp),
                color = Color.Red,
                thickness = 5.dp
            )

            Text(
                text = ((BigDecimal(data.workTime).divide(BigDecimal(60.0),2, RoundingMode.HALF_UP)).multiply(BigDecimal(0.8)).setScale(2,RoundingMode.HALF_UP)).toString() + " kWh",
                textAlign = TextAlign.Right,
                fontSize = 15.sp,
                color = Color.White
            )

        }
        Divider(color = Color.White, thickness = 1.dp)

    }
}