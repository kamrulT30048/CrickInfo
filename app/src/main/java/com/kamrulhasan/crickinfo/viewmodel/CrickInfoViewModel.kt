package com.kamrulhasan.crickinfo.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kamrulhasan.crickinfo.database.CrickInfoDatabase
import com.kamrulhasan.crickinfo.model.country.CountryData
import com.kamrulhasan.crickinfo.model.custom.CustomPlayer
import com.kamrulhasan.crickinfo.model.news.Article
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.leagues.LeaguesData
import com.kamrulhasan.crickinfo.model.lineup.Lineup
import com.kamrulhasan.crickinfo.model.match.Match
import com.kamrulhasan.crickinfo.model.match.MatchData
import com.kamrulhasan.crickinfo.model.officials.OfficialsData
import com.kamrulhasan.crickinfo.model.player.PlayersData
import com.kamrulhasan.crickinfo.model.season.SeasonsData
import com.kamrulhasan.crickinfo.model.squad.Squad
import com.kamrulhasan.crickinfo.model.squad.SquadTeams
import com.kamrulhasan.crickinfo.model.squad.SquadTeamsData
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.crickinfo.model.venues.VenuesData
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.crickinfo.repository.CrickInfoRepository
import com.kamrulhasan.topnews.utils.DateConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

private const val TAG = "CrickInfoViewModel"

class CrickInfoViewModel(application: Application) : AndroidViewModel(application) {

    var fixturesData: LiveData<List<FixturesData>?>
    var upcomingMatch: LiveData<List<FixturesData>?>
    var recentMatch: LiveData<List<FixturesData>?>
    var shortList: LiveData<List<FixturesData>?>
    var teamsData: LiveData<List<TeamsData>>

    private var _news: MutableLiveData<List<Article>?> = MutableLiveData<List<Article>?>()
    val news: LiveData<List<Article>?> = _news

    private var _lineup: MutableLiveData<List<Lineup>?> = MutableLiveData<List<Lineup>?>()
    val lineup: LiveData<List<Lineup>?> = _lineup

    private var _matchDetails: MutableLiveData<MatchData?> = MutableLiveData<MatchData?>()
    val matchDetails: LiveData<MatchData?> = _matchDetails

    private var _liveMatches: MutableLiveData<List<MatchData>?> =
        MutableLiveData<List<MatchData>?>()
    val liveMatches: LiveData<List<MatchData>?> = _liveMatches

    private var _player = MutableLiveData<PlayersData?>()
    val player: LiveData<PlayersData?> = _player

    private var _playerName = MutableLiveData<String?>()
    val playerName: LiveData<String?> = _playerName

    //    private var _playerList = MutableLiveData<List<Squad>?>()
    var playerList: LiveData<List<CustomPlayer>?>

    private val repository: CrickInfoRepository

    init {

        repository = CrickInfoRepository(
            CrickInfoDatabase.getDatabase(application)
                .cricketDao()
        )

        fixturesData = repository.readAllFixturesData

        recentMatch = repository.readRecentFixtures(
            DateConverter.todayDate(),
            DateConverter.passedThreeMonth()
        )
        shortList = repository.readUpcomingShort(
            DateConverter.todayDate(),
            DateConverter.upcomingThreeMonth(),
            2
        )
        upcomingMatch = repository.readUpcomingFixtures(
            DateConverter.todayDate(),
            DateConverter.upcomingThreeMonth()
        )
        playerList = repository.readAllPlayers
        teamsData = repository.readAllTeamsData

        getLeaguesData()
        getOfficialsData()
        getTeamsData()
        getFixturesData()
        getCountries()
        getSeasons()
        getVenues()
    }

    /// read all players
    fun readUpcomingMatch() {
        upcomingMatch = repository.readUpcomingFixtures(
            DateConverter.todayDate(),
            DateConverter.upcomingThreeMonth()
        )
    }

    /// read all players
    fun readAllPlayers() {
        playerList = repository.readAllPlayers
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

    // Venues Name
    fun readVenuesNameById(id: Int): LiveData<String> {
        return repository.readVenuesNameById(id)
    }

    // Venues City
    fun readVenuesCityById(id: Int): LiveData<String> {
        return repository.readVenuesCityById(id)
    }

    // player name
    fun readPlayerNameById(id: Int): LiveData<String> {
        return repository.readPlayerNameById(id)
//        if(player.value == ""){
//            getPlayerById(id)
//            player = repository.readPlayerNameById(id)
//        }

    }

    fun readPlayerCountryById(id: Int): LiveData<Int>? {
        return repository.readPlayerCountryById(id)
    }
    fun readPlayerImageUrlById(id: Int): LiveData<String>? {
        return repository.readPlayerImageUrlById(id)
    }

    // read recent matches short list
    fun readRecentMatchShortList(limit: Int) {
        shortList = repository.readUpcomingShort(
            DateConverter.todayDate(),
            DateConverter.upcomingThreeMonth(),
            limit
        )
        Log.d(TAG, "readRecentMatchShortList: ${shortList.value}")
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
        val firstDate = DateConverter.todayDate()
        val lastDate = DateConverter.upcomingThreeMonth()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dateRange = "$firstDate,$lastDate"
                val upcomingTemp = CricketApi.retrofitService.getFixturesByDate(dateRange).data
                Log.d(TAG, "getUpcomingMatches: ${upcomingTemp?.size}")

                upcomingTemp?.forEach {
                    repository.addFixturesData(it)
                }
            } catch (e: Exception) {
                Log.e(TAG, "getUpcomingMatches: $e")
            }
            try {
                upcomingMatch = repository.readUpcomingFixtures(firstDate, lastDate)
            } catch (e: Exception) {
                Log.e(TAG, "getUpcomingMatches: $e")
            }
        }
    }

    // recent matches
    fun getRecentMatches() {
        val todayDate = DateConverter.todayDate()
        val passedDate = DateConverter.passedThreeMonth()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dateRange = "$passedDate,$todayDate"
                val recentTemp = CricketApi.retrofitService.getFixturesByDate(dateRange).data
                Log.d(TAG, "getRecentMatches: ${recentTemp?.size}")

                recentTemp?.forEach {
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
                Log.d(TAG, "getRecentMatches: $e")
            }
            try {
                upcomingMatch = repository.readRecentFixtures(todayDate, passedDate)
            } catch (e: Exception) {
                Log.e(TAG, "getRecentMatches: $e")
            }
        }
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

    // get match details
    fun getMatchDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val matchTemp = CricketApi.retrofitService.getMatchDetails(id).await()
                _matchDetails.postValue(matchTemp.data)
                Log.d(TAG, "getMatchDetails: ${matchTemp.data}")
            } catch (e: Exception) {
                Log.e(TAG, "getMatchDetails: $e")
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
                    val temp = playerTemp.data
                    val player = CustomPlayer(
                        temp.id,
                        temp.image_path,
                        temp.fullname,
                        temp.country_id,
                    )
                    repository.addPlayer(player)
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
                Log.d(TAG, "getPlayerById: $playerTemp")
                if (playerTemp.data != null) {
                    _playerName.postValue(playerTemp.data.fullname)
                    val temp = playerTemp.data
                    val player = CustomPlayer(
                        temp.id,
                        temp.image_path,
                        temp.fullname,
                        temp.country_id,
                    )
                    repository.addPlayer(player)
                } else {
                    _playerName.value = "NA"
                }
            } catch (e: Exception) {
                Log.e(TAG, "getPlayer: $e")
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

                ////////////////////////////////////////////////
//                getSquadByTeamId(it.id)
                ///////////////////////////////////////////////
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

    // add Venues data into database
    private fun getVenues() {

        viewModelScope.launch(Dispatchers.IO) {
            var seasonsList: List<VenuesData>? = listOf()
            try {
                seasonsList = CricketApi.retrofitService.getVenues().data
            } catch (e: Exception) {
                Log.e(TAG, "getTeamsData: $e")
            }
            seasonsList?.forEach {
                repository.addVenues(it)
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

    // get cricket news
    fun getNewsArticleHome() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val articles = CricketApi.news_retrofitService.getCricketNewsHome().await().articles
                _news.postValue(articles)

            } catch (e: Exception) {
                Log.e("TAG", "getNewsArticleHome: $e")
            }
        }
    }

    // get live matches
    fun getLiveMatches() {

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val matches = CricketApi.news_retrofitService.getLiveMatches().await().data
                _liveMatches.postValue(matches)

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
                val playersCall = CricketApi.retrofitService.getPlayersByTeam(team_id).await()
                val players = playersCall.data?.squad
                Log.d(TAG, "getSquadByTeamId: squad: $players")

                players?.forEach {
                    val player = CustomPlayer(
                        it.id,
                        it.image_path,
                        it.fullname,
                        it.country_id
                    )
                    repository.addPlayer(player)
                }

            } catch (e: Exception) {
                Log.e(TAG, "getTeamSquad: $e")
            }
        }
    }


}