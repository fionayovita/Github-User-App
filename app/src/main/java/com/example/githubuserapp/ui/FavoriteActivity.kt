package com.example.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.GithubUser
import com.example.githubuserapp.adapter.UserAdapter
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.viewModel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private val listFavorite = listOf<GithubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteViewModel = FavoriteViewModel(application)

        adapter = UserAdapter(listFavorite)
        setupAdapter()
        favoriteViewModel.getAllFavorites()?.observe(this, {
            val data = it.map { favoriteUser ->
                GithubUser(
                    id = favoriteUser.id,
                    username = favoriteUser.username,
                    photo = favoriteUser.photo,
                    url = favoriteUser.url,
                    name = "",
                    company = "",
                    location = "",
                    repository = "",
                    followers = "",
                    following = "",
                )
            }
            adapter.setData(data)
        })
    }

    private fun setupAdapter() {
        with(binding.rvFavorite) {
            this@FavoriteActivity.adapter.setOnItemClickCallback(object :
                UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GithubUser) {
                    startActivity(
                        Intent(this@FavoriteActivity, DetailActivity::class.java).putExtra(
                            DetailActivity.EXTRA_DATA,
                            data
                        )
                    )
                }

            })
            binding.rvFavorite.layoutManager =
                LinearLayoutManager(this@FavoriteActivity, LinearLayoutManager.VERTICAL, false)
            adapter = this@FavoriteActivity.adapter
        }
    }
}