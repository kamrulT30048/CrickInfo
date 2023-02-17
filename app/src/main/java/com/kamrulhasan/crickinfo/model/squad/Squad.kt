package com.kamrulhasan.crickinfo.model.squad

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.RawValue

//@Entity(tableName = "player_table")
data class Squad(
    val battingstyle: String?,
    val bowlingstyle: String?,
    val country_id: Int?,
    val dateofbirth: String?,
    val firstname: String?,
    val fullname: String?,
    val gender: String?,
//    @PrimaryKey
    val id: Int,
    val image_path: String?,
    val lastname: String?,
//    @Ignore
//    val position: @RawValue Position?,
    val position: Position?,
    val resource: String?,
//    val squad: @RawValue SquadX?,
    val squad: SquadX?,
    val updated_at: String?
)