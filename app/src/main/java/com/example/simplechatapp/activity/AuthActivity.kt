package com.example.simplechatapp.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.fragment.app.replace
import com.example.simplechatapp.R
import com.example.simplechatapp.fragment.ChatListFragment
import com.example.simplechatapp.fragment.ProfileFragment
import com.example.simplechatapp.fragment.SignInFragment
import com.example.simplechatapp.fragment.SignUpFragment
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_sign_in_.*
import kotlinx.android.synthetic.main.fragment_sign_in_.view.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class AuthActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        loadFragments()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadFragments(){
        val signInFragment = SignInFragment()
        val signUpFragment = SignUpFragment()

        supportFragmentManager.beginTransaction().apply {
            sign_in_view.setTextAppearance(R.style.Auth_Tab_Text_Selected)
            sign_up_view.setTextAppearance(R.style.Auth_Tab_Text_Not_Selected)
            sign_in_view.setBackgroundResource(R.drawable.tab_selector_bg)
            sign_up_view.setBackgroundResource(R.drawable.tab_not_selector_bg)
            val replace = replace(R.id.frame_layout_fragment_auth, signInFragment)
            addToBackStack(null)
            commit()
        }

        sign_in_view.setOnClickListener {
            sign_in_view.setTextAppearance(R.style.Auth_Tab_Text_Selected)
            sign_up_view.setTextAppearance(R.style.Auth_Tab_Text_Not_Selected)
            sign_in_view.setBackgroundResource(R.drawable.tab_selector_bg)
            sign_up_view.setBackgroundResource(R.drawable.tab_not_selector_bg)

            sign_up_fragment.sign_up_email.text?.clear()
            sign_up_fragment.sign_up_username.text?.clear()
            sign_up_fragment.sign_up_psw.text?.clear()
            sign_up_fragment.sign_up_psw_repeat.text?.clear()


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout_fragment_auth, signInFragment)
                addToBackStack(null)
                commit()
            }

        }

        sign_up_view.setOnClickListener {
            sign_up_view.setTextAppearance(R.style.Auth_Tab_Text_Selected)
            sign_in_view.setTextAppearance(R.style.Auth_Tab_Text_Not_Selected)
            sign_up_view.setBackgroundResource(R.drawable.tab_selector_bg)
            sign_in_view.setBackgroundResource(R.drawable.tab_not_selector_bg)

            sign_in_fragment.login_psw.text?.clear()
            sign_in_fragment.login_email.text?.clear()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frame_layout_fragment_auth, signUpFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

}