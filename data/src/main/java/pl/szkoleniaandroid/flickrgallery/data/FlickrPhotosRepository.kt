package pl.szkoleniaandroid.flickrgallery.data

import pl.szkoleniaandroid.flickrgallery.data.model.PublicPhoto
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo
import pl.szkoleniaandroid.flickrgallery.domain.repositories.PhotosRepository

class FlickrPhotosRepository(
    private val flickrApi: FlickrApi
) : PhotosRepository {
    override suspend fun getPhotosByTags(tags: String): List<Photo> {
        return flickrApi.getPublicPhotosByTags(tags).items.map { it.toDomainModel() }
    }
}

fun PublicPhoto.toDomainModel() = Photo(
    title,
    tags,
    media.m,
    link,
    description,
    published,
    date_taken
)