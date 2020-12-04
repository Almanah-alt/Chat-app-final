package com.example.simplechatapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.simplechatapp.FirebaseConstant
import com.example.simplechatapp.R
import com.example.simplechatapp.activity.NavigationActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_in_.*
import kotlinx.android.synthetic.main.fragment_sign_in_.view.*


class SignInFragment : Fragment(R.layout.fragment_sign_in_) {
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        signIn(view)
        super.onViewCreated(view, savedInstanceState)
    }


    private fun signIn(view: View) {
        view.login_btn.setOnClickListener {
            if (login_email.text!!.isEmpty() || login_psw.text!!.isEmpty()) {
                if (login_email.text!!.isEmpty()) {
                    login_email.error = FirebaseConstant.ERROR_MSG
                }
                if (login_psw.text!!.isEmpty()) {
                    login_psw.error = FirebaseConstant.ERROR_MSG
                }
            } else {
                auth.signInWithEmailAndPassword(
                    login_email.text.toString(),
                    login_psw.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            successLogin()
                            return@addOnCompleteListener
                        }else Toast.makeText(activity?.baseContext, task.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun successLogin() {
        val intent = Intent(activity?.baseContext, NavigationActivity::class.java)
        startActivity(intent)
    }
}
