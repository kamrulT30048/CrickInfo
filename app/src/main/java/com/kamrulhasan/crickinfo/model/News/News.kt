package com.kamrulhasan.crickinfo.model.News

data class News(
    val articles: List<Article>?,
    val status: String?,
    val totalResults: Int?
)