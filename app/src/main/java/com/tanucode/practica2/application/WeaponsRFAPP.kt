package com.tanucode.practica2.application

import android.app.Application
import com.tanucode.practica2.data.remote.RetrofitHelper
import com.tanucode.practica2.data.WeaponsRepository

class WeaponsRFAPP : Application() {
    private val retrofit by lazy{
        RetrofitHelper().getRetrofit()
    }

    internal val repository by lazy {
        WeaponsRepository(retrofit)
    }
}