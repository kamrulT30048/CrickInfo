package com.kamrulhasan.crickinfo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kamrulhasan.crickinfo.ui.fragment.LiveMatchFragment
import com.kamrulhasan.crickinfo.ui.fragment.PlayersFragment
import com.kamrulhasan.crickinfo.ui.fragment.TeamSquadFragment
import com.kamrulhasan.crickinfo.ui.fragment.UpcomingMatchFragment


class MatchSquadViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> {
                //Team Fragment
                PlayersFragment()
            }
            else -> {
                // Team Fragment
                TeamSquadFragment()
            }

        }
    }
}