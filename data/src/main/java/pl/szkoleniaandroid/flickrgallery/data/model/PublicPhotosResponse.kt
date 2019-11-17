package pl.szkoleniaandroid.flickrgallery.data.model

data class PublicPhotosResponse(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<PublicPhoto>
)