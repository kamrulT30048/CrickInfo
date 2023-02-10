package com.kamrulhasan.crickinfo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.model.fixture.Run
import com.kamrulhasan.crickinfo.network.CricketApi
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.MyApplication

private const val TAG = "FixtureAdapter"

class FixtureAdapter(
    private val fixtureList: List<FixturesData>,
    private val viewModel: CrickInfoViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<FixtureAdapter.FixturesHolder>() {
    class FixturesHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView) {
        val team1: TextView = binding.findViewById(R.id.tv_team1)
        val team2: TextView = binding.findViewById(R.id.tv_team2)
        val date: TextView = binding.findViewById(R.id.tv_date)
        val matchType: TextView = binding.findViewById(R.id.tv_format)
        val notes: TextView = binding.findViewById(R.id.tv_match_notes)
        val scoreTeam1: TextView = binding.findViewById(R.id.tv_score_team1)
        val scoreTeam2: TextView = binding.findViewById(R.id.tv_score_team2)
        val teamIcon1: ImageView = binding.findViewById(R.id.iv_team1)
        val teamIcon2: ImageView = binding.findViewById(R.id.iv_team2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.match_item, parent, false)
        return FixturesHolder(view)
    }

    override fun onBindViewHolder(holder: FixturesHolder, position: Int) {
        val fixturesItem = fixtureList[position]

        holder.date.text = fixturesItem.starting_at?.let { DateConverter.zoneToDate(it) }
        holder.matchType.text = fixturesItem.round

        if (fixturesItem.status == "Finished") {
            holder.notes.text = fixturesItem.note
        }

        var runTeam1: Run?
        var runTeam2: Run?
        val runs = fixturesItem.runs

        if (runs != null && runs.isNotEmpty()) {

            if ((fixturesItem.toss_won_team_id == fixturesItem.localteam_id && fixturesItem.elected == "batting")
                || (fixturesItem.toss_won_team_id == fixturesItem.visitorteam_id && fixturesItem.elected == "bowling")
            ) {

                runTeam1 = runs[0]
                holder.scoreTeam1.text = runTeam1.score.toString()

                if (runs.size == 2) {
                    runTeam2 = runs[1]
                    holder.scoreTeam2.text = runTeam2.score.toString()
                } else {
                    holder.scoreTeam2.text = "-/-"
                }
            } else {
                runTeam2 = runs[0]
                holder.scoreTeam2.text = runTeam2.score.toString()

                if (runs.size == 2) {
                    runTeam1 = runs[1]
                    holder.scoreTeam1.text = runTeam1.score.toString()
                } else {
                    holder.scoreTeam1.text = "-/-"
                }
            }
        }

        fixturesItem.localteam_id?.let {
            val teamLocal = viewModel.localTeam

            Log.d(TAG, "onBindViewHolder: $teamLocal")
            
            if (teamLocal != null) {
                holder.team1.text = teamLocal.code.toString()
                //loading image with glide
                Glide
                    .with(holder.itemView.context)
                    .load(teamLocal.image_path)
                    .centerCrop()
                    .placeholder(R.drawable.icon_match)
                    .into(holder.teamIcon1)

            } else {
                holder.team1.text = "LT${it}"
            }
        }




//        fixturesItem.visitorteam_id?.let {
//            viewModel.getVisitorTeamById(it)
//        }

        if (viewModel.visitorTeam != null) {
            holder.team2.text = viewModel.visitorTeam!!.code.toString()
            //loading news image with glide
            Glide
                .with(holder.itemView.context)
                .load(viewModel.visitorTeam!!.image_path)
                .centerCrop()
                .placeholder(R.drawable.icon_match)
                .into(holder.teamIcon2)

        } else {
            holder.team2.text = "VT"
        }

//        fixturesItem.localteam_id?.let {
//            val team =  viewModel.getTeamById(it)
//            Log.d(TAG, "onBindViewHolder: ${team?.code}")
//        }

//        viewModel.teamsDataById.observe(viewLifecycleOwner) {
//            if (it != null) {
//                val it1 = it
//                Log.d(TAG, "onBindViewHolder: ${it1.code}")
//                holder.team1.text = it1.code //code.toString()
//                //loading image with glide
//                Glide
//                    .with(holder.itemView.context)
//                    .load(it1.image_path)
//                    .centerCrop()
//                    .placeholder(R.drawable.icon_match)
//                    .into(holder.teamIcon1)
//            } else {
//                holder.team1.text = "LT"
//            }
//        }


//        Log.d(TAG, "onBindViewHolder: api1: $teamInfo1")
//        Log.d(TAG, "onBindViewHolder: api2: $teamInfo2")
//        if(teamInfo1 != null){
//            holder.team1.text = teamInfo1.code.toString()
//            //loading news image with glide
//            Glide
//                .with(holder.itemView.context)
//                .load(teamInfo1.image_path)
//                .centerCrop()
//                .placeholder(R.drawable.icon_match)
//                .into(holder.teamIcon1)
//        }else{
//            holder.team1.text = "LT"
//        }

//        if(teamInfo2 != null){
//            holder.team2.text = teamInfo2.code.toString()
//            Glide
//                .with(holder.itemView.context)
//                .load(teamInfo2.image_path)
//                .centerCrop()
//                .placeholder(R.drawable.icon_match)
//                .into(holder.teamIcon2)
//        }else{
//            holder.team2.text = "VT"
//        }
    }

    override fun getItemCount(): Int {
        return fixtureList.size
    }
}