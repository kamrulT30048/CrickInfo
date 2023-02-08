package com.kamrulhasan.crickinfo.network

import com.kamrulhasan.crickinfo.model.fixture.Fixture
import com.kamrulhasan.topnews.utils.API_TOKEN
import com.kamrulhasan.topnews.utils.BASE_URL
import com.kamrulhasan.topnews.utils.FIXTURES_API
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CricketAPIService {
    @GET(FIXTURES_API)
    suspend fun getFixturesData(
        @Query("include") include: String = "winnerteam",
        @Query("api_token") api_token: String = API_TOKEN
    ): Fixture
}

object CricketApi {
    val retrofitService: CricketAPIService by lazy { retrofit.create(CricketAPIService::class.java) }
}