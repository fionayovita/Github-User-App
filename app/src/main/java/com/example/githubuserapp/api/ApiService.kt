package com.example.githubuserapp.api

import com.example.githubuserapp.UserItem
import com.example.githubuserapp.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    @Headers("Authorization: token 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")
    fun getUser(): Call<List<UserItem>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getFindUser(@Query("q") query: String): Call<UserResponse>

    @GET("users/{login}")
    @Headers("Authorization: token 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")
    fun getDetailUser(@Path("login") login: String): Call<UserItem>

    @GET("users/{login}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(@Path("login") login: String): Call<List<UserItem>>

    @GET("users/{login}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(@Path("login") login: String): Call<List<UserItem>>
}