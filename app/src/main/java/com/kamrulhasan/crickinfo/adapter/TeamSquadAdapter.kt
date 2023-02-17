package com.kamrulhasan.crickinfo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.squad.Squad
import com.kamrulhasan.topnews.utils.MyApplication

private const val TAG = "TeamSquadAdapter"

class TeamSquadAdapter(
    private val playerData: List<Squad>
) : RecyclerView.Adapter<TeamSquadAdapter.SquadHolder>(){

    class SquadHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView){
        val playerImage : ImageView = binding.findViewById(R.id.iv_player)
        val playerName: TextView = binding.findViewById(R.id.tv_player_name)
        val playerType : TextView = binding.findViewById(R.id.tv_player_position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.squad_item, parent, false)
        return SquadHolder(view)
    }

    override fun onBindViewHolder(holder: SquadHolder, position: Int) {
        val playerItem = playerData[position]

        Log.d(TAG, "onBindViewHolder: ${playerItem.firstname}")

        holder.playerName.text = playerItem.fullname
        holder.playerType.text = playerItem.position?.name
        Glide
            .with(holder.itemView.context)
            .load(playerItem.image_path)
            .centerCrop()
            .placeholder(R.drawable.icon_match)
            .into(holder.playerImage)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${playerData.size}")
        return playerData.size
    }
}