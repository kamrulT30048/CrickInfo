package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.FixtureAdapter
import com.kamrulhasan.crickinfo.adapter.LiveMatchAdapter
import com.kamrulhasan.crickinfo.adapter.NewsAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentHomeBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.news.Article
import com.kamrulhasan.crickinfo.network.NetworkConnection
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.MyApplication
import com.kamrulhasan.topnews.utils.URL_KEY
import com.kamrulhasan.topnews.utils.verifyAvailableNetwork

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var articleList = emptyList<Article>()
    private var _matchList = mutableListOf<List<FixturesData>?>()
    private var matchList = _matchList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]
        val connection = verifyAvailableNetwork(requireActivity() as AppCompatActivity)

        viewModel.readUpcomingMatchShortList(2)
        viewModel.getLiveMatches()

        NetworkConnection().observe(viewLifecycleOwner){
//            if(it){
//                Toast.makeText(MyApplication.appContext, "Internet is connected.", Toast.LENGTH_SHORT).show()
//            }else{
//                Toast.makeText(MyApplication.appContext, "No Internet !!!", Toast.LENGTH_SHORT).show()
//            }
            if (articleList.isEmpty() && !it) {

                binding.ivCloudOff.setImageResource(R.drawable.icon_cloud_off_24)
                binding.ivCloudOff.visibility = View.VISIBLE
                binding.tvCloudOff.visibility = View.VISIBLE
                binding.layoutHomeNews.visibility = View.GONE

            } else if (articleList.isEmpty()) {

                binding.ivCloudOff.setImageResource(R.drawable.icon_loading)
                binding.ivCloudOff.visibility = View.VISIBLE
                binding.tvCloudOff.visibility = View.GONE
                binding.layoutHomeNews.visibility = View.GONE

                viewModel.getNewsArticleHome()

                //handle data loading error
                Handler(Looper.getMainLooper()).postDelayed({
                    if(articleList.isEmpty()){
                        binding.ivCloudOff.setImageResource(R.drawable.icon_sync_problem_24)
                        binding.ivCloudOff.visibility = View.VISIBLE
                        Toast.makeText(
                            MyApplication.appContext, "Data Sync Failed,\n" +
                                    " Refresh Again!!", Toast.LENGTH_SHORT).show()
                    }
                }, 5000)
            }
        }

        if (articleList.isEmpty() && !connection) {

            binding.ivCloudOff.setImageResource(R.drawable.icon_cloud_off_24)
            binding.ivCloudOff.visibility = View.VISIBLE
            binding.tvCloudOff.visibility = View.VISIBLE
            binding.layoutHomeNews.visibility = View.GONE

        } else if (articleList.isEmpty()) {

            binding.ivCloudOff.setImageResource(R.drawable.icon_loading)
            binding.ivCloudOff.visibility = View.VISIBLE
            binding.tvCloudOff.visibility = View.GONE
            binding.layoutHomeNews.visibility = View.GONE

            viewModel.getNewsArticleHome()

            //handle data loading error
            Handler(Looper.getMainLooper()).postDelayed({
                if(articleList.isEmpty()){
                    binding.ivCloudOff.setImageResource(R.drawable.icon_sync_problem_24)
                    binding.ivCloudOff.visibility = View.VISIBLE
                    Toast.makeText(
                        MyApplication.appContext, "Data Sync Failed,\n" +
                            " Refresh Again!!", Toast.LENGTH_SHORT).show()
                }
            }, 5000)
        }


        viewModel.shortList.observe(viewLifecycleOwner) {
            binding.recyclerViewHome.adapter =
                it?.let { it1 -> FixtureAdapter(it1, viewModel, viewLifecycleOwner) }

        }


        viewModel.news.observe(viewLifecycleOwner) {

            if (it != null) {
                articleList = it
                binding.recyclerViewHomeNews.adapter = NewsAdapter(it)
                if (articleList.isNotEmpty()) {
                    binding.ivCloudOff.visibility = View.GONE
                    binding.tvCloudOff.visibility = View.GONE
                    binding.layoutHomeNews.visibility = View.VISIBLE
                }
            }

        }


        binding.tvSeeMoreNews.setOnClickListener {

            findNavController().navigate(R.id.newsFragment)
        }

    }
}