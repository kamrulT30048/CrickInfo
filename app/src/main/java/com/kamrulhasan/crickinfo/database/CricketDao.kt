package com.kamrulhasan.crickinfo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.model.team.TeamsData

@Dao
interface CricketDao {

    @Insert(onConflict = IGNORE)
    suspend fun addTeam(teamsData: TeamsData)

    @Insert(onConflict = IGNORE)
    suspend fun addFixtures(fixturesData: FixturesData)

    @Insert(onConflict = IGNORE)
    suspend fun addRun(run: Run)

    @Query("SELECT * FROM fixtures_data ORDER BY starting_at DESC")
    fun readAllFixturesData(): LiveData<List<FixturesData>>

    @Query("SELECT * FROM team_data WHERE id = :team_id")
    fun readTeamById(team_id: Int) : TeamsData

}