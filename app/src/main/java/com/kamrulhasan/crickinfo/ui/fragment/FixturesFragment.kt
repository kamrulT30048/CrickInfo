package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentFixturesBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel

class FixturesFragment : Fragment() {

    private var _binding : FragmentFixturesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var matchList : List<FixturesData> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFixturesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        viewModel.fixturesData.observe(viewLifecycleOwner){

            matchList = it
            val adapter = FixtureAdapter(matchList,viewModel)
            binding.matchRecyclerView.adapter = adapter

        }
    }
}