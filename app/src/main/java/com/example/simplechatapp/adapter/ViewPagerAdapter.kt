package com.example.simplechatapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.simplechatapp.fragment.ChatListFragment
import com.example.simplechatapp.fragment.NewChatFragment

class ViewPagerAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager)
{
    private val count = 2
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = ChatListFragment()
            1 -> fragment = NewChatFragment()
        }
        return fragment ?: ChatListFragment()
    }

    override fun getCount(): Int {
        return count
    }

}