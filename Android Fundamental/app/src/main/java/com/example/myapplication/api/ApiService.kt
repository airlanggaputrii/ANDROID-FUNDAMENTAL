package com.example.myapplication.api

import com.example.myapplication.dataclass.DetailResponse
import com.example.myapplication.dataclass.User
import com.example.myapplication.dataclass.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
        @GET("search/users")
        fun getUsers(
            @Query("q") query: String
        ): Call<UserResponse>

        @GET("users/{username}")
        fun getUsersDetail(
            @Path("username") username: String
        ): Call<DetailResponse>

        @GET("users/{username}/followers")
        fun getFollowers(
            @Path("username") username: String
        ): Call<List<User>>

        @GET("users/{username}/following")
        fun getFollowing(
            @Path("username") username: String
        ): Call<List<User>>

    } //TOKEN DISEMATKAN DI BUILD GRADLE