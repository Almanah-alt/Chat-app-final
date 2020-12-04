package com.example.simplechatapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.entity.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_firend_profile.view.*

class FriendProfileFragment : BottomSheetDialogFragment() {

    private val db by lazy{ FirebaseFirestore.getInstance()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_firend_profile, container, false)
        val argumentUid = arguments?.getString(FirebaseConstant.USER_ID)
        if (argumentUid != null) loadFriendData(argumentUid, view)
        return view
    }

    private fun loadFriendData(uid: String, view: View){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(activity?.baseContext, error.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }
                val user = value?.toObject(User::class.java)
                if (user != null) {
                    view.friend_toolbar_name.text = user.username
                    view.profile_email.text = user.email
                    view.profile_username.text = user.username
                    if (user.userImg != null)
                    {
                        Picasso.get().load(user.userImg).noFade().into(view.friend_profile_img)
                        view.photo_label.visibility = View.INVISIBLE
                    } else view.photo_label.visibility = View.VISIBLE
                }
            }
    }

}