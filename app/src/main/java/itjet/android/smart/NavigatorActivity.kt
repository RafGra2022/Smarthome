package itjet.android.smart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import itjet.android.smart.greenhouse.GreenhouseWorker
import itjet.android.smart.http.WiFiContext
import java.time.Duration


class NavigatorActivity : ComponentActivity() {

    val navigatorScreen = NavigatorScreen()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navigatorScreen.navigator()
        }
        startInfiniteWorker()
        WiFiContext.application = application
    }

    private fun startInfiniteWorker() {
        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<GreenhouseWorker>().setInitialDelay(
                Duration.ofMinutes(0)
            ).build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "send_reminder_periodic",
            ExistingWorkPolicy.REPLACE,
            uploadWorkRequest
        )
    }

}