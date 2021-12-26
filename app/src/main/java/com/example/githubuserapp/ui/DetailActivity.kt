package com.example.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuserapp.*
import com.example.githubuserapp.adapter.SectionsPagerAdapter
import com.example.githubuserapp.database.Favorite
import com.example.githubuserapp.database.FavoriteDao
import com.example.githubuserapp.database.FavoriteRoomDatabase
import com.example.githubuserapp.databinding.ActivityDetailBinding
import com.example.githubuserapp.viewModel.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private var favoriteDao: FavoriteDao? = null
    private var favoriteDatabase: FavoriteRoomDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteDatabase = FavoriteRoomDatabase.getDatabase(applicationContext)
        favoriteDao = favoriteDatabase?.favoriteDao()

        val user = intent.getParcelableExtra<GithubUser>(EXTRA_DATA) as GithubUser

        var isChecked = false

        CoroutineScope(Dispatchers.IO).launch {
            val count = checkUser(user.id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.btnFav.isChecked = true
                        isChecked = true
                    } else {
                        binding.btnFav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        with(binding) {
            tvUsername.text = user.username
            Glide.with(this@DetailActivity)
                .load(user.photo)
                .into(imgPhoto)
            tvUrl.text = user.url
            btnShare.setOnClickListener(this@DetailActivity)
            btnFav.setOnClickListener {
                isChecked = !isChecked
                if (isChecked) {
                    insertFavorite(user)
                } else {
                    removeFavorite(user.id)
                }
                btnFav.isChecked = isChecked
            }
        }

        detailViewModel.getDetailUser(user.username)

        findUser(user.username)

        detailViewModel.detailUser.observe(this, { users ->
            setDetailsData(users)
        })

        detailViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        detailViewModel.snackbarText.observe(this, {
            Snackbar.make(
                window.decorView.rootView,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    private fun checkUser(id: Int) = favoriteDao?.checkUser(id)

    private fun setDetailsData(users: UserItem) {
        with(binding) {
            tvName.text = users.name
            tvRepo.text = users.publicRepos.toString()
            tvFollowers.text = users.followers.toString()
            tvFollowing.text = users.following.toString()
            tvCompany.text = users.company
            tvLocation.text = users.location
        }

        if (users.company == null && users.location == null) {
            with(binding) {
                tvCompany.visibility = View.GONE
                tvLocation.visibility = View.GONE
            }
        } else {
            with(binding) {
                tvCompany.visibility = View.VISIBLE
                tvLocation.visibility = View.VISIBLE
            }
        }
    }

    private fun insertFavorite(favorite: GithubUser) {
        CoroutineScope(Dispatchers.IO).launch {
            val favorite = Favorite(
                favorite.id,
                favorite.username,
                favorite.url,
                favorite.photo
            )
            favoriteDao?.insert(favorite)
        }
    }

    private fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteDao?.delete(id)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun findUser(username: String?) {
        detailViewModel.getDetailUser(username!!)
        tabLayout(username)
        Log.d(TAG, "getDetailUser: $username")
    }

    private fun tabLayout(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_share) {
            showLoading(true)
            Toast.makeText(this@DetailActivity, "Shared", Toast.LENGTH_SHORT).show()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "you shared data from GitHub User App")
                type = "text/plain"
            }
            showLoading(false)
            val shareIntent = Intent.createChooser(sendIntent, "Share Data")
            startActivity(shareIntent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_DATA = "extra data"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers
        )
    }
}