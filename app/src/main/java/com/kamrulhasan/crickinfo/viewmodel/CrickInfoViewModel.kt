package com.kamrulhasan.crickinfo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.kamrulhasan.crickinfo.database.CrickInfoDatabase
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.crickinfo.repository.CrickInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CrickInfoViewModel"

class CrickInfoViewModel(application: Application) : AndroidViewModel(application) {

    private var _fixturesFixturesData: MutableLiveData<List<FixturesData>> =
        MutableLiveData<List<FixturesData>>()
    val fixturesData: LiveData<List<FixturesData>> = _fixturesFixturesData

    //
    private var _teamsData: MutableLiveData<TeamsData> = MutableLiveData<TeamsData>()
    val teamsData: LiveData<TeamsData> = _teamsData
    var team: TeamsData? = null

    var localTeam: TeamsData? = null
    var visitorTeam: TeamsData? = null

    private val repository: CrickInfoRepository

    init {
        repository = CrickInfoRepository(
            CrickInfoDatabase.getDatabase(application)
                .cricketDao())

        getTeamsData()
        getFixturesData()
    }

    private fun getFixturesData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fixturesDataTemp = CricketApi.retrofitService.getFixturesData().data!!
                Log.d(TAG, "getFixturesData: ${fixturesDataTemp.size}")
                fixturesDataTemp.forEach {
                    repository.addFixturesData(it)

                    val runs = it.runs
                    if (runs != null && runs.isNotEmpty()) {
                        if(runs.size == 2){
                            repository.addRun(runs[0])
                            repository.addRun(runs[1])
                        }else if(runs.size == 1){
                            repository.addRun(runs[0])
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "getFixturesData: Fixtures Api call failed")
                Log.d(TAG, "getFixturesData: $e")
            }

        }
    }

    fun getTeamById(team_id: Int){
        var team: TeamsData? = null
        viewModelScope.launch(Dispatchers.IO) {
            team = repository.readTeamById(team_id)
        }
//        return team
    }

    private fun getTeamsData() {
        viewModelScope.launch(Dispatchers.IO) {
            var teamsList: List<TeamsData> = listOf()
            try {
                teamsList = CricketApi.retrofitService.getTeams().data
            } catch (e: Exception) {
                Log.d(TAG, "getTeamsData: $e")
            }
            teamsList.forEach {
                repository.addTeams(it)
            }
        }
    }

    /*fun getLocalTeamById(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                localTeam = CricketApi.retrofitService.getTeamById(id).data
            } catch (e: Exception) {
                localTeam = null
                Log.d(TAG, "getTeamById: $e")
            }
        }
    }

    fun getVisitorTeamById(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                visitorTeam = CricketApi.retrofitService.getTeamById(id).data
            } catch (e: Exception) {
                visitorTeam = null
                Log.d(TAG, "getTeamById: $e")
            }
        }
    }*/
}