package com.example.simplechatapp.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.FirebaseConstant.Companion.SUCCESS_MSG
import com.example.simplechatapp.R
import com.example.simplechatapp.activity.NavigationActivity
import com.example.simplechatapp.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : androidx.fragment.app.Fragment(R.layout.fragment_sign_up) {

    private val auth by lazy{ FirebaseAuth.getInstance()}
    private val db by lazy{ FirebaseFirestore.getInstance()}

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        signUp(view)

        super.onViewCreated(view, savedInstanceState)
    }

    private fun signUp(view: View){
        view.sing_up_btn.setOnClickListener {
            if (sign_up_email.text!!.isEmpty() || sign_up_username.text!!.isEmpty()|| sign_up_psw.text!!.isEmpty() || sign_up_psw_repeat.text!!.isEmpty())
            {
                if (sign_up_email.text!!.isEmpty()) {
                    sign_up_email.error = FirebaseConstant.ERROR_MSG
                }
                if (sign_up_username.text!!.isEmpty()) {
                    sign_up_username.error = FirebaseConstant.ERROR_MSG
                }
                if (sign_up_psw.text!!.isEmpty()) {
                    sign_up_psw.error = FirebaseConstant.ERROR_MSG
                }
                if (sign_up_psw_repeat.text!!.isEmpty()) {
                    sign_up_psw_repeat.error = FirebaseConstant.ERROR_MSG
                }
            } else {
                if (sign_up_psw.text.toString() == sign_up_psw_repeat.text.toString()) {
                    auth.createUserWithEmailAndPassword(sign_up_email.text.toString(), sign_up_psw.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = User(
                                    task.result?.user?.uid!!, sign_up_email.text.toString(), sign_up_username.text.toString(),false, psw =  sign_up_psw.text.toString()
                                )
                                successReg()
                                saveUserData(task.result?.user?.uid!!,user)
                                return@addOnCompleteListener
                            }
                            Toast.makeText(activity?.baseContext, task.exception?.message, Toast.LENGTH_LONG).show()

                        }
                } else {
                    Toast.makeText(activity?.baseContext, "Passwords do not match!!!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun successReg(){
        val intent = Intent(activity?.baseContext, NavigationActivity::class.java)
        startActivity(intent)
    }
    private fun saveUserData(
        id: String,
        user: User
    ){
        db.collection(FirebaseConstant.USER_COLLECTION)
            .document(id)
            .set(user).addOnSuccessListener {
                Log.d("tagtag",SUCCESS_MSG)
            }
    }
}

