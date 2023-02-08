package com.kamrulhasan.crickinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.fixture.FixturesData
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.DateConverter
import com.kamrulhasan.topnews.utils.MyApplication

class FixtureAdapter(
    private val fixtureList: List<FixturesData>,
    private val viewModel: CrickInfoViewModel
) : RecyclerView.Adapter<FixtureAdapter.FixturesHolder>() {
    class FixturesHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView) {
        val team1 : TextView = binding.findViewById(R.id.tv_team1)
        val team2 : TextView = binding.findViewById(R.id.tv_team2)
        val date : TextView = binding.findViewById(R.id.tv_date)
        val matchType : TextView = binding.findViewById(R.id.tv_format)
        val notes : TextView = binding.findViewById(R.id.tv_match_notes)
        val scoreTeam1: TextView = binding.findViewById(R.id.tv_score_team1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixturesHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.match_item, parent, false)
        return FixturesHolder(view)
    }

    override fun onBindViewHolder(holder: FixturesHolder, position: Int) {
        val fixturesItem = fixtureList[position]

        holder.team1.text = fixturesItem.localteam_id.toString()
        holder.team2.text = fixturesItem.visitorteam_id.toString()
        holder.date.text = fixturesItem.starting_at?.let { DateConverter.zoneToDate(it) }
        holder.matchType.text = fixturesItem.type

        if(fixturesItem.status == "Finished"){
            holder.notes.text = fixturesItem.note
        }
//        holder.scoreTeam1.text = fixturesItem.localteam_dl_data?.score.toString() + "/" +
//                fixturesItem.localteam_dl_data?.wickets_out.toString()
    }

    override fun getItemCount(): Int {
        return fixtureList.size
    }
}