package com.kamrulhasan.crickinfo.repository

import androidx.lifecycle.LiveData
import com.kamrulhasan.crickinfo.database.CricketDao
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.model.team.TeamsData

class CrickInfoRepository(private val cricketDao: CricketDao) {

    val readAllFixturesData: LiveData<List<FixturesData>> = cricketDao.readAllFixturesData()
    val readAllTeamsData: LiveData<List<TeamsData>> = cricketDao.readAllTeam()

    suspend fun addFixturesData(fixturesData: FixturesData) {
        cricketDao.addFixtures(fixturesData)
    }

    suspend fun addRun(run: Run) {
        cricketDao.addRun(run)
    }

    // read team info

    fun readTeamCodeById(id: Int): LiveData<String> {
        return cricketDao.readTeamCodeById(id)
    }

    fun readTeamIconById(id: Int): LiveData<String> {
        return cricketDao.readTeamIconById(id)
    }

    // get run

    fun readRunById(id: Int): LiveData<Run> {
        return cricketDao.readRunById(id)
    }

    fun readTeamScoreById(team_id: Int, fixture_id: Int): LiveData<Int> {
        return cricketDao.readTeamScoreById(team_id,fixture_id)
    }

    fun readTeamWicketById(team_id: Int, fixture_id: Int): LiveData<Int> {
        return cricketDao.readTeamWicketById(team_id,fixture_id)
    }

    suspend fun addTeams(teamsData: TeamsData) {
        cricketDao.addTeam(teamsData)
    }
}