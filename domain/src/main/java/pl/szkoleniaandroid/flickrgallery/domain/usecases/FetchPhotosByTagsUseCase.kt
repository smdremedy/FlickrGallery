package pl.szkoleniaandroid.flickrgallery.domain.usecases

import pl.szkoleniaandroid.flickrgallery.domain.model.Photo
import pl.szkoleniaandroid.flickrgallery.domain.repositories.PhotosRepository

class FetchPhotosByTagsUseCase(
    private val photosRepository: PhotosRepository
) {

    suspend operator fun invoke(tags: String): List<Photo> {
        return photosRepository.getPhotosByTags(tags)
    }
}
