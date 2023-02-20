package com.kamrulhasan.crickinfo.ui.fragment

import android.graphics.Color
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
import com.kamrulhasan.crickinfo.model.custom.CustomBatting
import com.kamrulhasan.crickinfo.model.custom.CustomBowling
import com.kamrulhasan.crickinfo.model.player.Career
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.PLAYER_ID
import java.util.Calendar
import java.util.Date
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                Glide.with(requireContext()).load(it.image_path).fitCenter()
                    .placeholder(R.drawable.icon_person).into(binding.ivPlayer)

                binding.tvPlayerName.text = it.fullname
                binding.tvFirstName.text = it.firstname
                binding.tvLastName.text = it.lastname

                it.dateofbirth?.let { it1 ->
                    val dob = DateConverter.zoneToDate(it1)
                    val dobYear = DateConverter.dateToYear(dob)
                    val ageYear = Calendar.getInstance().get(Calendar.YEAR) - dobYear

//                    val date = DateConverter.stringToDate(it1)
//                    val dobMili = DateConverter.dateToLong(date)
//                    val todayMili = Calendar.getInstance().time.time
//                    val ageSec =  (todayMili - dobMili)/1000  // get age in sec
//                    val ageYear = ((ageSec/(60*60))/24)/365   // get age year

                    val age = "$ageYear Years ($dob)"

                    binding.tvDob.text = age
                }

                val gender = if (it.gender == "m") {
                    "Male"
                } else if (it.gender == "f") {
                    "Female"
                } else {
                    "NA"
                }
                binding.tvGender.text = gender
                binding.tvPlayerPosition.text = it.position?.name
                binding.tvPlayerBattingStyle.text = it.battingstyle
                binding.tvPlayerBowlingStyle.text = it.bowlingstyle

                it.country_id?.let { country_id ->
                    viewModel.readCountryById(country_id).observe(viewLifecycleOwner) { country ->
                        binding.tvCountry.text = country
                    }
                }
                binding.tvCurrentTeam.text = if (it.teams != null && it.teams.isNotEmpty()) {
                    Log.d(TAG, "onViewCreated: teams: ${it.teams.size}")
                    it.teams[0].name
                } else {
                    "__"
                }
                val career = it.career

                if (career != null && career.isNotEmpty()) {

                    /////////// Batting Career \\\\\\\\\\\\\

                    // by default show batting career
                    setBatingStatistics(career)

                    binding.tvBatting.setOnClickListener {
                        binding.tvBatting.setBackgroundColor(resources.getColor(R.color.olive_light_00))
                        binding.tvBowling.setBackgroundColor(resources.getColor(R.color.gray_light_0))

                        binding.layoutBowling.visibility = View.GONE
                        binding.layoutBatting.visibility = View.VISIBLE

                        setBatingStatistics(career)
                    }

                    /////////// Bowling Career \\\\\\\\\\\\\

                    binding.tvBowling.setOnClickListener {
                        binding.tvBowling.setBackgroundColor(resources.getColor(R.color.olive_light_00))
                        binding.tvBatting.setBackgroundColor(resources.getColor(R.color.gray_light_0))

                        binding.layoutBowling.visibility = View.VISIBLE
                        binding.layoutBatting.visibility = View.GONE

                        setBowlingStatistics(career)
                    }


                }

            }
        }
    }

    ////////////////////////////
    // Set Batting Statistic  //
    ////////////////////////////

    private fun setBatingStatistics(career: List<Career>) {
        // T20
        var batting = sumOfBatting("T20", career)

        binding.tvT20Match.text = batting.matches.toString()
        binding.tvT20Run.text = batting.runs.toString()
        binding.tvT20BestRun.text = batting.best.toString()
        binding.tvT20StickRate.text = batting.stickRate.toString()
        binding.tvT20Hundreds.text = batting.hundreds.toString()
        binding.tvT20Fifties.text = batting.fifties.toString()

        // T20I
        batting = sumOfBatting("T20I", career)

        binding.tvT20iMatch.text = batting.matches.toString()
        binding.tvT20iRun.text = batting.runs.toString()
        binding.tvT20iBestRun.text = batting.best.toString()
        binding.tvT20iStickRate.text = batting.stickRate.toString()
        binding.tvT20iHundreds.text = batting.hundreds.toString()
        binding.tvT20iFifties.text = batting.fifties.toString()

        // ODI
        batting = sumOfBatting("ODI", career)

        binding.tvOdiMatch.text = batting.matches.toString()
        binding.tvOdiRun.text = batting.runs.toString()
        binding.tvOdiBestRun.text = batting.best.toString()
        binding.tvOdiStickRate.text = batting.stickRate.toString()
        binding.tvOdiHundreds.text = batting.hundreds.toString()
        binding.tvOdiFifties.text = batting.fifties.toString()

        // Test
        batting = sumOfBatting("Test/5day", career)

        binding.tvTestMatch.text = batting.matches.toString()
        binding.tvTestRun.text = batting.runs.toString()
        binding.tvTestBestRun.text = batting.best.toString()
        binding.tvTestStickRate.text = batting.stickRate.toString()
        binding.tvTestHundreds.text = batting.hundreds.toString()
        binding.tvTestFifties.text = batting.fifties.toString()

    }

    ////////////////////////////
    // Set Bowling Statistic  //
    ////////////////////////////

    private fun setBowlingStatistics(career: List<Career>) {

        var bowling = sumOfBowling("T20", career)

        binding.tvBowlingT20Match.text = bowling.matches.toString()
        binding.tvBowlingT20Run.text = bowling.runs.toString()
        binding.tvBowlingT20Wicket.text = bowling.wicket.toString()
        binding.tvBowlingT20Economy.text = bowling.economy.toString()
        binding.tvBowlingT20Avg.text = bowling.avg.toString()
        binding.tvBowlingT20StickRate.text = bowling.stickRate.toString()
        binding.tvBowlingT20FiveWicket.text = bowling.fiveWicket.toString()

        bowling = sumOfBowling("T20I", career)

        binding.tvBowlingT20iMatch.text = bowling.matches.toString()
        binding.tvBowlingT20iRun.text = bowling.runs.toString()
        binding.tvBowlingT20iWicket.text = bowling.wicket.toString()
        binding.tvBowlingT20iEconomy.text = bowling.economy.toString()
        binding.tvBowlingT20iAvg.text = bowling.avg.toString()
        binding.tvBowlingT20iStickRate.text = bowling.stickRate.toString()
        binding.tvBowlingT20iFiveWicket.text = bowling.fiveWicket.toString()

        bowling = sumOfBowling("ODI", career)

        binding.tvBowlingOdiMatch.text = bowling.matches.toString()
        binding.tvBowlingOdiRun.text = bowling.runs.toString()
        binding.tvBowlingOdiWicket.text = bowling.wicket.toString()
        binding.tvBowlingOdiEconomy.text = bowling.economy.toString()
        binding.tvBowlingOdiAvg.text = bowling.avg.toString()
        binding.tvBowlingOdiStickRate.text = bowling.stickRate.toString()
        binding.tvBowlingOdiFiveWicket.text = bowling.fiveWicket.toString()

        bowling = sumOfBowling("Test/5day", career)

        binding.tvBowlingTestMatch.text = bowling.matches.toString()
        binding.tvBowlingTestRun.text = bowling.runs.toString()
        binding.tvBowlingTestWicket.text = bowling.wicket.toString()
        binding.tvBowlingTestEconomy.text = bowling.economy.toString()
        binding.tvBowlingTestAvg.text = bowling.avg.toString()
        binding.tvBowlingTestStickRate.text = bowling.stickRate.toString()
        binding.tvBowlingTestFiveWicket.text = bowling.fiveWicket.toString()


    }

    /////////////////////////////
    // Batting statistic's sum  //
    /////////////////////////////
    private fun sumOfBatting(matchType: String, career: List<Career>): CustomBatting {

        var matches = 0
        var runs = 0
        var best = 0
        var stickRate = 0.0
        var fifties = 0
        var hundreds = 0
        var seasonCount = 0

        career.forEach { it1 ->
            if (it1.type == matchType) {

                seasonCount += 1

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

        val tempSR = floor((stickRate / seasonCount) * 100)
        stickRate = if (stickRate > 0.0) {
            tempSR / 100.00
        } else {
            0.0
        }
        return CustomBatting(
            matches, runs, best, stickRate, fifties, hundreds
        )
    }

    private fun sumOfBowling(matchType: String, career: List<Career>): CustomBowling {
        var matches = 0
        var runs = 0
        var wicket = 0
        var economy = 0.0
        var average = 0.0
        var stickRate = 0.0
        var fiveWickets = 0
        var seasonCount = 0

        career.forEach { it1 ->
            if (it1.type == matchType) {

                seasonCount += 1

                if (it1.bowling?.matches != null) {
                    matches += it1.bowling.matches!!
                }

                if (it1.bowling?.runs != null) {
                    runs += it1.bowling.runs
                }

                if (it1.bowling?.wickets != null) {
                    wicket += it1.bowling.wickets
                }

                if (it1.bowling?.econ_rate != null) {
                    economy += it1.bowling.econ_rate
                }

                if (it1.bowling?.average != null) {
                    average += it1.bowling.average
                }

                if (it1.bowling?.strike_rate != null) {
                    stickRate += it1.bowling.strike_rate
                }

                if (it1.bowling?.five_wickets != null) {
                    fiveWickets += it1.bowling.five_wickets
                }

            }
        }

        economy = if (economy > 0.0) {
            floor((economy / seasonCount) * 100) / 100.00
        } else {
            0.0
        }

        average = if (average > 0.0) {
            floor((average / seasonCount) * 100) / 100.00
        } else {
            0.0
        }

        stickRate = if (stickRate > 0.0) {
            floor((stickRate / seasonCount) * 100) / 100.00
        } else {
            0.0
        }

        return CustomBowling(
            matches, runs, wicket, economy, average, stickRate, fiveWickets
        )

    }
}