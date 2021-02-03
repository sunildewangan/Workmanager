package com.sunil.workmanager1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object{
        const val MESSAGE_STATUS:String = "message_status"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val btnSend = findViewById<Button>(R.id.button)

        val mWorkManager:WorkManager = WorkManager.getInstance()
        val data = Data.Builder()
        data.putString(MESSAGE_STATUS,"50% off in Micromax mobile")
        val oneTimeWorkRequest:OneTimeWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data.build())
            .setInitialDelay(5,TimeUnit.SECONDS)
            .build()

        btnSend.setOnClickListener(View.OnClickListener {
            mWorkManager.enqueue(oneTimeWorkRequest)
        })

        mWorkManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id).observe(this, object :Observer<WorkInfo>{
            override fun onChanged(t: WorkInfo?) {
                t.let {
                    val state: WorkInfo.State? = it?.state
                    tvStatus.append(state.toString()+ "\n")
                }
            }

        })
    }
}