package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentMatchDetailsBinding
import com.kamrulhasan.crickinfo.databinding.FragmentTeamSquadBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.player.PlayersData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel


class TeamSquadFragment : Fragment() {

    private var _binding: FragmentTeamSquadBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var matchList: List<PlayersData> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTeamSquadBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

//        viewModel.fixturesData.observe(viewLifecycleOwner) {
//
//            matchList = it
//            val adapter = FixtureAdapter(matchList, viewModel, viewLifecycleOwner)
//            binding.matchRecyclerView.adapter = adapter
//
//        }
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_bar)

        /*binding.matchRecyclerView.setOnScrollChangeListener { _: View?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }*/
    }
}