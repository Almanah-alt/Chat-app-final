package com.example.simplechatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.adapter.UserChatAdapter
import com.example.simplechatapp.entity.Chat
import com.example.simplechatapp.entity.Message
import com.example.simplechatapp.entity.User
import com.example.simplechatapp.fragment.FriendProfileFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    private val db by lazy{ FirebaseFirestore.getInstance()}
    private val auth by lazy{ FirebaseAuth.getInstance()}
    private var username: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val uid = intent.getStringExtra(FirebaseConstant.USER_ID)
        loadFriendData(uid)
        reverseRecyclerview()
        setupViews(auth.currentUser!!.uid, uid)
        startChat(uid)

        backspace_btn.setOnClickListener {
            finish()
        }

        friend_profile.setOnClickListener {
            friendProfile(uid)
        }
    }

    private fun startChat(uid :String){
        send_msg.setOnClickListener {
            createOrGetChat(auth.currentUser!!.uid, uid)
            Toast.makeText(this,"Sent", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadFriendData(uid:String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception !=  null) {
                    Toast.makeText(this, exception.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)

                if (user != null) {
                    Log.i("USERUSER", user.toString())
                    friend_toolbar_name.text = user.username
                    if (user.userImg != null)
                    {
                        Picasso.get().load(user.userImg).noFade().into(friend_profile)
                        photo_label.visibility = View.INVISIBLE
                    }
                    else photo_label.visibility = View.VISIBLE

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
        user_chat_list.layoutManager = LinearLayoutManager(this)
            .apply {
            stackFromEnd = true
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
                val sortedMsgs = msgs.sortedWith(compareBy {
                    it.timestamp
                })
                user_chat_list.adapter = UserChatAdapter(sortedMsgs)
                Log.d("mmmmmmmmm", sortedMsgs.toString())
            }
    }

    private fun friendProfile(uid: String){

        val bundle = Bundle()
        bundle.putString(FirebaseConstant.USER_ID, uid)
        val friendProfileFragment = FriendProfileFragment()
        friendProfileFragment.arguments = bundle
        friendProfileFragment.show(supportFragmentManager, "FriendProfileFragment")


    }
}