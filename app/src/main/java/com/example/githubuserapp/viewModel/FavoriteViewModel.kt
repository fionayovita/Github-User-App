package com.example.githubuserapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.database.Favorite
import com.example.githubuserapp.database.FavoriteDao
import com.example.githubuserapp.database.FavoriteRoomDatabase

class FavoriteViewModel(application: Application) : ViewModel() {
    private var favoriteDao: FavoriteDao? = null
    private var favoriteDatabase: FavoriteRoomDatabase? = null

    init {
        favoriteDatabase = FavoriteRoomDatabase.getDatabase(application)
        favoriteDao = favoriteDatabase?.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<Favorite>>? {
        return favoriteDao?.getAllFavorites()
    }
}