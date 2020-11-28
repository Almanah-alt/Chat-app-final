package com.example.simplechatapp.adapter

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.R
import com.example.simplechatapp.entity.Message
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_chat_in.view.*
import kotlinx.android.synthetic.main.user_chat_out.view.*

class UserChatAdapter(
    private val messages: List<Message> = emptyList()
) : RecyclerView.Adapter<UserChatAdapter.MessageViewHolder>() {

    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutResource = if (viewType == 0)
            R.layout.user_chat_out
        else
            R.layout.user_chat_in
        return MessageViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layoutResource, parent, false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindMessage(message = messages[position])
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].senderId == auth.currentUser!!.uid)
            0
        else
            1
    }

    inner class MessageViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        val date = "MMM d, yyyy HH:mm"
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = SimpleDateFormat(date)

        @RequiresApi(Build.VERSION_CODES.N)
        fun bindMessage(message: Message) {
            if (message.senderId == auth.currentUser!!.uid){
                val date = message.timestamp.toDate()
                val dateString = formatter.format(date)
                view.user_msg_own.text =message.text
                view.user_msg_date_own.text = dateString
            }
            else{
                val date = message.timestamp.toDate()
                val dateString = formatter.format(date)
                view.user_msg.text = message.text
                view.user_msg_date.text = dateString
            }
        }
    }
}