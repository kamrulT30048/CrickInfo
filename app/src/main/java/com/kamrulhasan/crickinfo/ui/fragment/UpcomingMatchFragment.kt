package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentUpcomingMatchBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

private const val TAG = "UpcomingMatchFragment"

class UpcomingMatchFragment : Fragment() {
    private var _binding: FragmentUpcomingMatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel


    private var matchList: List<FixturesData>? = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpcomingMatchBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        val today = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val todayDate = formatter.format(today.time)

        today.add(Calendar.MONTH, 3)
        val lastDate = formatter.format(today.time)

        val upcomingDate = "$todayDate,$lastDate"
        Log.d(TAG, "onViewCreated: dateLimit: $upcomingDate")

        viewModel.getUpcomingMatches()
        binding.matchRecyclerView.setHasFixedSize(true)

        viewModel.upcomingMatch.observe(viewLifecycleOwner) {
            Log.d(TAG, "onViewCreated: before $it")

            if (it != null) {
                val adapterViewState =
                    binding.matchRecyclerView.layoutManager?.onSaveInstanceState()
                binding.matchRecyclerView.layoutManager?.onRestoreInstanceState(adapterViewState)

                Log.d(TAG, "onViewCreated: $it")
                binding.matchRecyclerView.adapter =
                    FixtureAdapter(it, viewModel, viewLifecycleOwner)
            } else {
                Log.d(TAG, "onViewCreated: null")
            }

        }
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_bar)

        binding.matchRecyclerView.setOnScrollChangeListener { _: View?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                bottomNav.visibility = View.GONE
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_bar)
        bottomNav.visibility = View.VISIBLE
    }
}