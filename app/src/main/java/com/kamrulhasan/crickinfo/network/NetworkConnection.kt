package com.kamrulhasan.crickinfo.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.kamrulhasan.topnews.utils.MyApplication

private const val TAG = "NetworkConnection"

class NetworkConnection: LiveData<Boolean>() {
    private val connectivityManager: ConnectivityManager =
        MyApplication.appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback

    override fun onActive() {
        super.onActive()
        updateNetwork()
        when{
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->{
                connectivityManager.registerDefaultNetworkCallback(connectionCallBack())
            }else ->{
                MyApplication.appContext.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
            }
        }
    }

    private fun updateNetwork() {
        val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
        postValue(networkConnection?.isConnected == true)
    }
    private val networkReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            updateNetwork()
        }
    }

//    override fun onInactive() {
//        super.onInactive()
//        try {
//            connectivityManager.unregisterNetworkCallback(connectionCallBack())
//        }catch (e: Exception){
//            Log.e(TAG, "onInactive: $e")
//        }
//
//    }

    private fun connectionCallBack(): ConnectivityManager.NetworkCallback{
        networkConnectionCallback = object : ConnectivityManager.NetworkCallback(){
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postValue(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postValue(false)
            }
        }
        return networkConnectionCallback
    }
}