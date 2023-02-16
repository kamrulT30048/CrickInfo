package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.ViewPagerAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentFixturesBinding
import com.kamrulhasan.crickinfo.databinding.FragmentMatchDetailsBinding

class MatchDetailsFragment : Fragment() {

    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMatchDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Live"
                1 -> tab.text = "Upcoming"
                2 -> tab.text = "Recent"
            }
        }.attach()

    }
}