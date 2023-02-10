package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kamrulhasan.crickinfo.databinding.FragmentUpcomingMatchBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel

class UpcomingMatchFragment : Fragment() {
    private var _binding: FragmentUpcomingMatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var matchList: List<FixturesData> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpcomingMatchBinding.inflate(layoutInflater)
        return binding.root
    }
}