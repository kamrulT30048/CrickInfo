package com.kamrulhasan.crickinfo.network

import com.kamrulhasan.crickinfo.model.News.News
import com.kamrulhasan.crickinfo.model.fixture.Fixtures
import com.kamrulhasan.crickinfo.model.leagues.Leagues
import com.kamrulhasan.crickinfo.model.officials.Officials
import com.kamrulhasan.crickinfo.model.officials.OfficialsData
import com.kamrulhasan.crickinfo.model.team.Teams
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.topnews.utils.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val retrofit_news = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_NEWS)
    .build()

interface CricketAPIService {

    @GET(FIXTURES_END)
    suspend fun getFixturesData(
        @Query("include") include: String = "runs",
        @Query("api_token") api_token: String = API_TOKEN
    ): Fixtures

    @GET(FIXTURES_END)
    suspend fun getFixturesByDate(
        @Query("filter[starts_between]") dateRange: String,
        @Query("include") include: String = "runs",
        @Query("api_token") api_token: String = API_TOKEN
    ): Fixtures

    @GET("teams/{team_id}")
    suspend fun getTeamById(
        @Path("team_id") team_id: Int,
        @Query("api_token") api_token: String = API_TOKEN
    ): TeamsData

    @GET(TEAM_END)
    suspend fun getTeams(
        @Query("api_token") api_token: String = API_TOKEN
    ): Teams

    @GET(LEAGUES_END)
    suspend fun getLeagues(
        @Query("api_token") api_token: String = API_TOKEN
    ): Leagues

    @GET(OFFICIALS_END)
    suspend fun getOfficials(
        @Query("api_token") api_token: String = API_TOKEN
    ): Officials

    @GET(GET_CRICKET_NEWS)
    fun getCricketNews(): Call<News>

}

object CricketApi {
    val retrofitService: CricketAPIService by lazy { retrofit.create(CricketAPIService::class.java) }
    val news_retrofitService: CricketAPIService by lazy { retrofit_news.create(CricketAPIService::class.java) }
}
