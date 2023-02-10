package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentRecentMatchBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.MyApplication

private const val TAG = "RecentMatchFragment"

class RecentMatchFragment : Fragment() {
    private var _binding: FragmentRecentMatchBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var matchList: List<FixturesData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecentMatchBinding.inflate(layoutInflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        viewModel.fixturesData.observe(viewLifecycleOwner) {

            matchList = it
            val adapter = FixtureAdapter(matchList, viewModel,viewLifecycleOwner)
            binding.matchRecyclerView.adapter = adapter

        }
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_bar)

        binding.matchRecyclerView.setOnScrollChangeListener { _: View?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                bottomNav.visibility = View.GONE
//                bottomNav.animate().translationY(bottomNav.height.toFloat())
            } else {
                bottomNav.visibility = View.VISIBLE
            }
        }
    }


    // Search menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.search_item)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                Toast.makeText(
                    MyApplication.appContext,
                    "Result has been showed.",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                searchArticle(newText.toString())
                Toast.makeText(
                    MyApplication.appContext,
                    "search completed",
                    Toast.LENGTH_SHORT
                ).show()

                return true
            }
        })
    }

    private fun searchArticle(toString: String) {
        Toast.makeText(MyApplication.appContext, "search completed", Toast.LENGTH_SHORT).show()
    }
}