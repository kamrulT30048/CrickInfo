package com.kamrulhasan.crickinfo.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.databinding.FragmentPlayerDetailsBinding
import com.kamrulhasan.crickinfo.model.player.Batting
import com.kamrulhasan.crickinfo.model.player.Bowling
import com.kamrulhasan.crickinfo.model.player.Career
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.PLAYER_ID
import kotlin.math.floor

private const val TAG = "PlayerDetailsFragment"

class PlayerDetailsFragment : Fragment() {

    private var _binding: FragmentPlayerDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var player: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            player = it.getInt(PLAYER_ID, 0)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPlayerDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        Log.d(TAG, "onViewCreated: $player")
        viewModel.getPlayerById(player)
        viewModel.player.observe(viewLifecycleOwner) { playersData ->
            playersData?.let { it ->
                Glide
                    .with(requireContext())
                    .load(it.image_path)
                    .centerCrop()
                    .placeholder(R.drawable.icon_person)
                    .into(binding.ivPlayer)

                binding.tvPlayerName.text = it.fullname
                binding.tvFirstName.text = "First_Name: ${it.firstname}"
                binding.tvLastName.text = "Last_Name: ${it.lastname}"
                val dob = it.dateofbirth?.let { dob -> DateConverter.zoneToDate(dob) }
                binding.tvDob.text = "Dob: $dob"
                val gender = if (it.gender == "m") {
                    "Male"
                } else if (it.gender == "f") {
                    "Female"
                } else {
                    "NA"
                }
                binding.tvGender.text = "Gender: $gender"
                binding.tvPlayerPosition.text = "Role: ${it.position?.name}"
                binding.tvPlayerBattingStyle.text = "Batting Style: ${it.battingstyle}"
                binding.tvPlayerBowlingStyle.text = "Bowling Style: ${it.bowlingstyle}"

                it.country_id?.let { country_id ->
                    viewModel.readCountryById(country_id).observe(viewLifecycleOwner) { country ->
                        binding.tvCountry.text = "Country: $country"
                    }
                }
                binding.tvCurrentTeam.text =
                    if (it.teams != null && it.teams.isNotEmpty()) {
                        Log.d(TAG, "onViewCreated: teams: ${it.teams.size}")
                        "Team: " + it.teams[0].name
                    } else {
                        "Team: " + "__"
                    }
                val career = it.career
                /*    val careerList = listOf<Career>()
                    var t20_batting: Batting? = null
                    var t20_bowling: Bowling? = null
                    var t20I_batting: Batting? = null
                    var t20I_bowling: Bowling? = null
                    var odi_batting: Batting? = null
                    var odi_bowling: Bowling? = null
                    var test_batting: Batting? = null
                    var test_bowling: Bowling? = null
    */
                if (career != null && career.isNotEmpty()) {
                    var matches = 0
                    var runs = 0
                    var best = 0
                    var stickRate = 0.0
                    var fifties = 0
                    var hundreds = 0
                    career.forEach { it1 ->
                        if (it1.type == "T20") {
                            if (it1.batting?.matches != null) {
                                matches += it1.batting.matches!!
                            }
                            if (it1.batting?.runs_scored != null) {
                                runs += it1.batting.runs_scored
                            }
                            if (it1.batting?.highest_inning_score != null) {
                                if (it1.batting.highest_inning_score > best) {
                                    best = it1.batting.highest_inning_score
                                }
                            }

                            if (it1.batting?.strike_rate != null) {
                                stickRate += it1.batting.strike_rate
                            }

                            if (it1.batting?.fifties != null) {
                                fifties += it1.batting.fifties
                            }

                            if (it1.batting?.hundreds != null) {
                                hundreds += it1.batting.hundreds
                            }

                        }
                    }
                    val tempSR = floor((stickRate / career.size)*100)
                    stickRate = tempSR/100.00
                    binding.tvT20MatchCount.text = matches.toString()
                    binding.tvT20RunCount.text = runs.toString()
                    binding.tvT20Best.text = best.toString()
                    binding.tvT20Sr.text = stickRate.toString()
                    binding.tvT20100s.text = hundreds.toString()
                    binding.tvT2050s.text = fifties.toString()
                }

            }
        }
    }

}