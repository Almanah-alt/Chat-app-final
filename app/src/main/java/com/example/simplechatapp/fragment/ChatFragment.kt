package com.example.simplechatapp.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.adapter.UserChatAdapter
import com.example.simplechatapp.entity.Chat
import com.example.simplechatapp.entity.Message
import com.example.simplechatapp.entity.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : Fragment() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        view.toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)
        // Inflate the layout for this fragment

//        val uid = intent.getStringExtra(FirebaseConstant.USER_ID)!!
//        val uid = "s"
//        loadFriendData(uid)
//        reverseRecyclerview()
//        setupViews(auth.currentUser!!.uid, uid)
//        startChat(uid)
        return view
    }

    private fun startChat(uid :String){
        send_msg.setOnClickListener {
            createOrGetChat(auth.currentUser!!.uid, uid)
            Toast.makeText(activity?.baseContext,"Sent", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadFriendData(uid:String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception !=  null) {
                    Toast.makeText(activity?.baseContext, exception.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)

                if (user != null) {
                    (this as AppCompatActivity).supportActionBar?.title =user.username
                }
            }
    }

    private fun createOrGetChat(activeUserId: String, friendId: String) {
        db.collection(FirebaseConstant.CHAT_COLLECTION)
            .whereIn("id", listOf(activeUserId+friendId,friendId+activeUserId))
            .get()
            .addOnSuccessListener{
                val chats =  it?.documents?.map {
                    it.toObject(Chat::class.java)!!
                }?: emptyList()
                checkChat(chats,activeUserId,friendId)
            }
    }
    //    private fun creatChat(){
//        db.collection(FirebaseConstant.CHAT_COLLECTION)
//            .addSnapshotListener{ snapshot, e ->
//                if (e != null){
//                    Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
//                    return@addSnapshotListener
//                }
//                val chats =
//
//            }
//    }
    private fun checkChat(chats:List<Chat>, activeUserId: String, friendId: String){
        if (chats.isNotEmpty()){

            for (chat in chats){
                if (chat.id.equals(activeUserId + friendId) || chat.id.equals(friendId + activeUserId)) {
                    val updateChat = Chat(
                        chat.id,
                        chat.participants,
                        chat.participantId,
                        Timestamp.now(),
                        new_msg.text.toString()
                    )
                    updateChat(activeUserId, updateChat)
                } else {
                    db.collection(FirebaseConstant.USER_COLLECTION)
                        .whereIn("uid", listOf(activeUserId, friendId))
                        .addSnapshotListener { snapshot2, e2 ->
                            if (e2 != null) {
                                Log.d("eerrrr", "e2.message")
                                return@addSnapshotListener
                            }
                            val users = snapshot2?.documents?.map {
                                it.toObject(User::class.java)!!
                            } ?: emptyList()
                            val createChat = Chat(
                                activeUserId + friendId,
                                users,
                                listOf(activeUserId, friendId),
                                Timestamp.now(),
                                new_msg.text.toString()
                            )
                            updateChat(activeUserId, createChat)
                        }
                }
            }
        }
        else{
            db.collection(FirebaseConstant.USER_COLLECTION)
                .whereIn("uid", listOf(activeUserId, friendId))
                .addSnapshotListener { snapshot2, e2 ->
                    if (e2 != null) {
                        Log.d("eerrrr", "e2.message")
                        return@addSnapshotListener
                    }
                    val users = snapshot2?.documents?.map {
                        it.toObject(User::class.java)!!
                    } ?: emptyList()
                    val createChat = Chat(
                        activeUserId + friendId,
                        users,
                        listOf(activeUserId, friendId),
                        Timestamp.now(),
                        new_msg.text.toString()
                    )
                    updateChat(activeUserId, createChat)
                }
        }
    }

    private fun updateChat(
        activeUserId: String,
        chat: Chat
    ) {
        db.collection(FirebaseConstant.CHAT_COLLECTION)
            .document(chat.id)
            .set(chat)
            .addOnSuccessListener {
                Log.d("uuuuuuuuuuuuuuuu", "Chat was updated")
                createMessage(activeUserId, chat.id)
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                Log.d("eeeeeeeeeeeeeeeee", "not updated chat")
                return@addOnFailureListener
            }
    }


    private fun createMessage(activeUserId: String, chatId: String){
        val time = Timestamp.now()
        val msg = Message(
            chatId,activeUserId, new_msg.text.toString(),time
        )
        saveMessage(msg)
    }

    private fun saveMessage(message: Message)
    {
        db.collection(FirebaseConstant.MESSAGE_COLLECTION)
            .add(message)
            .addOnSuccessListener{task ->
                Log.d("ssssssuuuuuc", "Message saved")
                new_msg.text.clear()
                return@addOnSuccessListener
            }
            .addOnFailureListener {
                Log.d("ssssssuuuuuc", "Message was not saved")
                return@addOnFailureListener
            }
    }
    private fun reverseRecyclerview(){
        user_chat_list.layoutManager = LinearLayoutManager(activity?.baseContext).apply {
            reverseLayout = true
        }
        Handler().postDelayed({
            user_chat_list.smoothScrollToPosition(0)

        }, 100)

    }



    private fun setupViews(activeUserId: String, friendId: String) {
        db.collection(FirebaseConstant.MESSAGE_COLLECTION)
            .whereIn("chatid", listOf(activeUserId+friendId,friendId+activeUserId))
            .addSnapshotListener { snapshot, e ->
                val msgs = snapshot?.documents?.map { message ->
                    message.toObject(Message::class.java)!!
                }?: emptyList()
                user_chat_list.adapter = UserChatAdapter(msgs)
                Log.d("mmmmmmmmm", msgs.toString())
            }
    }
}