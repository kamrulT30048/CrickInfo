package com.kamrulhasan.crickinfo.network

import com.kamrulhasan.crickinfo.model.fixture.Fixtures
import com.kamrulhasan.crickinfo.model.team.Teams
import com.kamrulhasan.topnews.utils.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

interface CricketAPIService {
    @GET(FIXTURES_END)
    suspend fun getFixturesData(
        @Query("include") include: String = "runs",
        @Query("api_token") api_token: String = API_TOKEN
    ): Fixtures

    @GET("teams/{team_id}")
    suspend fun getTeamById(
        @Path("team_id") team_id: Int,
        @Query("api_token") api_token: String = API_TOKEN
    ): Teams

    @GET(TEAM_END)
    suspend fun getTeams(
        @Query("api_token") api_token: String = API_TOKEN
    ): Teams


}

object CricketApi {
    val retrofitService: CricketAPIService by lazy { retrofit.create(CricketAPIService::class.java) }
}