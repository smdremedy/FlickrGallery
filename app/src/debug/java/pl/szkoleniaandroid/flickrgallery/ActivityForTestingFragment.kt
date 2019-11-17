package pl.szkoleniaandroid.flickrgallery

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

/**
 * Used for testing fragments inside a fake activity.
 */
class ActivityForTestingFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun initWithNavigation(
        @IdRes destinationId: Int,
        @NavigationRes navigationId: Int
    ) {
        with(findNavController(R.id.nav_host)) {
            val graphToSet = navInflater.inflate(navigationId)
            graphToSet.startDestination = destinationId
            setGraph(graphToSet, intent.extras)
        }
    }
}
