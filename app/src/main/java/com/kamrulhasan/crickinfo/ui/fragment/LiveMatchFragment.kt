package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.adapter.LiveMatchAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentLiveMatchBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel

class LiveMatchFragment : Fragment() {
    private var _binding: FragmentLiveMatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var matchList: List<FixturesData> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLiveMatchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        viewModel.readUpcomingMatchShortList(2)
        viewModel.getLiveMatches()
        viewModel.liveMatches.observe(viewLifecycleOwner){
            binding.recyclerViewLive.adapter =
                it?.let { it1 -> LiveMatchAdapter(it1,viewModel,viewLifecycleOwner) }
        }
        if(viewModel.liveMatches.value == null){
            viewModel.shortList.observe(viewLifecycleOwner){
                binding.recyclerViewLive.adapter =
                    it?.let { it1 -> FixtureAdapter(it1,viewModel,viewLifecycleOwner) }
            }
        }

       /* */

    }
}