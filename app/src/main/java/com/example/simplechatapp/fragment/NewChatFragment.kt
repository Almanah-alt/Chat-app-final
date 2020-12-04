package com.example.simplechatapp.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.activity.ChatActivity
import com.example.simplechatapp.adapter.UserListAdapter
import com.example.simplechatapp.communcators.PagerLifecycleListener
import com.example.simplechatapp.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_new_chat.view.*

class NewChatFragment : Fragment(R.layout.fragment_new_chat), PagerLifecycleListener {

    private val db by lazy{ FirebaseFirestore.getInstance()}
    private val auth by lazy{ FirebaseAuth.getInstance()}

    override fun onCreate(savedInstanceState: Bundle?) {
        view?.let { initListUsers(it) }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initListUsers(view)
        super.onViewCreated(view, savedInstanceState)
    }

     fun initListUsers(view: View){

        view.init_user_list.layoutManager = LinearLayoutManager(context)
        db.collection(FirebaseConstant.USER_COLLECTION)
            .whereNotEqualTo("uid", auth.currentUser!!.uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map{
                    it.toObject(User::class.java)!!
                }?: emptyList()
                view.init_user_list.adapter = UserListAdapter(users, onUserClick = {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra(FirebaseConstant.USER_ID, it.uid)
                    startActivity(intent)
                })
            }
        designRecyclerView(view)
    }

    private fun designRecyclerView(view: View){
        (view.init_user_list.layoutManager as LinearLayoutManager).reverseLayout = true
        (view.init_user_list.layoutManager as LinearLayoutManager).stackFromEnd = true
        val mDividerItemDecoration = DividerItemDecoration(
            view.init_user_list.context,
            (view.init_user_list.layoutManager as LinearLayoutManager).getOrientation()
        )
        view.init_user_list.addItemDecoration(mDividerItemDecoration)
    }

    override fun onPauseFragment() {
        Log.i("ONPAUSEFRAGMENT", "fragment was paused")
    }

    override fun onResumeFragment() {
        view?.let { initListUsers(it) }
        Log.i("ONREsumeFRAGMENT", "fragment was resumed")

    }


}