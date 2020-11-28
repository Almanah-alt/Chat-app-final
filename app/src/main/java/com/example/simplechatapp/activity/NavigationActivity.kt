package com.example.simplechatapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserImageCommunicator
import com.example.simplechatapp.fragment.BottomSheetFragment
import com.example.simplechatapp.fragment.ChatListFragment
import com.example.simplechatapp.fragment.ProfileFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.bottom_image_choose_view.view.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class NavigationActivity : AppCompatActivity(){
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        loadFragments()
    }


    private fun loadFragments() {
        val chatListFragment = ChatListFragment()
        val profileFragment = ProfileFragment()

        supportFragmentManager.beginTransaction().apply {
            profile_btn.setImageResource(R.drawable.ic_baseline_person_outline_24)
            chat_btn.setImageResource(R.drawable.ic_baseline_chat_24)
            chat_btn.setBackgroundResource(R.drawable.selected_menu_item)
            profile_btn.setBackgroundResource(R.drawable.not_selected_menu_item)
            sign_out.setBackgroundResource(R.drawable.not_selected_menu_item)
            replace(R.id.frame_layout_fragment, chatListFragment)
            addToBackStack(null)
            commit()
        }

        profile_btn.setOnClickListener {
            profile_btn.setImageResource(R.drawable.ic_baseline_person_24)
            chat_btn.setImageResource(R.drawable.ic_outline_chat_24)
            profile_btn.setBackgroundResource(R.drawable.selected_menu_item)
            chat_btn.setBackgroundResource(R.drawable.not_selected_menu_item)
            sign_out.setBackgroundResource(R.drawable.not_selected_menu_item)
           if (searchView != null){
               if (!searchView?.isIconified!!) {
                   searchView!!.onActionViewCollapsed();
               } else {
                   super.onBackPressed();
               }
           }
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout_fragment, profileFragment)
                addToBackStack(null)
                commit()
            }

        }

        chat_btn.setOnClickListener {
            profile_btn.setImageResource(R.drawable.ic_baseline_person_outline_24)
            chat_btn.setImageResource(R.drawable.ic_baseline_chat_24)
            chat_btn.setBackgroundResource(R.drawable.selected_menu_item)
            profile_btn.setBackgroundResource(R.drawable.not_selected_menu_item)
            sign_out.setBackgroundResource(R.drawable.not_selected_menu_item)
            if (searchView != null){
                if (!searchView?.isIconified!!) {
                    searchView!!.onActionViewCollapsed();
                } else {
                    super.onBackPressed();
                }
            }
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout_fragment, chatListFragment)
                addToBackStack(null)
                commit()
            }
        }
        sign_out.setOnClickListener {
            if (searchView != null){
                if (!searchView?.isIconified!!) {
                    searchView!!.onActionViewCollapsed();
                } else {
                    super.onBackPressed();
                }
            }
            supportFragmentManager.beginTransaction().apply {
                finish()
            }
        }
    }

    private fun logout() {
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(auth.currentUser!!.uid)
            .update("status", false)
            .addOnSuccessListener { Log.d("offfffff", "DocumentSnapshot successfully updated!") }
            .addOnFailureListener { e -> Log.d("rrrrrr", "Error updating document", e) }
        auth.signOut()

    }


}