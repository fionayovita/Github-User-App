package com.example.githubuserapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.database.SettingPreferences

class ViewModelFactory(private val pref: SettingPreferences) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }
}