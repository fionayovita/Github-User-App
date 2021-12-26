package com.example.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun insert(favoriteUser: Favorite)

    @Query("SELECT * FROM favorite_user")
    fun getAllFavorites(): LiveData<List<Favorite>>

    @Query("SELECT COUNT(*) FROM favorite_user WHERE favorite_user.id = :id")
    fun checkUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    fun delete(id: Int): Int
}