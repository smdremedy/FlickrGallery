package pl.szkoleniaandroid.flickrgallery

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.szkoleniaandroid.flickrgallery.di.appModule

class FlickrGalleryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FlickrGalleryApp)
            modules(appModule)
        }
    }
}