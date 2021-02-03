package com.sunil.workmanager1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParams: WorkerParameters
) : Worker(context, workerParams) {
    companion object{
        const val WORK_RESULT = "work_result"
    }
    override fun doWork(): Result {
        val taskData:Data = inputData
        val taskDataString: String? = taskData.getString(MainActivity.MESSAGE_STATUS)
        println("taskDataString = $taskDataString")
        showNotification("Work Manager", taskDataString?.toString()?:"Message has been sent")
        val outputData:Data = Data.Builder().putString(WORK_RESULT," Job Finished").build()
        return Result.success(outputData)
    }

    private fun showNotification(task:String,desc:String){
        val notificationManager:NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId:String = "task_channel"
        val channelName:String = "task_name"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channelId)
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)

        notificationManager.notify(1,builder.build())
    }
}