package com.example.githubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.api.ApiConfig
import com.example.githubuserapp.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel: ViewModel() {

    private val _detailUser = MutableLiveData<UserItem>()
    val detailUser: LiveData<UserItem> = _detailUser

    private val _listUser = MutableLiveData<List<UserItem>>()
    val listUser: LiveData<List<UserItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<String>()
    val snackbarText: LiveData<String> = _snackbarText

    fun getDetailUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<UserItem>{
            override fun onResponse(
                call: Call<UserItem>,
                response: Response<UserItem>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _detailUser.value = response.body()
                    Log.d(TAG, "Username: $username")
                } else{
                    Log.e(TAG, "failed getDetailUser: ${response.message()}")
                    _snackbarText.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<UserItem>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackbarText.value = "Error: ${t.message}"
            }

        })
    }

    fun getFollowing(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserItem>>{
            override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                _isLoading.value = false
                if (response.body() != null){
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "fail GetFollowing: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun getFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserItem>>{
            override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                _isLoading.value = false
                if (response.body() != null){
                    _listUser.value = response.body()
                } else {
                    Log.e(TAG, "fail GetFollowers: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}