package com.kamrulhasan.crickinfo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.BattingAdapter
import com.kamrulhasan.crickinfo.adapter.BowlingAdapter
import com.kamrulhasan.crickinfo.adapter.LineupAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentMatchDetailsBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.match.Batting
import com.kamrulhasan.crickinfo.model.match.Lineup
import com.kamrulhasan.crickinfo.model.match.Bowling
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.MATCH_ID

private const val TAG = "MatchDetailsFragment"

class MatchDetailsFragment : Fragment() {

    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CrickInfoViewModel

    private var match: FixturesData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            match = it.getParcelable(MATCH_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMatchDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrickInfoViewModel::class.java]

        binding.layoutMatchInfo.visibility = View.VISIBLE
        binding.layoutScorecard.visibility = View.GONE
        binding.layoutTeamSquad.visibility = View.GONE
        binding.layoutMatchUmpire.visibility = View.GONE

        binding.tvMatchInfo.setOnClickListener {
            binding.layoutMatchInfo.visibility = View.VISIBLE
            binding.layoutScorecard.visibility = View.GONE
            binding.layoutTeamSquad.visibility = View.GONE
            binding.layoutMatchUmpire.visibility = View.GONE
        }
        binding.tvTeamSquad.setOnClickListener {
            binding.layoutMatchInfo.visibility = View.GONE
            binding.layoutScorecard.visibility = View.GONE
            binding.layoutTeamSquad.visibility = View.VISIBLE
            binding.layoutMatchUmpire.visibility = View.GONE
        }

        binding.tvTeamScoreCard.setOnClickListener {
            binding.layoutMatchInfo.visibility = View.GONE
            binding.layoutScorecard.visibility = View.VISIBLE
            binding.layoutTeamSquad.visibility = View.GONE
            binding.layoutMatchUmpire.visibility = View.GONE
        }
        binding.tvMatchUmpire.setOnClickListener {
            binding.layoutMatchInfo.visibility = View.GONE
            binding.layoutScorecard.visibility = View.GONE
            binding.layoutTeamSquad.visibility = View.GONE
            binding.layoutMatchUmpire.visibility = View.VISIBLE
        }



        match?.let { it ->

            //binding.tvDate.text = it.starting_at?.let { DateConverter.zoneToDate(it) }
            binding.tvMatchDate.text = it.starting_at?.let { DateConverter.zoneToDate(it) }
            binding.tvMatchTime.text = it.starting_at?.let { DateConverter.zoneToTime(it) }
            // match notes
            binding.tvMatchNotes.text = if (it.note == "") {
                "NA"
            } else {
                it.note
            }

            //Match Status
            binding.tvStatus.text = it.status
            if (it.status == "Finished") {
                binding.tvStatus.setTextColor(resources.getColor(R.color.red))
            } else if (it.status == "NS") {

                binding.tvStatus.text = "Upcoming"
                binding.tvStatus.setTextColor(resources.getColor(R.color.green_dark))

                it.starting_at?.let {

                    val countdownDuration = DateConverter.stringToDateLong(it) - DateConverter.todayToDateLong()

                    Log.d(TAG, "onBindViewHolder: ms: $countdownDuration")
                    val countHour = countdownDuration.div((1000 * 60 * 60))

                    if (countHour in 1..48) {
                        val countdownTimer = object : CountDownTimer(countdownDuration, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                // Update the countdown text view with the remaining time
                                var secondsRemaining = millisUntilFinished / 1000
                                var minute = secondsRemaining / 60
                                secondsRemaining %= 60
                                val hour = minute / 60
                                minute %= 60
                                binding.tvStatus.text = "Upcoming"
                                binding.tvTimeCountDown.text = "$hour:$minute:$secondsRemaining"
                            }

                            override fun onFinish() {
                                // Update the countdown text view with the final message
//                                holder.notes.text = ""
                                binding.tvStatus.text = "Live"
                                binding.tvTimeCountDown.text = "Live"
                                viewModel.readUpcomingMatch()
                            }
                        }
                        countdownTimer.start()
                    }
                }
            }
            //series
            binding.tvSeries.text = it.round
            // match notes
            binding.tvElected.text = if (it.elected == "" || it.elected.isNullOrEmpty()) {
                "NA"
            } else {
                it.elected
            }

            it.venue_id?.let { it1 ->
                //venue
                viewModel.readVenuesNameById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvVenues.text = it
                    }
                //venues city
                viewModel.readVenuesCityById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvVenuesCity.text = it
                    }

            }

            //won team
            it.winner_team_id?.let { it1 ->
                viewModel.readTeamCode(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvMatchWonTeam.text = it
                    }
            }

            // Man of Match
            it.man_of_match_id?.let { it1 ->
                viewModel.getPlayerNameById(it1)
                viewModel.playerName.observe(viewLifecycleOwner) {
                    Log.d(TAG, "onViewCreated: $it")
                    binding.tvManOfTheMatch.text = it
                }
            }

            //toss won team
            it.toss_won_team_id?.let { it1 ->
                viewModel.readTeamCode(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvTossWonTeam.text = it
                    }
            }

            // League
            it.league_id?.let { it1 ->
                viewModel.readLeaguesById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvLeague.text = it
                    }
            }

            /// match Umpire Info
            binding.tvFirstUmpire.text = "1st Umpire: __"
            binding.tvSecondUmpire.text = "2nd Umpire: __"
            binding.tvTvUmpire.text = "3rd Umpire: __"

            it.first_umpire_id?.let { it1 ->
                viewModel.readOfficialsById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvFirstUmpire.text = "1st Umpire: $it"
                    }
            }

            it.second_umpire_id?.let { it1 ->
                viewModel.readOfficialsById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvSecondUmpire.text = "2nd Umpire: $it"
                    }
            }

            it.tv_umpire_id?.let { it1 ->
                viewModel.readOfficialsById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvTvUmpire.text = "TV Umpire: $it"
                    }
            }

            it.referee_id?.let { it1 ->
                viewModel.readOfficialsById(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvReferee.text = "Referee: $it"
                    }
            }

            // read team code
            viewModel.readTeamCode(it.localteam_id)
                .observe(viewLifecycleOwner) {
                    binding.tvTeam1.text = it
                    binding.tvNameTeam1.text = it
                    binding.tvTeam1Card.text = it
                }

            viewModel.readTeamCode(it.visitorteam_id)
                .observe(viewLifecycleOwner) {
                    binding.tvTeam2.text = it
                    binding.tvNameTeam2.text = it
                    binding.tvTeam2Card.text = it
                }

            // read team image url
            viewModel.readTeamUrl(it.localteam_id)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(requireContext())
                        .load(it)
                        .fitCenter()
                        .placeholder(R.drawable.icon_match)
                        .into(binding.ivTeam1)
                }

            viewModel.readTeamUrl(it.visitorteam_id)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(requireContext())
                        .load(it)
                        .fitCenter()
                        .placeholder(R.drawable.icon_match)
                        .into(binding.ivTeam2)
                }

            // run
            viewModel.readScoreById(it.localteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvScoreTeam1.text = if (it != null) {
                        "$it/"
                    } else {
                        "-/"
                    }
                }

            viewModel.readScoreById(it.visitorteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvScoreTeam2.text = if (it != null) {
                        "$it/"
                    } else {
                        "-/"
                    }
                }

            // wicket
            viewModel.readWicketById(it.localteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvWicketTeam1.text = it?.toString() ?: "-"
                }

            viewModel.readWicketById(it.visitorteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvWicketTeam2.text = it?.toString() ?: "-"
                }

            // over
            viewModel.readOverById(it.localteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvOverTeam1.text = if (it != null) {
                        "($it)"
                    } else {
                        "(-)"
                    }
                }

            viewModel.readOverById(it.visitorteam_id, it.id)
                .observe(viewLifecycleOwner) {
                    binding.tvOverTeam2.text = if (it != null) {
                        "($it)"
                    } else {
                        "(-)"
                    }
                }

            //// Match details calling
            viewModel.getMatchDetails(it.id)

            val localTeam = it.localteam_id
            val visitorTeam = it.visitorteam_id

            val lineupLocal = mutableListOf<Lineup>()
            val lineupVisitor = mutableListOf<Lineup>()

            val battingTeam1 = mutableListOf<Batting>()
            val bowlingTeam1 = mutableListOf<Bowling>()
            val battingTeam2 = mutableListOf<Batting>()
            val bowlingTeam2 = mutableListOf<Bowling>()

            var team1Extra = 0
            var team2Extra = 0

            // lineup
            viewModel.matchDetails.observe(viewLifecycleOwner) { it1 ->



                battingTeam1.clear()
                battingTeam2.clear()
                bowlingTeam1.clear()
                bowlingTeam2.clear()

                it1?.batting?.forEach { batting ->
                    if (batting.team_id == localTeam) {
                        battingTeam1.add(batting)
                    } else {
                        battingTeam2.add(batting)
                    }
                }
                binding.recyclerViewBattingScorecard.adapter =
                    BattingAdapter(battingTeam1, viewModel, viewLifecycleOwner)

                /////// calculating extra runs

                it1?.scoreboards?.forEach { score ->
                    if (score.team_id == localTeam && score.type == "extra") {
                        team1Extra =
                            (score.wide ?: 0) + (score.noball_runs ?: 0) + (score.noball_balls ?: 0)
                        +(score.bye ?: 0) + (score.leg_bye ?: 0) + (score.penalty ?: 0)
                    }
                    else if(score.team_id == visitorTeam && score.type == "extra"){
                        team2Extra =
                            (score.wide ?: 0) + (score.noball_runs ?: 0) + (score.noball_balls ?: 0)
                        +(score.bye ?: 0) + (score.leg_bye ?: 0) + (score.penalty ?: 0)
                    }
                }
                binding.tvTeamExtra.text = "Extra: ( $team1Extra )"

                it1?.bowling?.forEach { bowling ->
                    if (bowling.team_id == localTeam) {
                        bowlingTeam1.add(bowling)
                    } else {
                        bowlingTeam2.add(bowling)
                    }
                }
                binding.recyclerViewBowlingScorecard.adapter =
                    BowlingAdapter(bowlingTeam1, viewModel, viewLifecycleOwner)

                lineupLocal.clear()
                lineupVisitor.clear()
                Log.d(TAG, "onViewCreated: lineup: ${it1?.lineup?.size}")

                it1?.lineup?.forEach {
                    if (it.lineup?.team_id == localTeam) {
                        lineupLocal.add(it)
                    } else {
                        lineupVisitor.add(it)
                    }
                }
                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupLocal)
            }

            ///////// scorecard for team1  \\\\\\\\\\\

            binding.tvTeam1Card.setOnClickListener {

                binding.recyclerViewBattingScorecard.adapter =
                    BattingAdapter(battingTeam1, viewModel, viewLifecycleOwner)

                binding.tvTeamExtra.text = "Extra: ( $team1Extra )"

                binding.recyclerViewBowlingScorecard.adapter =
                    BowlingAdapter(bowlingTeam1, viewModel, viewLifecycleOwner)

                // text background change
                binding.tvTeam1Card.setBackgroundResource(R.drawable.selected)
                binding.tvTeam2Card.setBackgroundResource(R.drawable.non_selected)
                // text color change
                binding.tvTeam1Card.setTextColor(resources.getColor(R.color.white))
                binding.tvTeam2Card.setTextColor(resources.getColor(R.color.black))
                // text size change
                binding.tvTeam1Card.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                binding.tvTeam2Card.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }

            ///////// scorecard for team2  \\\\\\\\\\\

            binding.tvTeam2Card.setOnClickListener {

                binding.recyclerViewBattingScorecard.adapter =
                    BattingAdapter(battingTeam2, viewModel, viewLifecycleOwner)

                binding.tvTeamExtra.text = "Extra: ( $team2Extra )"

                binding.recyclerViewBowlingScorecard.adapter =
                    BowlingAdapter(bowlingTeam2, viewModel, viewLifecycleOwner)

                // text background change
                binding.tvTeam2Card.setBackgroundResource(R.drawable.selected)
                binding.tvTeam1Card.setBackgroundResource(R.drawable.non_selected)

                // text color change
                binding.tvTeam2Card.setTextColor(resources.getColor(R.color.white))
                binding.tvTeam1Card.setTextColor(resources.getColor(R.color.black))

                // text size change
                binding.tvTeam2Card.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                binding.tvTeam1Card.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }

//            viewModel.getSquadByTeamId(it.localteam_id)
            /*var list = listOf<Squad>()
            viewModel.playerList.observe(viewLifecycleOwner){
                it?.let{  list = it
                    binding.recyclerViewTeamSquad.adapter = TeamSquadAdapter(list)
                } }*/

            val localId = it.localteam_id
            binding.tvNameTeam1.setOnClickListener {
//                viewModel.getSquadByTeamId(localId)

                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupLocal)

                // text background change
                binding.tvNameTeam1.setBackgroundResource(R.drawable.selected)
                binding.tvNameTeam2.setBackgroundResource(R.drawable.non_selected)

                // text color change
                binding.tvNameTeam1.setTextColor(resources.getColor(R.color.white))
                binding.tvNameTeam2.setTextColor(resources.getColor(R.color.black))

                // text size change
                binding.tvNameTeam1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                binding.tvNameTeam2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }

            val visitorId = it.visitorteam_id

            binding.tvNameTeam2.setOnClickListener {
//                viewModel.getSquadByTeamId(visitorId)

                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupVisitor)

                // text background change
                binding.tvNameTeam2.setBackgroundResource(R.drawable.selected)
                binding.tvNameTeam1.setBackgroundResource(R.drawable.non_selected)

                // text color change
                binding.tvNameTeam2.setTextColor(resources.getColor(R.color.white))
                binding.tvNameTeam1.setTextColor(resources.getColor(R.color.black))

                // text size change
                binding.tvNameTeam1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                binding.tvNameTeam2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            }


        }


    }
}