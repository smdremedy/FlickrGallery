package pl.szkoleniaandroid.flickrgallery.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.FlowPreview
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import pl.szkoleniaandroid.flickrgallery.BuildConfig
import pl.szkoleniaandroid.flickrgallery.data.FlickrApi
import pl.szkoleniaandroid.flickrgallery.data.FlickrPhotosRepository
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo
import pl.szkoleniaandroid.flickrgallery.domain.usecases.FetchPhotosByTagsUseCase
import pl.szkoleniaandroid.flickrgallery.domain.repositories.PhotosRepository
import pl.szkoleniaandroid.flickrgallery.features.details.PhotoDetailsViewModel
import pl.szkoleniaandroid.flickrgallery.features.gallery.GalleryViewModel
import pl.szkoleniaandroid.flickrgallery.utils.IsoLocalDateTimeJsonAdapter
import pl.szkoleniaandroid.flickrgallery.utils.Rfc3339LocalDateTimeJsonAdapter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL_NAMED = "BASE_URL"
private const val BASE_URL = "https://www.flickr.com"

@FlowPreview
val appModule = module {

    // api
    single(named(BASE_URL_NAMED)) {
        BASE_URL
    }

    single<Moshi> {
        Moshi.Builder()
            .add(LocalDateTime::class.java, Rfc3339LocalDateTimeJsonAdapter())
            .add(OffsetDateTime::class.java, IsoLocalDateTimeJsonAdapter())
            .build()
    }

    single<OkHttpClient> {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(httpLoggingInterceptor)
        }
        builder.build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>(named(BASE_URL_NAMED)))
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .build()
    }

    single<FlickrApi> {
        get<Retrofit>().create(FlickrApi::class.java)
    }

    single<PhotosRepository> { FlickrPhotosRepository(flickrApi = get()) }

    factory {
        FetchPhotosByTagsUseCase(
            photosRepository = get()
        )
    }

    viewModel { GalleryViewModel(fetchPhotosByTagsUseCase = get()) }
    viewModel { (photo: Photo) -> PhotoDetailsViewModel(photo) }
}