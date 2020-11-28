package com.example.simplechatapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.adapter.UserListAdapter
import com.example.simplechatapp.entity.User
import com.example.simplechatapp.observer.ForegroundBackgroundListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity() {
    companion object{

    }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_list)
        initListUsers()

    }

    private fun initListUsers(){
        init_user_list.layoutManager = LinearLayoutManager(this)
        db.collection(FirebaseConstant.USER_COLLECTION)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                val users = querySnapshot?.documents?.map{
                    it.toObject(User::class.java)!!
                }?: emptyList()
                init_user_list.adapter = UserListAdapter(users, onUserClick = {
//                    val intent = Intent(this, ChatActivity::class.java)
//                    intent.putExtra(FirebaseConstant.USER_ID, it.uid)
//                    startActivity(intent)
                })
            }
        designRecyclerView()
    }

    private fun designRecyclerView(){
        (init_user_list.layoutManager as LinearLayoutManager).reverseLayout = true
        (init_user_list.layoutManager as LinearLayoutManager).stackFromEnd = true
        val mDividerItemDecoration = DividerItemDecoration(
            init_user_list.context,
            (init_user_list.layoutManager as LinearLayoutManager).getOrientation()
        )
        init_user_list.addItemDecoration(mDividerItemDecoration)
    }

    private fun getUserData(){


    }
}
