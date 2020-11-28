package com.example.simplechatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplechatapp.R
import kotlinx.android.synthetic.main.app_bar_main.view.*

class NewChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_chat, container, false)
        view.toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.toolbar.setNavigationOnClickListener {

         }
    }


}