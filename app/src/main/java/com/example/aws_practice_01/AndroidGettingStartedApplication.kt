package com.example.aws_practice_01

import android.app.Application
import com.example.aws_practice_01.backend.Backend

class AndroidGettingStartedApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Backend.initialize(applicationContext)
    }
}
