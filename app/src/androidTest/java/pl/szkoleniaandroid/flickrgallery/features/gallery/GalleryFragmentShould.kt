package pl.szkoleniaandroid.flickrgallery.features.gallery

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.koin.test.mock.declare
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import pl.szkoleniaandroid.flickrgallery.*
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo
import pl.szkoleniaandroid.flickrgallery.domain.repositories.PhotosRepository

class GalleryFragmentShould : FragmentTest<GalleryRobot>() {

    private val mockRepository = mock<PhotosRepository>()

    @get:Rule
    val permissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!!

    @get:Rule
    val rule = fragmentTestRuleWithMocks {
        declare {
            single { mockRepository }
        }
    }

    override fun createRobot() = GalleryRobot(rule, mockRepository)

    @Test
    fun showEmptyMessage() {

        withRobot {
            setPhotosResponse(emptyList<Photo>())
            startFragmentWithNav(R.id.gallery_fragment)
            hasEmptyMessageShown()
        }
    }

    @Test
    fun showItemsSortedByTakenOnDefault() {

        withRobot {
            val firstTaken = Photo(
                name = "test_name",
                tags = "taken first tags with a long text to clip",
                imageUrl = "https://placekitten.com/200/200",
                link = "",
                htmlDescription = "",
                datePublished = LocalDateTime.of(2019, Month.APRIL, 21, 23, 59),
                dateTaken = OffsetDateTime.of(2019, 1, 1, 23, 59, 0, 0, ZoneOffset.UTC)
            )
            val secondTaken = Photo(
                name = "test_name",
                tags = "taken second tags with a long text to clip",
                imageUrl = "https://placekitten.com/200/200",
                link = "",
                htmlDescription = "",
                datePublished = LocalDateTime.of(2019, Month.APRIL, 20, 23, 59),
                dateTaken = OffsetDateTime.of(2019, 1, 2, 23, 59, 0, 0, ZoneOffset.UTC)
            )
            setPhotosResponse(
                listOf(
                    secondTaken, // reversed by default to test sorting
                    firstTaken
                )
            )
            startFragmentWithNav(R.id.gallery_fragment)
            wait(1)
            checkTagsAtPosition(0, firstTaken.tags)
            checkTagsAtPosition(1, secondTaken.tags)
            openSortDialog()
            changeSortOrderTo(SortOrder.DATE_PUBLISHED)
            checkTagsAtPosition(0, secondTaken.tags)
            checkTagsAtPosition(1, firstTaken.tags)
        }
    }
}

class GalleryRobot(
    rule: ActivityTestRule<ActivityForTestingFragment>,
    private val mockPhotosRepository: PhotosRepository
) : FragmentRobot(rule) {

    fun setPhotosResponse(photos: List<Photo>) {
        runBlocking {
            whenever(mockPhotosRepository.getPhotosByTags(any())).thenReturn(photos)
        }
    }

    fun hasEmptyMessageShown() {
        onView(withText(R.string.no_results_try_some_other_tags)).check(matches(isDisplayed()))
    }

    fun checkTagsAtPosition(position: Int, tags: String) {
        onView(
            RecyclerViewMatcher(R.id.gallery_recycler_view).atPositionOnView(
                position,
                R.id.gallery_item_tags
            )
        ).check(matches(withText(tags)))
    }

    fun changeSortOrderTo(sortOrder: SortOrder) {
        val optionText = InstrumentationRegistry.getInstrumentation()
            .targetContext.resources.getTextArray(R.array.sort_types)[sortOrder.ordinal].toString()
        onView(withText(optionText)).perform(click())
    }

    fun openSortDialog() {
        onView(withId(R.id.action_sort)).perform(click())
    }
}