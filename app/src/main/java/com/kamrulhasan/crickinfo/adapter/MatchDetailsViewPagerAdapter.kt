package com.kamrulhasan.crickinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamrulhasan.crickinfo.ui.fragment.*
import com.kamrulhasan.topnews.utils.MATCH_DETAILS_PAGE_COUNTER

class MatchDetailsViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return MATCH_DETAILS_PAGE_COUNTER
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                MatchScoreCardFragment()
            }
            2 -> {
                MatchSquadFragment()
            }
            else -> {
                MatchInfoFragment()
            }

        }
    }
}