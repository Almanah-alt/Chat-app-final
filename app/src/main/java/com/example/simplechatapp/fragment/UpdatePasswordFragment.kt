package com.example.simplechatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.entity.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_update_password.*
import kotlinx.android.synthetic.main.fragment_update_password.view.*

class UpdatePasswordFragment : BottomSheetDialogFragment() {

    private val db by lazy{ FirebaseFirestore.getInstance()}
    private val auth by lazy{ FirebaseAuth.getInstance()}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val oldPsw = view.findViewById<TextInputEditText>(R.id.old_psw)
        val newPsw = view.findViewById<TextInputEditText>(R.id.new_psw)
        val repNewPsw = view.findViewById<TextInputEditText>(R.id.new_psw1)

        view.save_update_psw.setOnClickListener {
            checkUserPsw(oldPsw = oldPsw.text.toString(), newPsw = newPsw.text.toString(), uid = auth.currentUser!!.uid, repNewPsw = repNewPsw.text.toString())
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkUserPsw(oldPsw: String, newPsw: String, uid: String, repNewPsw: String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .addSnapshotListener { value, error ->
                if (error != null){
                    Toast.makeText(activity?.baseContext, error.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val user = value?.toObject(User::class.java)
                if (user != null) {
                    if (user.psw == oldPsw){
                        if (repNewPsw == newPsw) {
                            applyChanges(newPsw)
                        }else Toast.makeText(activity?.baseContext, "Ваш новый пароль не совпадает", Toast.LENGTH_SHORT).show()
                    }else Toast.makeText(activity?.baseContext, "Старый пароль не правильный!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun applyChanges(newPsw: String){
        auth
            .currentUser
            ?.updatePassword(newPsw)
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(activity?.baseContext, "Успешно!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }else Toast.makeText(activity?.baseContext, it.toString(), Toast.LENGTH_SHORT).show()

            }

        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(auth.currentUser!!.uid)
            .update("psw", newPsw)
            .addOnSuccessListener {
                Toast.makeText(activity?.baseContext, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity?.baseContext, "Error during saving", Toast.LENGTH_SHORT).show()
            }
    }

}