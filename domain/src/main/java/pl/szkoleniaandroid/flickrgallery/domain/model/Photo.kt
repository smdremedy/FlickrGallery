package pl.szkoleniaandroid.flickrgallery.domain.model

import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import java.io.Serializable

class Photo(
    val name: String,
    val tags: String,
    val imageUrl: String,
    val link: String,
    val htmlDescription: String,
    val datePublished: LocalDateTime,
    val dateTaken: OffsetDateTime
) : Serializable