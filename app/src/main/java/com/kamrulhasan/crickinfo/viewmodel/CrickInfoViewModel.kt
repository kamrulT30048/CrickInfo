package com.kamrulhasan.crickinfo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kamrulhasan.crickinfo.database.CrickInfoDatabase
import com.kamrulhasan.crickinfo.model.country.CountryData
import com.kamrulhasan.crickinfo.model.news.Article
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.leagues.LeaguesData
import com.kamrulhasan.crickinfo.model.lineup.Lineup
import com.kamrulhasan.crickinfo.model.officials.OfficialsData
import com.kamrulhasan.crickinfo.model.player.PlayersData
import com.kamrulhasan.crickinfo.model.season.SeasonsData
import com.kamrulhasan.crickinfo.model.squad.Squad
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.crickinfo.model.upcomingmatch.UpcomingMatchData
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.crickinfo.repository.CrickInfoRepository
import com.kamrulhasan.topnews.utils.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CrickInfoViewModel"

class CrickInfoViewModel(application: Application) : AndroidViewModel(application) {

    var fixturesData: LiveData<List<FixturesData>>
    var teamsData: LiveData<List<TeamsData>>

    private var _news: MutableLiveData<List<Article>?> = MutableLiveData<List<Article>?>()
    val news: LiveData<List<Article>?> = _news

    private var _lineup: MutableLiveData<List<Lineup>?> = MutableLiveData<List<Lineup>?>()
    val lineup: LiveData<List<Lineup>?> = _lineup

    private var _upcomingMatch = MutableLiveData<List<FixturesData>?>()
    var upcomingMatch: LiveData<List<FixturesData>?>

    private var _player = MutableLiveData<PlayersData?>()
    val player: LiveData<PlayersData?> = _player

    private var _playerName = MutableLiveData<String?>()
    val playerName: LiveData<String?> = _playerName

    private var _playerList = MutableLiveData<List<Squad>?>()
    val playerList: LiveData<List<Squad>?> = _playerList


    private val repository: CrickInfoRepository

    //    private val today: Calendar = Calendar.getInstance()
    private val formatter = SimpleDateFormat("yyyy-MM-dd")
    private val today = formatter.format(Calendar.getInstance().time)

    init {

        repository = CrickInfoRepository(
            CrickInfoDatabase.getDatabase(application)
                .cricketDao()
        )

        fixturesData = repository.readAllFixturesData
        upcomingMatch = repository.readUpcomingFixtures(today, today)
        teamsData = repository.readAllTeamsData

        getLeaguesData()
        getOfficialsData()
        getTeamsData()
        getFixturesData()
        getCountries()
        getSeasons()
    }

    ///  read team code
    fun readTeamCode(id: Int): LiveData<String> {
        return repository.readTeamCodeById(id)
    }

    fun readTeamUrl(id: Int): LiveData<String> {
        return repository.readTeamIconById(id)
    }

    /// read run
    fun readScoreById(team_id: Int, fixture_id: Int): LiveData<Int> {
        return repository.readTeamScoreById(team_id, fixture_id)
    }

    fun readWicketById(team_id: Int, fixture_id: Int): LiveData<Int> {
        return repository.readTeamWicketById(team_id, fixture_id)
    }

    fun readOverById(team_id: Int, fixture_id: Int): LiveData<Double> {
        return repository.readTeamOverById(team_id, fixture_id)
    }

    // officials
    fun readOfficialsById(id: Int): LiveData<String> {
        return repository.readOfficialsById(id)
    }

    // officials
    fun readLeaguesById(id: Int): LiveData<String> {
        return repository.readLeaguesById(id)
    }

    // officials
    fun readCountryById(id: Int): LiveData<String> {
        return repository.readCountryById(id)
    }

    // add Matches info into database
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
                Log.e(TAG, "getFixturesData: Fixtures Api call failed")
                Log.e(TAG, "getFixturesData: $e")
            }
        }
        readAllFixtures()
    }

    // get upcoming match
    fun getUpcomingMatches() {

        var today = Calendar.getInstance()
        var formatter = SimpleDateFormat("yyyy-MM-dd")
        //2019-04-01T18:20:38.000000Z"
        var todayDate = formatter.format(today.time)
        today.add(Calendar.MONTH, 5)
        var last = formatter.format(today.time)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dateRange = "$todayDate,$last"
                val upcomingTemp = CricketApi.retrofitService.getFixturesByDate(dateRange).data
                Log.d(TAG, "getUpcomingMatches: ${upcomingTemp?.size}")
                upcomingTemp?.forEach {

                    repository.addFixturesData(date(it))
                }
            } catch (e: Exception) {
                Log.e(TAG, "getUpcomingMatches: $e")
            }
            try {

                today = Calendar.getInstance()
                formatter = SimpleDateFormat("yyyy-MM-dd")
                //2019-04-01T18:20:38.000000Z"
                todayDate = formatter.format(today.time)
                today.add(Calendar.MONTH, 3)
                last = formatter.format(today.time)

                upcomingMatch = repository.readUpcomingFixtures(todayDate, last)
            } catch (e: Exception) {
                Log.e(TAG, "getUpcomingMatches: $e")
            }
        }
    }

    fun date(match: FixturesData): FixturesData {
        val date = if (match.starting_at != null) {
            DateConverter.zoneToDate(match.starting_at!!)
        } else {
            null
        }
        return FixturesData(
            draw_noresult = match.draw_noresult,
            elected = match.elected,
            first_umpire_id = match.first_umpire_id,
            follow_on = match.follow_on,
            id = match.id,
            last_period = match.last_period,
            league_id = match.league_id,
            live = match.live,
            localteam_dl_data = match.localteam_dl_data,
            localteam_id = match.localteam_id,
            man_of_match_id = match.man_of_match_id,
            man_of_series_id = match.man_of_series_id,
            note = match.note,
            referee_id = match.referee_id,
            resource = match.resource,
            round = match.round,
            rpc_overs = match.rpc_overs,
            rpc_target = match.rpc_target,
            runs = match.runs,
            season_id = match.season_id,
            second_umpire_id = match.second_umpire_id,
            stage_id = match.stage_id,
            starting_at = date,
            status = match.status,
            super_over = match.super_over,
            toss_won_team_id = match.toss_won_team_id,
            total_overs_played = match.total_overs_played,
            tv_umpire_id = match.tv_umpire_id,
            type = match.type,
            venue_id = match.venue_id,
            visitorteam_dl_data = match.visitorteam_dl_data,
            visitorteam_id = match.visitorteam_id,
            weather_report = match.weather_report,
            winner_team_id = match.winner_team_id
        )
    }

    // get match lineup
    fun getLineup(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lineupTemp = CricketApi.retrofitService.getLineup(id).await()
                _lineup.postValue(lineupTemp.data?.lineup)
            } catch (e: Exception) {
                Log.e(TAG, "getLineup: $e")
            }
        }
    }

    // ok///get [player] by id
    fun getPlayerById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playerTemp = CricketApi.retrofitService.getPlayerById(id).await()
                Log.d(TAG, "getPlayerById: MOM: $playerTemp")
                if (playerTemp.data != null) {
                    _player.postValue(playerTemp.data)
                } else {
                    _player.value = null
                }
            } catch (e: Exception) {
                Log.e(TAG, "getPlayers: $e")
            }
        }
    }


    // get [player] by id
    fun getPlayerNameById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val playerTemp = CricketApi.retrofitService.getPlayerNameById(id).await()
                Log.d(TAG, "getPlayerById: MOM: $playerTemp")
                if (playerTemp.data != null) {
                    _playerName.postValue(playerTemp.data.fullname)
                } else {
                    _playerName.value = "NA"
                }
            } catch (e: Exception) {
                Log.e(TAG, "getPlayers: $e")
            }
        }
    }

    private fun readAllFixtures() {
        try {
            fixturesData = repository.readAllFixturesData
        } catch (e: Exception) {
            Log.e(TAG, "readAllFixtures: $e")
        }
    }

    // add team data into database
    private fun getTeamsData() {

        viewModelScope.launch(Dispatchers.IO) {
            var teamsList: List<TeamsData> = listOf()
            try {
                teamsList = CricketApi.retrofitService.getTeams().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            teamsList.forEach {
                repository.addTeams(it)
            }
        }
    }

    // add officials data into database
    private fun getOfficialsData() {

        viewModelScope.launch(Dispatchers.IO) {
            var officialsList: List<OfficialsData>? = listOf()
            try {
                officialsList = CricketApi.retrofitService.getOfficials().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            officialsList?.forEach {
                repository.addOfficials(it)
            }
        }
    }

    // add Leagues data into database
    private fun getLeaguesData() {

        viewModelScope.launch(Dispatchers.IO) {
            var leaguesList: List<LeaguesData>? = listOf()
            try {
                leaguesList = CricketApi.retrofitService.getLeagues().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            leaguesList?.forEach {
                repository.addLeagues(it)
            }
        }
    }

    // add Leagues data into database
    private fun getCountries() {

        viewModelScope.launch(Dispatchers.IO) {
            var countriesList: List<CountryData>? = listOf()
            try {
                countriesList = CricketApi.retrofitService.getCountries().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            countriesList?.forEach {
                repository.addCountries(it)
            }
        }
    }

    // add Leagues data into database
    private fun getSeasons() {

        viewModelScope.launch(Dispatchers.IO) {
            var seasonsList: List<SeasonsData>? = listOf()
            try {
                seasonsList = CricketApi.retrofitService.getSeasons().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            seasonsList?.forEach {
                repository.addSeasons(it)
            }
        }
    }

    // get cricket news
    fun getNewsArticle() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val articles = CricketApi.news_retrofitService.getCricketNews().await().articles
                _news.postValue(articles)

            } catch (e: Exception) {
                Log.e("TAG", "getNewsArticle: $e")
            }
        }
    }

    //ok//Team Squad
    fun getSquadByTeamId(team_id: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                Log.d(TAG, "getSquadByTeamId: before")
                val players = CricketApi.retrofitService.getPlayersByTeam(team_id).await()
                Log.d(TAG, "getSquadByTeamId: squad: $players")
                _playerList.postValue(players.data?.squad)

            } catch (e: Exception) {
                Log.e(TAG, "getTeamSquad: $e")
            }
        }
    }

}