package itjet.android.smart.greenhouse.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import itjet.android.smart.greenhouse.model.WeatherData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeatherScreen {


    @Preview
    @Composable
    fun Weather() {
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
                val weather = WeatherData()
                val data = listOf(weather, weather)
                LazyColumn(Modifier.fillMaxSize()) {
                    items(data) {
                        Day(it)
                    }

                }
            }
        }
    }


    @Composable
    fun Day(data: WeatherData, modifier: Modifier = Modifier) {
        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .height(70.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray, //Card background color
                contentColor = Color.White  //Card content color,e.g.text
            )

        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = data.sunrise.format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp, 0.dp, 20.dp, 0.dp),
                    textAlign = TextAlign.End
                )
            }
            Row(verticalAlignment = Alignment.Top) {
                if (data.date.isEqual(LocalDate.now())) {
                    Text(text = "dzisiaj", modifier = Modifier.weight(0.6f))
                } else {
                    Text(text = data.date.toString(), modifier = Modifier.weight(0.6f))
                }
                Text(text = data.precipValue.toString(), modifier = Modifier.weight(0.313f))
                Text(text = data.fenomen, modifier = Modifier.weight(1f))
            }
            Row {
                Text(
                    text = data.sunset.format(DateTimeFormatter.ofPattern("HH:mm")).toString(),
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp, 0.dp, 20.dp, 0.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}




