package com.kamrulhasan.crickinfo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kamrulhasan.crickinfo.database.CrickInfoDatabase
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.crickinfo.repository.CrickInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "CrickInfoViewModel"

class CrickInfoViewModel(application: Application) : AndroidViewModel(application) {

    private var _fixturesData: MutableLiveData<List<FixturesData>> =
        MutableLiveData<List<FixturesData>>()
    var fixturesData: LiveData<List<FixturesData>>// = _fixturesData!!
    var teamsData: LiveData<List<TeamsData>>
    var teamCode: LiveData<String>? = null

//    var team: TeamsData

    var localTeam: TeamsData? = null
    var visitorTeam: TeamsData? = null

    private val repository: CrickInfoRepository

    init {
        repository = CrickInfoRepository(
            CrickInfoDatabase.getDatabase(application)
                .cricketDao()
        )
        fixturesData = repository.readAllFixturesData

        teamsData = repository.readAllTeamsData
//        teamCode = repository.readTeamCodeById()

        getTeamsData()
        getFixturesData()
    }

    ///  read team code
    fun readTeamCode(id: Int): LiveData<String>{
        return repository.readTeamCodeById(id)
    }

    fun readTeamUrl(id: Int): LiveData<String>{
        return repository.readTeamIconById(id)
    }

    // read run

    fun readScoreById(team_id: Int, fixture_id: Int): LiveData<Int>{
        return repository.readTeamScoreById(team_id, fixture_id)
    }
    fun readWicketById(team_id: Int, fixture_id: Int): LiveData<Int>{
        return repository.readTeamWicketById(team_id, fixture_id)
    }

    private fun readAllFixtures() {
        try {
            fixturesData = repository.readAllFixturesData
        }catch (e: Exception){
            Log.d(TAG, "readAllFixtures: $e")
        }
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
                        if (runs.size == 2) {
                            repository.addRun(runs[0])
                            repository.addRun(runs[1])
                        } else if (runs.size == 1) {
                            repository.addRun(runs[0])
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "getFixturesData: Fixtures Api call failed")
                Log.d(TAG, "getFixturesData: $e")
            }
        }
        readAllFixtures()
    }

    /*fun getTeamById(team_id: Int): TeamsData?{
//        return repository.readTeamById(team_id)
        var team: TeamsData? = null
        viewModelScope.launch(Dispatchers.IO) {
            team = repository.readTeamById(team_id)
        }
        return team
    }
*/
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
            teamsData = repository.readAllTeamsData
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