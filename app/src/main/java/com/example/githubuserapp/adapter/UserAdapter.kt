package com.example.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubuserapp.GithubUser
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ItemRowUserBinding

class UserAdapter(private var listUser: List<GithubUser>) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(user: List<GithubUser>) {
        listUser = user
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, username, photo, url) = listUser[position]
        holder.binding.tvUsername.text = username
        Glide.with(holder.itemView.context)
            .load(photo)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .placeholder(R.drawable.ic_person)
            .error("could not load picture")
            .into(holder.binding.imgPhoto)
        holder.binding.tvUrl.text = url

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size


    class ListViewHolder(var binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }
}