package itjet.android.smart.greenhouse

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import itjet.android.smart.greenhouse.model.SensorData
import itjet.android.smart.http.Webclient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GreenhouseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    val context = context
    override fun doWork(): Result {

        heavyWork()

        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<GreenhouseWorker>().setInitialDelay(
                Duration.ofMinutes(10)
            ).build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "send_reminder_periodic",
            ExistingWorkPolicy.REPLACE,
            uploadWorkRequest
        )
        return Result.success()
    }

    private fun heavyWork() {
        Log.i("worker-log", "executed")
        val sharedPreferences = context.getSharedPreferences("green-notif", MODE_PRIVATE)
        val lastAlertText =
            sharedPreferences.getString("last_alert", LocalDate.now().minusDays(1).toString())
        val lastAlert = LocalDate.parse(lastAlertText, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if (lastAlert.isBefore(LocalDate.now())) {
            checkTemperature()
        }

    }

    private fun checkTemperature(){
        Log.i("green-temp","temperature check")
        Webclient.getInstance().getRetrofitInstance(context).getSensorData().enqueue(object :
            Callback<SensorData> {
            override fun onResponse(call: Call<SensorData>, response: Response<SensorData>) {
                val data = response.body()
                val sharedPreferences = context.getSharedPreferences("green-notif",MODE_PRIVATE)
                if (data != null && data.sensors.airTemperature.toFloat() > 30f) {
                    sharedPreferences.edit().putString("last_alert",LocalDate.now().toString()).apply()
                    val notif = GreenhouseNotification(context)
                    Log.i("green-temp","greenhouse temp is : "+ data.sensors.airTemperature)
                    notif.tooHotShow()
                }
            }

            override fun onFailure(call: Call<SensorData>, t: Throwable) {
                Log.v("retrofit", "call failed" + t.cause)
            }
        })
    }
}