package com.example.rickandmorty

import com.example.rickandmorty.Api.ResponseAPI
import retrofit2.Call
import retrofit2.http.*

interface DataServices {
    @GET("api/character")
    fun getData() : Call<ResponseAPI>


}