package com.example.simplechatapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.R
import com.example.simplechatapp.entity.User
import kotlinx.android.synthetic.main.user_item.view.*

class UserListAdapter(
    private val users: List<User> = emptyList(),
    private val onUserClick:(User) -> Unit
):RecyclerView.Adapter<UserListAdapter.UserListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bindUser(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
    inner class UserListViewHolder(
        private val view: View
    ):RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bindUser(user: User){
            view.list_user_username.text = user.username
            view.list_user_email.text = user.email
            if (user.status){
                view.list_user_status.text = "online"
            }else view.list_user_status.text = "offline"
            view.setOnClickListener{
                onUserClick(user)
            }
        }
    }
}