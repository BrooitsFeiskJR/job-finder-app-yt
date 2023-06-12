package dev.tontech.job_finder_yt.data.model

data class Job (
    var id: String = "",
    var company: String = "",
    var title: String = "",
    var iconUrl: String = "",
    var location: String = "",
    var isFavorite: Boolean = false,
    var filters: List<String> = listOf("Remoto", "Tempo Integral")
)