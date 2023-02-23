package com.kamrulhasan.crickinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.match.Bowling
import com.kamrulhasan.crickinfo.viewmodel.CrickInfoViewModel
import com.kamrulhasan.topnews.utils.MyApplication

class BowlingAdapter(
    private val bowling: List<Bowling>,
    private val viewModel: CrickInfoViewModel,
    private val viewLifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<BowlingAdapter.BattingHolder>() {

    class BattingHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView) {

        val playerName: TextView = binding.findViewById(R.id.tv_player_name)
        val runs: TextView = binding.findViewById(R.id.tv_runs)
        val overs: TextView = binding.findViewById(R.id.tv_over)
        val wickets: TextView = binding.findViewById(R.id.tv_wicket)
        val economyRate: TextView = binding.findViewById(R.id.tv_economy_rate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BattingHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.bowling_item, parent, false)
        return BattingHolder(view)
    }

    override fun onBindViewHolder(holder: BattingHolder, position: Int) {
        val battingItem = bowling[position]

        battingItem.player_id.let { it1 ->
            viewModel.getPlayerNameById(it1)
            viewModel.readPlayerNameById(it1).observe(viewLifecycleOwner) {
                if (battingItem.active == true) {
                    holder.playerName.text = "$it*"
                } else {
                    holder.playerName.text = it
                }
            }
        }

        holder.runs.text = battingItem.runs.toString()
        holder.overs.text = battingItem.overs.toString()
        holder.wickets.text = battingItem.wickets.toString()
        holder.economyRate.text = battingItem.rate.toString()

    }

    override fun getItemCount(): Int {
        return bowling.size
    }
}
