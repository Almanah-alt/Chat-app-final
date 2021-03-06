package com.example.simplechatapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.activity.ChatActivity
import com.example.simplechatapp.adapter.ChatListAdapter
import com.example.simplechatapp.communcators.PagerLifecycleListener
import com.example.simplechatapp.entity.Chat
import com.example.simplechatapp.entity.User
import com.example.simplechatapp.observer.ForegroundBackgroundListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_chat_list.*
import kotlinx.android.synthetic.main.fragment_chat_list.view.*


class ChatListFragment : Fragment(R.layout.fragment_chat_list), PagerLifecycleListener {

    private val db by lazy{ FirebaseFirestore.getInstance()}
    private val auth by lazy{ FirebaseAuth.getInstance()}
    private var username: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customToolbar()
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(
                ForegroundBackgroundListener()
                    .also { this }
            )
        setupViews(auth.currentUser!!.uid,view)
        loadUserData(auth.currentUser!!.uid, view)

    }



    private fun customToolbar(){
        view?.toolbar?.title = R.string.chat_list_toolbar.toString()
    }



    private fun setupViews(id: String, view: View){
        view.init_list_chat.layoutManager = LinearLayoutManager(activity?.baseContext)
        db.collection(FirebaseConstant.CHAT_COLLECTION)
            .whereArrayContains("participantId", id)
            .addSnapshotListener { snapshot, e ->
                if(e != null) {
                    Toast.makeText(activity?.baseContext, e.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                val chats =  snapshot?.documents?.map {
                    it.toObject(Chat::class.java)!!
                }?: emptyList()
                view.init_list_chat.adapter = ChatListAdapter(chats, onClikChat = { it ->
                    for (uid in it.participantId) {
                        if (uid != id) {
                            val intent = Intent(activity?.baseContext, ChatActivity::class.java)
                            intent.putExtra(FirebaseConstant.USER_ID, uid)
                            startActivity(intent)
                        }
                    }
                })

            }

        designRecyclerView(view)
    }

    private fun designRecyclerView(view: View){
        (view.init_list_chat.layoutManager as LinearLayoutManager).reverseLayout = true
        (view.init_list_chat.layoutManager as LinearLayoutManager).stackFromEnd = true
        val mDividerItemDecoration = DividerItemDecoration(
            view.init_list_chat.context,
            (view.init_list_chat.layoutManager as LinearLayoutManager).getOrientation()
        )
        view.init_list_chat.addItemDecoration(mDividerItemDecoration)
    }

    private fun loadUserData(uid:String, view: View){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception !=  null) {
                    Toast.makeText(activity?.baseContext, exception.message, Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                val user = snapshot?.toObject(User::class.java)

                if (user != null) {
//                    Toast.makeText(activity?.baseContext, "Добро пожаловать! ${user.username}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onPauseFragment() {
        Log.i("ONPAUSEFRAGMEN", "CHAT listfragment was paused")
    }

    override fun onResumeFragment() {
        Log.i("ONRESUMEFRAGMENT", "CHAT list fragment was resumed")
    }

}