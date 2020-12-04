package com.example.simplechatapp.fragment

import android.graphics.Bitmap
import android.graphics.Paint
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserDataCommunicator
import com.example.simplechatapp.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(R.layout.fragment_profile), UserDataCommunicator{

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    companion object{
        const val REQUEST_CODE_CAMERA = 0
        const val REQUEST_CODE_GALLERY = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, null)
        loadUserProfile(view, uid = auth.currentUser!!.uid)
        view.profile_password_change.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        view.toolbar.title = getString(R.string.profile_toolbar_title)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.set_profile_img.setOnClickListener {
            val bottomSheetDialog = BottomSheetFragment()
            bottomSheetDialog.setTargetFragment(this, 1)
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "BottomSheet")
        }

        view.profile_username.setOnClickListener {
            val bottomSheetDialog = UsernameUpdateFragment()
            bottomSheetDialog.setTargetFragment(this, 2)
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "UsernameUpdate")
        }
        view.profile_password_change.setOnClickListener {
            val bottomSheetDialog = UpdatePasswordFragment()
            bottomSheetDialog.setTargetFragment(this, 3)
            bottomSheetDialog.show(requireActivity().supportFragmentManager, "PasswordUpdate")
        }
    }

    private fun loadUserProfile(view: View, uid: String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(activity?.baseContext, error.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val user = value?.toObject(User::class.java)
                if (user != null) {
                    view.profile_email.text = user.email
                    view.profile_username.text = user.username
                    if (user.userImg != null)
                    {
                        Picasso.get().load(user.userImg).noFade().into(view.profile_img)
                        view.photo_label?.visibility = View.INVISIBLE
                    }
                }
            }
    }

    override fun userImgUri(bitmap: Bitmap) {
        view?.profile_img?.setImageBitmap(bitmap)
        view?.photo_label?.visibility = View.INVISIBLE
    }

    override fun userUsername(username: String) {
        view?.profile_username?.text = username
    }

}