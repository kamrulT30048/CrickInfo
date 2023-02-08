package com.kamrulhasan.crickinfo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.topnews.utils.API_TOKEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CrickInfoViewModel"

class CrickInfoViewModel(application: Application) : AndroidViewModel(application) {

    private var _fixturesFixturesData: MutableLiveData<List<FixturesData>> = MutableLiveData<List<FixturesData>>()
    val fixturesData: LiveData<List<FixturesData>> = _fixturesFixturesData

    init {
        getFixturesData()
    }

    private fun getFixturesData() {
        viewModelScope.launch {
            try {
                _fixturesFixturesData.value = CricketApi.retrofitService.getFixturesData().data
                Log.d(TAG, "getFixturesData: ${fixturesData.value?.size}")
            } catch (e: Exception) {
                Log.d(TAG, "getFixturesData: Fixtures Api call failed")
                Log.d(TAG, "getFixturesData: $e")
            }

        }
    }
}