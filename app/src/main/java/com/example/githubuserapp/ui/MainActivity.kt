package com.example.githubuserapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.*
import com.example.githubuserapp.adapter.UserAdapter
import com.example.githubuserapp.database.SettingPreferences
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.viewModel.MainViewModel
import com.example.githubuserapp.viewModel.SettingViewModel
import com.example.githubuserapp.viewModel.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import java.util.*
import kotlin.collections.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var settingViewModel: SettingViewModel
    private val listUser = ArrayList<GithubUser>()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvUsers.setHasFixedSize(true)
        adapter = UserAdapter(listUser)

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        mainViewModel.listUser.observe(this, { listUser ->
            setUserData(listUser)
        })

        mainViewModel.listSearchUser.observe(this, { listSearchUser ->
            setUserData(listSearchUser)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.snackbarText.observe(this, {
            Snackbar.make(
                window.decorView.rootView,
                "User Not Found",
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val searchView = item.actionView as SearchView

                searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
                searchView.queryHint = resources.getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query!!.isEmpty()) {
                            showLoading(false)
                        } else {
                            showLoading(true)
                            mainViewModel.searchUser(query)
                            getUsersSearch(query)
                        }
                        Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                        return true
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        getUsersSearch(newText)
                        return false
                    }
                })
            }
            R.id.favorite -> {
                Toast.makeText(this, "Go to favorites", Toast.LENGTH_SHORT).show()
                val moveIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                Log.d(TAG, "Go to Favorite")
                startActivity(moveIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserData(users: List<UserItem>) {
        val list = arrayListOf<GithubUser>()
        users.forEach {
            val user = GithubUser(
                it.id,
                it.login,
                it.avatarUrl,
                it.htmlUrl,
                "",
                "",
                "",
                "",
                "",
                "",
            )
            if (!listUser.contains(user)) {
                listUser.add(user)
            }
            list.add(user)
        }

        themeSetting()
        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(list)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                showSelectedUser(data)
            }
        })
    }

    private fun themeSetting() {
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        settingViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            })

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun getUsersSearch(text: String) {
        val listFilterUser = ArrayList<GithubUser>()

        for (item in listUser) {
            if (item.username.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                listFilterUser.add(item)
            }
        }
        adapter.setData(listFilterUser)
    }

    private fun showSelectedUser(user: GithubUser) {
        Toast.makeText(this, "You choose " + user.username, Toast.LENGTH_SHORT).show()
        val moveIntent = Intent(this@MainActivity, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_DATA, user)
        Log.d(TAG, "Get Detail User: ${user.username}")
        startActivity(moveIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}