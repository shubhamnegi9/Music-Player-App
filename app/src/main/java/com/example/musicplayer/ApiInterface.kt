package com.example.musicplayer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiInterface {

    @Headers("x-rapidapi-key: cd0d36fbe6msh80813fedc98b405p1e4f7ejsn6c7d35fc51bd",
             "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com")
    @GET("/search")
    fun getMusicData(@Query ("q") query: String): Call<MusicData>

}