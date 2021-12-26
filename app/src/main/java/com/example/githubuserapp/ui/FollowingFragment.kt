package com.example.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.GithubUser
import com.example.githubuserapp.UserItem
import com.example.githubuserapp.adapter.UserAdapter
import com.example.githubuserapp.databinding.FragmentFollowingBinding
import com.example.githubuserapp.viewModel.DetailViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: UserAdapter
    private val listUser = ArrayList<GithubUser>()
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        with(binding) {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
        }

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        chooseTab(index)

        detailViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })
    }

    private fun setFollowData(users: List<UserItem>) {
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
                it.followersUrl,
                it.followingUrl
            )
            listUser.add(user)
        }
        adapter = UserAdapter(listUser)
        binding.rvFollowing.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                startActivity(
                    Intent(
                        requireContext(),
                        DetailActivity::class.java
                    ).putExtra(DetailActivity.EXTRA_DATA, data)
                )
            }
        })
    }

    private fun chooseTab(index: Int?) {
        val username = arguments?.getString(EXTRA_USERNAME)
        when (index) {
            0 -> {
                detailViewModel.apply{
                    getFollowing(username.toString())
                    listUser.observe(viewLifecycleOwner, { listUser ->
                        setFollowData(listUser)
                    })
                }
            }
            1 -> {
                detailViewModel.apply {
                    getFollowers(username.toString())
                    listUser.observe(viewLifecycleOwner, { listUser ->
                        setFollowData(listUser)
                    })
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val EXTRA_USERNAME = "username"
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowingFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(EXTRA_USERNAME, username)
                }
                Log.d("Index: ", index.toString())
                Log.d("Username: ", username)
            }
    }
}