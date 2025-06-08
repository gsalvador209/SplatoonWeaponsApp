package com.tanucode.practica2.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkConnectionLiveData(context: Context) : LiveData<Boolean>() {
    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
    as ConnectivityManager

    private val callback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        //Consulta inicial
        val activeNetwork = cm.activeNetwork
        val caps = cm.getNetworkCapabilities(activeNetwork)
        postValue(caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)

        //Registro de callback
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(request,callback)
    }

    override fun onInactive() {
        super.onInactive()
        cm.unregisterNetworkCallback(callback)
    }
}