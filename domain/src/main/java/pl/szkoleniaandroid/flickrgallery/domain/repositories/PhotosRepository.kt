package pl.szkoleniaandroid.flickrgallery.domain.repositories

import pl.szkoleniaandroid.flickrgallery.domain.model.Photo

interface PhotosRepository {

    suspend fun getPhotosByTags(tags: String): List<Photo>
}