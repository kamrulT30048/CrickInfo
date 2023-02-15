package com.kamrulhasan.crickinfo.repository

import androidx.lifecycle.LiveData
import com.kamrulhasan.crickinfo.database.CricketDao
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.model.team.TeamsData

class CrickInfoRepository(private val cricketDao: CricketDao) {

    val readAllFixturesData: LiveData<List<FixturesData>> = CricketDao.readAllFixturesData()

    suspend fun addFixturesData(fixturesData: FixturesData){
        cricketDao.addFixtures(fixturesData)
    }

    suspend fun addRun(run: Run){
        cricketDao.addRun(run)
    }

    fun readTeamById(id : Int): TeamsData {
        return cricketDao.readTeamById(id)
    }

    suspend fun addTeams(teamsData: TeamsData){
        cricketDao.addTeam(teamsData)
    }
}