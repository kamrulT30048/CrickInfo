package com.kamrulhasan.crickinfo.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.adapter.LineupAdapter
import com.kamrulhasan.crickinfo.databinding.FragmentMatchDetailsBinding
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.lineup.Lineup
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

        match?.let { it ->


            binding.tvDate.text = it.starting_at?.let { DateConverter.zoneToDate(it) }
            binding.tvMatchNotes.text = it.note
            binding.tvStatus.text = "Status: ${it.status}"
            binding.tvSeries.text = "Round: ${it.round}"

            //won team
            it.winner_team_id?.let { it1 ->
                viewModel.readTeamCode(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvMatchWonTeam.text = "Winner: $it"
                    }
            }

            // MOM
            it.man_of_match_id?.let{ it1 ->
                viewModel.getPlayerNameById(it1)
                viewModel.playerName.observe(viewLifecycleOwner){
                    Log.d(TAG, "onViewCreated: $it")
                    binding.tvManOfTheMatch.text = "MOM: $it"
                }
            }

            //toss won team
            it.toss_won_team_id?.let { it1 ->
                viewModel.readTeamCode(it1)
                    .observe(viewLifecycleOwner) {
                        binding.tvTossWonTeam.text = "Toss: $it"
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

            // read team code
            viewModel.readTeamCode(it.localteam_id)
                .observe(viewLifecycleOwner) {
                    binding.tvTeam1.text = it
                    binding.tvNameTeam1.text = it
                }

            viewModel.readTeamCode(it.visitorteam_id)
                .observe(viewLifecycleOwner) {
                    binding.tvTeam2.text = it
                    binding.tvNameTeam2.text = it
                }

            // read team image url
            viewModel.readTeamUrl(it.localteam_id)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(requireContext())
                        .load(it)
                        .centerCrop()
                        .placeholder(R.drawable.icon_match)
                        .into(binding.ivTeam1)
                }

            viewModel.readTeamUrl(it.visitorteam_id)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(requireContext())
                        .load(it)
                        .centerCrop()
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

            //// Lineup calling
            viewModel.getLineup(it.id)

            val localTeam = it.localteam_id
            val visitorTeam = it.visitorteam_id

            val lineupLocal = mutableListOf<Lineup>()
            val lineupVisitor = mutableListOf<Lineup>()

            viewModel.lineup.observe(viewLifecycleOwner){ it1 ->

                lineupLocal.clear()
                lineupVisitor.clear()

                Log.d(TAG, "onViewCreated: lineup: ${it1?.size}")

                it1?.forEach {
                    if(it.lineup?.team_id == localTeam){
                        lineupLocal.add(it)
                    }else{
                        lineupVisitor.add(it)
                    }
                }
                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupLocal)
            }
//            viewModel.getSquadByTeamId(it.localteam_id)
            binding.tvNameTeam1.setBackgroundColor(resources.getColor( R.color.olive_light_00))

            /*var list = listOf<Squad>()

            viewModel.playerList.observe(viewLifecycleOwner){

                it?.let{

                    list = it
                    binding.recyclerViewTeamSquad.adapter = TeamSquadAdapter(list)

                }
            }*/

            val localId = it.localteam_id
            binding.tvNameTeam1.setOnClickListener {
//                viewModel.getSquadByTeamId(localId)

                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupLocal)

                binding.tvNameTeam1.setBackgroundColor(resources.getColor( R.color.olive_light_00))
                binding.tvNameTeam2.setBackgroundColor(resources.getColor( R.color.gray_light_0))
            }

            val visitorId = it.visitorteam_id
            binding.tvNameTeam2.setOnClickListener {
//                viewModel.getSquadByTeamId(visitorId)

                binding.recyclerViewTeamSquad.adapter = LineupAdapter(lineupVisitor)

                binding.tvNameTeam1.setBackgroundColor(resources.getColor( R.color.gray_light_0))
                binding.tvNameTeam2.setBackgroundColor(resources.getColor( R.color.olive_light_00))
            }
        }



    }
}