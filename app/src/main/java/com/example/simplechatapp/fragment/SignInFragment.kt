package com.example.simplechatapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplechatapp.R
import com.example.simplechatapp.activity.NavigationActivity
import kotlinx.android.synthetic.main.fragment_sign_in_.*


class SignInFragment : androidx.fragment.app.Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_sign_in_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn.setOnClickListener {
            val intent = Intent(activity?.baseContext, NavigationActivity::class.java)
            startActivity(intent)
        }
    }
}

//
//package com.example.simplechatapp.activity
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.simplechatapp.R
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.android.synthetic.main.activity_main.*
//
//class MainActivity : AppCompatActivity() {
//
//    private val auth by lazy { FirebaseAuth.getInstance() }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        (this as AppCompatActivity).supportActionBar?.title = "Login"
//        goReg()
//        signIn()
//    }
//    private fun signIn(){
//        login_btn.setOnClickListener {
//            if (login_email.text!!.isEmpty() || login_psw.text!!.isEmpty()) {
//                if (login_email.text!!.isEmpty()) {
//                    login_email.error = Registration.ERROR_MSG_IS_EMPTY
//                }
//                if (login_psw.text!!.isEmpty()) {
//                    login_psw.error = Registration.ERROR_MSG_IS_EMPTY
//                }
//            } else {
//                auth.signInWithEmailAndPassword(
//                    login_email.text.toString(),
//                    login_psw.text.toString()
//                )
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
////                Toast.makeText(this, "Welcome, $email", Toast.LENGTH_LONG).show()
//                            Log.d("tagtag", Registration.SUCCESS_MSG)
//                            successLogin(login_email.text.toString())
//                            return@addOnCompleteListener
//                        }
//                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
//
//                    }
//            }
//        }
//    }
//    private fun successLogin(email: String){
//        val intent = Intent(this, NavigationActivity::class.java)
//        startActivity(intent)
//    }
//
//    private fun goReg(){
//        reg_textView.setOnClickListener {
//            val intent = Intent(this, Registration::class.java)
//            startActivity(intent)
//        }
//    }
//
////    private fun loadDataByEmail(email:String) {
////        firestore.collection(Registration.USER_COLLECTION)
////            .whereEqualTo("email", email)
////            .addSnapshotListener { snapshot, error ->
////                if (error != null) {
////                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
////                    return@addSnapshotListener
////                }
////                snapshot?.documents?.forEach {
////                    val user = it.toObject(User::class.java)
////                    Log.d("TAAAAG", user.toString())
////                }
////            }
////    }
//}
