package com.kamrulhasan.crickinfo.model.upcomingmatch

data class UpcomingMatch(
    val `data`: List<UpcomingMatchData>?,
    val links: Links?,
    val meta: Meta?
)