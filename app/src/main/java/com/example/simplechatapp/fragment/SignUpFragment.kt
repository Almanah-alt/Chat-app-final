package com.example.simplechatapp.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.simplechatapp.R

class SignUpFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
//class Registration : AppCompatActivity() {
//
//    companion object{
//        const val ERROR_MSG_IS_EMPTY = "Fill it"
//        const val SUCCESS_MSG ="SUCCESS!!!!"
//    }
//
//    private val auth by lazy{FirebaseAuth.getInstance()}
//    private val db by lazy{FirebaseFirestore.getInstance()}
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_registration)
//        (this as AppCompatActivity).supportActionBar?.title = "Sign up"
//        signUp()
//    }
//
//
//
//    private fun signUp(){
//        sing_up_btn.setOnClickListener {
//            if (sign_up_email.text!!.isEmpty() || sign_up_username.text!!.isEmpty()|| sign_up_psw.text!!.isEmpty() || sign_up_psw_repeat.text!!.isEmpty())
//            {
//                if (sign_up_email.text!!.isEmpty()) {
//                    sign_up_email.error = ERROR_MSG_IS_EMPTY
//                }
//                if (sign_up_username.text!!.isEmpty()) {
//                    sign_up_username.error = ERROR_MSG_IS_EMPTY
//                }
//                if (sign_up_psw.text!!.isEmpty()) {
//                    sign_up_psw.error = ERROR_MSG_IS_EMPTY
//                }
//                if (sign_up_psw_repeat.text!!.isEmpty()) {
//                    sign_up_psw_repeat.error = ERROR_MSG_IS_EMPTY
//                }
//            } else {
//                if (sign_up_psw.text.toString() == sign_up_psw_repeat.text.toString()) {
//                    auth.createUserWithEmailAndPassword(sign_up_email.text.toString(), sign_up_psw.text.toString())
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                val user = User(
//                                    task.result?.user?.uid!!, sign_up_email.text.toString(), sign_up_username.text.toString(),false
//                                )
//                                successReg(sign_up_email.text.toString())
//                                saveUserData(task.result?.user?.uid!!,user)
//                                return@addOnCompleteListener
//                            }
//                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
//
//                        }
//                } else {
//                    Toast.makeText(this, "Passwords do not match!!!", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//    }
//    private fun successReg(email:String){
//        finish()
//    }
//    private fun saveUserData(
//        id: String,
//        user: User
//    ){
//        db.collection(FirebaseConstant.USER_COLLECTION)
//            .document(id)
//            .set(user).addOnSuccessListener {
//                Log.d("tagtag",SUCCESS_MSG)
//            }
//    }
//}
