package pl.szkoleniaandroid.flickrgallery.data.model

import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime

data class PublicPhoto(
    val title: String,
    val link: String,
    val media: Media,
    val date_taken: OffsetDateTime,
    val description: String, // in html
    val published: LocalDateTime,
    val author: String,
    val author_id: String,
    val tags: String
)