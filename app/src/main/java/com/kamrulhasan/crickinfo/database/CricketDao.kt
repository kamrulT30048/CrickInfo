package com.kamrulhasan.crickinfo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import com.kamrulhasan.crickinfo.model.country.Country
import com.kamrulhasan.crickinfo.model.country.CountryData
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.model.leagues.LeaguesData
import com.kamrulhasan.crickinfo.model.officials.OfficialsData
import com.kamrulhasan.crickinfo.model.season.Seasons
import com.kamrulhasan.crickinfo.model.season.SeasonsData
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.crickinfo.model.venues.VenuesData

@Dao
interface CricketDao {

    @Insert(onConflict = IGNORE)
    suspend fun addTeam(teamsData: TeamsData)

    @Insert(onConflict = IGNORE)
    suspend fun addFixtures(fixturesData: FixturesData)

    @Insert(onConflict = IGNORE)
    suspend fun addRun(run: Run)

    @Insert(onConflict = IGNORE)
    suspend fun addOfficials(officialsData: OfficialsData)

    @Insert(onConflict = IGNORE)
    suspend fun addLeagues(leaguesData: LeaguesData)

    @Insert(onConflict = IGNORE)
    suspend fun addSeasons(seasonsData: SeasonsData)

    @Insert(onConflict = IGNORE)
    suspend fun addVenues(venuesData: VenuesData)

    @Insert(onConflict = IGNORE)
    suspend fun addCountries(countryData: CountryData)

    @Query("SELECT * FROM fixtures_data ORDER BY starting_at DESC")
    fun readAllFixturesData(): LiveData<List<FixturesData>?>

    // read upcoming matches
    @Query(
        "SELECT * FROM fixtures_data " +
                "WHERE starting_at BETWEEN :todayDate AND :lastDate " +
                "ORDER BY starting_at ASC"
    )
    fun readUpcomingFixtures(todayDate: String, lastDate: String): LiveData<List<FixturesData>?>

    /// read recent matches
    @Query(
        "SELECT * FROM fixtures_data " +
                "WHERE starting_at BETWEEN :passedDate AND :todayDate " +
                "ORDER BY starting_at DESC"
    )
    fun readRecentFixtures(todayDate: String, passedDate: String): LiveData<List<FixturesData>?>

    @Query("SELECT * FROM team_data ")
    fun readAllTeam(): LiveData<List<TeamsData>>

    ///  read team information

    @Query("SELECT code FROM team_data WHERE id = :team_id")
    fun readTeamCodeById(team_id: Int): LiveData<String>

    @Query("SELECT image_path FROM team_data WHERE id = :team_id")
    fun readTeamIconById(team_id: Int): LiveData<String>

    /// run information

    @Query("SELECT * FROM run WHERE id = :run_id")
    fun readRunById(run_id: Int): LiveData<Run>

    // score
    @Query("SELECT score FROM run WHERE team_id = :team_id AND fixture_id = :fixture_id")
    fun readTeamScoreById(team_id: Int, fixture_id: Int): LiveData<Int>

    // wicket
    @Query("SELECT wickets FROM run WHERE team_id = :team_id AND fixture_id = :fixture_id")
    fun readTeamWicketById(team_id: Int, fixture_id: Int): LiveData<Int>

    // over
    @Query("SELECT overs FROM run WHERE team_id = :team_id AND fixture_id = :fixture_id")
    fun readTeamOverById(team_id: Int, fixture_id: Int): LiveData<Double>

    // officials
    @Query("SELECT fullname FROM official_table WHERE id = :officials_id ")
    fun readUmpireNameById(officials_id: Int): LiveData<String>

    // Leagues
    @Query("SELECT name FROM leagues_table WHERE id = :leagueId ")
    fun readLeaguesById(leagueId: Int): LiveData<String>

    // read country
    @Query("SELECT name FROM country_table WHERE id = :countryId ")
    fun readCountryById(countryId: Int): LiveData<String>

    // read venues
    @Query("SELECT name FROM venues_table WHERE id = :id ")
    fun readVenuesNameById(id: Int): LiveData<String>

    // read venues
    @Query("SELECT city FROM venues_table WHERE id = :id ")
    fun readVenuesCityById(id: Int): LiveData<String>

}