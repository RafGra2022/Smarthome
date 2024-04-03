package itjet.android.smart.greenhouse

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import itjet.android.smart.greenhouse.model.Sensor
import itjet.android.smart.http.Webclient
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Duration

class GreenhouseWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    val context = context
    override fun doWork(): Result {

        heavyWork()

        val uploadWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<GreenhouseWorker>().setInitialDelay(
                Duration.ofMinutes(15)
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
        checkTemperature()
    }

    private fun checkTemperature(){
        Webclient.getInstance().getRetrofitInstance().getSensorData().enqueue(object :
            Callback<Sensor> {
            override fun onResponse(call: Call<Sensor>, response: Response<Sensor>) {
                val sensor = response.body()
                if (sensor != null && sensor.airTemperature.toFloat() > 30f) {
                    val notif = GreenhouseNotification(context)
                    Log.i("green-temp","greenhouse temp is : "+ sensor.airTemperature)
                    notif.tooHotShow()
                }
            }

            override fun onFailure(call: Call<Sensor>, t: Throwable) {
                Log.v("retrofit", "call failed" + t.cause)
            }
        })
    }
}