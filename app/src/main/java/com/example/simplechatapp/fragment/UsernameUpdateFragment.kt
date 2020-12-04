package com.example.simplechatapp.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.communcators.UserDataCommunicator
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_profile_update.view.*

class UsernameUpdateFragment : BottomSheetDialogFragment() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }
    lateinit var com: UserDataCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val username = view.findViewById<TextInputEditText>(R.id.update_username)
        view.save_update.setOnClickListener {
            if (username != null) {
                applyChanges(username = username.text.toString(), uid = auth.currentUser!!.uid)
                Toast.makeText(activity?.baseContext, "Успешно изменен!", Toast.LENGTH_SHORT).show()
            }
             else Toast.makeText(activity?.baseContext, "Введите имя пользователя", Toast.LENGTH_SHORT).show()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun applyChanges(username:String, uid: String){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(uid)
            .update("username", username)
            .addOnSuccessListener {
                com.userUsername(username)
                dismiss()
                Toast.makeText(activity?.baseContext, "Success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(activity?.baseContext, "Error during saving", Toast.LENGTH_SHORT).show()
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        com = targetFragment as UserDataCommunicator
    }

}