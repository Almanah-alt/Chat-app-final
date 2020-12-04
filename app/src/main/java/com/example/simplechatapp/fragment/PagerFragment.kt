package com.example.simplechatapp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.simplechatapp.R
import com.example.simplechatapp.adapter.ViewPagerAdapter
import com.example.simplechatapp.communcators.PagerLifecycleListener
import java.text.FieldPosition


class PagerFragment : Fragment() {

    lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pager, null)
        customViewPager(view)
        return view
    }

    private fun customViewPager(view: View){
        val viewPager: ViewPager = view.findViewById(R.id.viewPager)

        val newChatBtn = view.findViewById<Button>(R.id.start_chat)
        val openUserChats = view.findViewById<ImageView>(R.id.chat_list)
        val pageTitle = view.findViewById<TextView>(R.id.page_title_pager)



        viewPager.adapter = pagerAdapter

        newChatBtn.setOnClickListener {
            viewPager.currentItem = 1
            checkForFragment(viewPager, openUserChats, newChatBtn, pageTitle)
        }

        openUserChats.setOnClickListener {
            viewPager.currentItem = 0
            checkForFragment(viewPager, openUserChats, newChatBtn, pageTitle)
        }

        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                checkForFragment(viewPager, openUserChats, newChatBtn, pageTitle)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        customViewPager(view)
        super.onViewCreated(view, savedInstanceState)
    }
    @SuppressLint("SetTextI18n")
    private fun checkForFragment(viewPager: ViewPager, openUserChats: ImageView, newChatBtn: Button, pageTitle: TextView){
        val pagerNewChatListener = NewChatFragment() as PagerLifecycleListener
        val pagerChatListListener = ChatListFragment() as PagerLifecycleListener

        if (viewPager.currentItem == 0){
            openUserChats.visibility = View.INVISIBLE
            newChatBtn.visibility = View.VISIBLE
            pageTitle.text = getString(R.string.user_chat_list)
            pagerChatListListener.onResumeFragment()
            pagerNewChatListener.onPauseFragment()

        }else {
            openUserChats.visibility = View.VISIBLE
            newChatBtn.visibility = View.INVISIBLE
            pageTitle.text = getString(R.string.new_user_list)
            pagerNewChatListener.onResumeFragment()
            pagerChatListListener.onPauseFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
    }

}