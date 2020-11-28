package com.example.simplechatapp.adapter

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.entity.Chat
import com.example.simplechatapp.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout_chat_item.view.*

class ChatListAdapter(
    private val chatList: List<Chat> = emptyList(),
    private val onClikChat:(Chat) -> Unit
):RecyclerView.Adapter<ChatListAdapter.ChatViewHolder>(){

    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
            return chatList.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindChat(chatList[position])
    }


    inner class ChatViewHolder(
        private val view: View
    ):RecyclerView.ViewHolder(view){
        val date = "MMM d, yyyy HH:mm"
        @RequiresApi(Build.VERSION_CODES.O)
        val formatter = SimpleDateFormat(date)
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindChat(chat: Chat){
                db.collection(FirebaseConstant.USER_COLLECTION)
                    .whereIn("uid", chat.participantId)
                    .addSnapshotListener{snaphot, e  ->
                        if(e != null){
                            Log.d("tagtaguser", e.message)
                            return@addSnapshotListener
                        }
                        val users = snaphot?.documents?.map {
                            it.toObject(User::class.java)!!
                        } ?: emptyList()
                        for (user in users){
                            if (user.uid != auth.currentUser!!.uid){
                                view.chat_username.text = user.username
                            }
                        }
                    }
            val dateString = formatter.format(chat.last_msgTimestamp.toDate())
            view.chat_last_text.text = chat.last_msg
            view.chat_date.text = dateString
            view.setOnClickListener{
                onClikChat(chat)
            }
        }
    }
}