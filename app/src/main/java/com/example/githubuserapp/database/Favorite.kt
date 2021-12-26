package com.example.githubuserapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_user")
data class Favorite(
    @PrimaryKey
    val id: Int,
    var username: String,
    var url: String,
    var photo: String
) : Serializable