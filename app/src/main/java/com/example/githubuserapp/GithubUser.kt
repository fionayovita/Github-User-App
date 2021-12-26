package com.example.githubuserapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUser(
    val id: Int,
    val username: String,
    val photo: String,
    val url: String,
    val name: String,
    val company: String,
    val location: String,
    val repository: String,
    val followers: String,
    val following: String
) : Parcelable
