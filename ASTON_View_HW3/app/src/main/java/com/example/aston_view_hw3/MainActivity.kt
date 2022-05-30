package com.example.aston_view_hw3

import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var clocks: CustomViewClock? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainHandler = Handler(Looper.getMainLooper())

        clocks = findViewById(R.id.clocks)

        mainHandler.post(object : Runnable {
            override fun run() {
                val time = getTime()
                setPointers(time[2].toInt(), time[1].toInt(), time[0].toInt())
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    fun getTime(): List<String>{
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        return currentDateAndTime.split(":")
    }

    private fun setPointers(seconds: Int, minutes: Int, hours: Int) {
        clocks?.secondModifier = seconds
        clocks?.minuteModifier = minutes
        if (hours > 12) {
            clocks?.hourModifier = hours - 12
        } else {
            clocks?.hourModifier = hours
        }
    }
}