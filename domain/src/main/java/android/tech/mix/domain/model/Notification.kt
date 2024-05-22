package android.tech.mix.domain.model

data class Notification(
    val sender: String,
    val startTime: Long,
    val endTime: Long,
    val eventType: String,
    val details: String? = null,
    val tagsList: List<String>? = null
)