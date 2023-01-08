package com.example.aws_practice_01.backend

import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.core.Amplify

object Backend {
    private const val TAG = "Backend"

    fun initialize(applicationContext: Context) : Backend{
        try{
            Amplify.configure(applicationContext)
            Log.i(TAG, "Initialized Amplify")
        } catch (error: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", error)
        }
        return this
    }
}
