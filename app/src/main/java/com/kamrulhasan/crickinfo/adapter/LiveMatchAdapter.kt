package com.kamrulhasan.crickinfo.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.match.MatchData
import com.kamrulhasan.crickinfo.ui.fragment.FixturesFragmentDirections
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.MATCH_ID
import com.kamrulhasan.topnews.utils.MyApplication
import com.kamrulhasan.topnews.utils.URL_KEY
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "LiveAdapter"

class LiveMatchAdapter(
    private val fixtureList: List<MatchData>,
    private val viewModel: CrickInfoViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<LiveMatchAdapter.FixturesHolder>() {

    class FixturesHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView) {
        val team1: TextView = binding.findViewById(R.id.tv_team1)
        val team2: TextView = binding.findViewById(R.id.tv_team2)
        val date: TextView = binding.findViewById(R.id.tv_date)
        val matchType: TextView = binding.findViewById(R.id.tv_format)
        val notes: TextView = binding.findViewById(R.id.tv_match_notes)
        val status: TextView = binding.findViewById(R.id.tv_match_status)
        val scoreTeam1: TextView = binding.findViewById(R.id.tv_score_team1)
        val scoreTeam2: TextView = binding.findViewById(R.id.tv_score_team2)
        val wicketTeam1: TextView = binding.findViewById(R.id.tv_wicket_team1)
        val wicketTeam2: TextView = binding.findViewById(R.id.tv_wicket_team2)
        val teamIcon1: ImageView = binding.findViewById(R.id.iv_team1)
        val teamIcon2: ImageView = binding.findViewById(R.id.iv_team2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.match_item, parent, false)
        return FixturesHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FixturesHolder, position: Int) {
        val fixturesItem = fixtureList[position]

        holder.date.text = fixturesItem.starting_at?.let { DateConverter.zoneToDate(it) }
        holder.matchType.text = fixturesItem.round

        // run
     /*   fixturesItem.localteam_id?.let {
            viewModel.readScoreById(it, fixturesItem.id)
                .observe(viewLifecycleOwner) {
                    holder.scoreTeam1.text = if (it != null) {
                        it.toString() + "/"
                    } else {
                        "-/"
                    }
                }
        }*/

/*
        viewModel.readScoreById(fixturesItem.visitorteam_id, fixturesItem.id)
            .observe(viewLifecycleOwner) {
                holder.scoreTeam2.text = if (it != null) {
                    it.toString() + "/"
                } else {
                    "-/"
                }
            }
        // wicket
        viewModel.readWicketById(fixturesItem.localteam_id, fixturesItem.id)
            .observe(viewLifecycleOwner) {
                holder.wicketTeam1.text = if (it != null) {
                    it.toString()
                } else {
                    "-"
                }
            }

        viewModel.readWicketById(fixturesItem.visitorteam_id, fixturesItem.id)
            .observe(viewLifecycleOwner) {
                holder.wicketTeam2.text = if (it != null) {
                    it.toString()
                } else {
                    "-"
                }
            }
*/



        fixturesItem.localteam_id?.let {
            viewModel.readTeamCode(it)
                .observe(viewLifecycleOwner) {
                    holder.team1.text = it
                }
        }

        fixturesItem.localteam_id?.let {
            viewModel.readTeamUrl(it)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(holder.itemView.context)
                        .load(it)
                        .fitCenter()
                        .placeholder(R.drawable.icon_match)
                        .into(holder.teamIcon1)
                }
        }

        fixturesItem.visitorteam_id?.let {
            viewModel.readTeamCode(it)
                .observe(viewLifecycleOwner) {
                    holder.team2.text = it
                }
        }

        fixturesItem.visitorteam_id?.let {
            viewModel.readTeamUrl(it)
                .observe(viewLifecycleOwner) {
                    Glide
                        .with(holder.itemView.context)
                        .load(it)
                        .centerCrop()
                        .placeholder(R.drawable.icon_match)
                        .into(holder.teamIcon2)
                }
        }

    }

    override fun getItemCount(): Int {
        return fixtureList.size
    }
}