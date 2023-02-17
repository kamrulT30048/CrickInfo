package com.kamrulhasan.crickinfo.model.season

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "season_table")
data class SeasonsData(
    val code: String?,
    @PrimaryKey
    val id: Int,
    val league_id: Int?,
    val name: String?,
    val resource: String?,
    val updated_at: String?
)