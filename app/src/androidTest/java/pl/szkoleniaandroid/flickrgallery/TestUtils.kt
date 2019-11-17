package pl.szkoleniaandroid.flickrgallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnitRunner
import com.facebook.testing.screenshot.Screenshot
import com.facebook.testing.screenshot.ScreenshotRunner
import org.hamcrest.CoreMatchers
import org.koin.test.KoinTest
import java.util.concurrent.TimeUnit

fun fragmentTestRuleWithMocks(stubbing: () -> Unit = {}) =
    object : ActivityTestRule<ActivityForTestingFragment>(
        ActivityForTestingFragment::class.java, true, false
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            stubbing()
        }
    }

abstract class Robot<T : Activity>(protected val rule: ActivityTestRule<T>) {

    fun startMainActivity() {
        rule.launchActivity(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                MainActivity::class.java
            )
        )
    }

    fun wait(seconds: Int) = TimeUnit.SECONDS.sleep(seconds.toLong())

    fun waitMs(milliseconds: Int) = TimeUnit.MILLISECONDS.sleep(milliseconds.toLong())

    fun checkWasToastShown(text: String) {
        Espresso.onView(ViewMatchers.withText(text))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(rule.activity.window.decorView)))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun hideKeyboard() {
        Espresso.closeSoftKeyboard()
    }
}

abstract class FragmentRobot(rule: ActivityTestRule<ActivityForTestingFragment>) :
    Robot<ActivityForTestingFragment>(rule) {

    fun startFragmentWithNav(
        @IdRes destinationId: Int,
        @NavigationRes navigationId: Int = R.navigation.flickr_gallery_nav_graph
    ) {
        rule.launchActivity(
            Intent(
                InstrumentationRegistry.getInstrumentation().targetContext,
                ActivityForTestingFragment::class.java
            )
        )
        rule.activity.initWithNavigation(destinationId, navigationId)
    }

    fun capture(tag: String, description: String = "") {
        wait(1)
        Screenshot.snapActivity(rule.activity)
            .setName(tag)
            .setDescription(description)
            .record()
    }

    fun pressBack() = Espresso.pressBackUnconditionally()
}

abstract class RobotTest<A : Activity, R : Robot<A>> : KoinTest {

    protected fun withRobot(steps: R.() -> Unit) {
        createRobot().apply(steps)
    }

    abstract fun createRobot(): R
}

abstract class FragmentTest<R : FragmentRobot> : RobotTest<ActivityForTestingFragment, R>()

fun Int.click() {
    Espresso.onView(ViewMatchers.withId(this)).perform(ViewActions.click())
}

class ScreenshotsRunner : AndroidJUnitRunner() {

    override fun onCreate(args: Bundle) {
        super.onCreate(args)
        ScreenshotRunner.onCreate(this, args)
    }

    override fun finish(resultCode: Int, results: Bundle) {
        ScreenshotRunner.onDestroy()
        super.finish(resultCode, results)
    }
}
