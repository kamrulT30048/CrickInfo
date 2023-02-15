package com.kamrulhasan.crickinfo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.kamrulhasan.crickinfo.R
import com.kamrulhasan.crickinfo.model.team.TeamsData
import com.kamrulhasan.topnews.utils.MyApplication

class PlayerAdapter(
    private val playerData: List<TeamsData>,
    private val viewModel: ViewModel
) :RecyclerView.Adapter<PlayerAdapter.PlayerHolder>(){

    class PlayerHolder(binding: View) : RecyclerView.ViewHolder(binding.rootView){
        val playerImage : ImageView = binding.findViewById(R.id.iv_player)
        val playerName: TextView = binding.findViewById(R.id.tv_player_name)
        val playerCountry : TextView = binding.findViewById(R.id.tv_player_county)
        val playerType : TextView = binding.findViewById(R.id.tv_player_type)
        val playerTeam : TextView = binding.findViewById(R.id.tv_player_team)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerHolder {
        val view = LayoutInflater.from(MyApplication.appContext)
            .inflate(R.layout.player_item, parent, false)
        return PlayerHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
        val playerItem = playerData[position]

        holder.playerImage.setImageResource(R.drawable.icon_image_24)
        holder.playerName.text = playerItem.name
        holder.playerType.text = playerItem.code
        holder.playerTeam.text = playerItem.code
    }

    override fun getItemCount(): Int {
        return playerData.size
    }
}