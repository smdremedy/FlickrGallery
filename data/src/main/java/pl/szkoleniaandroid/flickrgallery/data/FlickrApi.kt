package pl.szkoleniaandroid.flickrgallery.data

import pl.szkoleniaandroid.flickrgallery.data.model.PublicPhotosResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun getPublicPhotosByTags(@Query("tags") tags: String): PublicPhotosResponse
}
