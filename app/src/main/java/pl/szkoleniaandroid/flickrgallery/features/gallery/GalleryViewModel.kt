package pl.szkoleniaandroid.flickrgallery.features.gallery

import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import me.tatarka.bindingcollectionadapter2.ItemBinding
import me.tatarka.bindingcollectionadapter2.collections.DiffObservableList
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import pl.szkoleniaandroid.flickrgallery.BR
import pl.szkoleniaandroid.flickrgallery.R
import pl.szkoleniaandroid.flickrgallery.binding.OnItemClickListener
import pl.szkoleniaandroid.flickrgallery.domain.usecases.FetchPhotosByTagsUseCase
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo
import pl.szkoleniaandroid.flickrgallery.livedatautils.LiveEvent

@FlowPreview
class GalleryViewModel(
    private val fetchPhotosByTagsUseCase: FetchPhotosByTagsUseCase
) : ViewModel() {

    val items = DiffObservableList<GalleryItemModel>(object :
        DiffUtil.ItemCallback<GalleryItemModel>() {
        override fun areItemsTheSame(
            oldItem: GalleryItemModel,
            newItem: GalleryItemModel
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: GalleryItemModel,
            newItem: GalleryItemModel
        ) = oldItem.link == newItem.link
    })

    val itemBinding = ItemBinding.of<GalleryItemModel>(BR.item, R.layout.gallery_item)
        .bindExtra(BR.listener, object : OnItemClickListener<GalleryItemModel> {
            override fun onItemClicked(item: GalleryItemModel) {
                showDetailsEvent.value = item.photo
            }
        })

    val search = MutableLiveData<String>("")

    private val state = MutableLiveData<GalleryState>(EmptyState)

    val emptyVisible = state.map { it == EmptyState }
    val loadingVisible = state.map { it == LoadingState }
    val dataVisible = state.map { it == DataLoadedState }

    val showSortDialogEvent = LiveEvent<SortOrder>()
    val showErrorDialogEvent = LiveEvent<String>()
    val showDetailsEvent = LiveEvent<Photo>()

    private var sortOrder = SortOrder.DATE_TAKEN

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        showErrorDialogEvent.value = throwable.localizedMessage!!
    }

    init {
        viewModelScope.launch(errorHandler) {

            search.asFlow().debounce(SEARCH_DEBOUNCE_DELAY_IN_MS).collect { tags ->
                state.value = LoadingState
                items.update(emptyList())
                val photos = fetchPhotosByTagsUseCase(tags).map { it.toUiModel() }

                state.value = if (photos.isEmpty()) {
                    EmptyState
                } else {
                    showSorted(photos)
                    DataLoadedState
                }
            }
        }
    }

    fun selectSortOrder() {
        showSortDialogEvent.value = sortOrder
    }

    fun updateSortOrder(newSortOrder: SortOrder) {
        sortOrder = newSortOrder
        showSorted(items)
    }

    private fun showSorted(photos: List<GalleryItemModel>) {
        items.update(photos.sortedWith(sortOrder.sortFunction))
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY_IN_MS = 500L
    }
}

sealed class GalleryState

object EmptyState : GalleryState()
object LoadingState : GalleryState()
object DataLoadedState : GalleryState()

enum class SortOrder(val sortFunction: Comparator<GalleryItemModel>) {
    DATE_TAKEN(compareBy { it.dateTaken }),
    DATE_PUBLISHED(compareBy { it.datePublished })
}

class GalleryItemModel(
    val link: String,
    val name: String,
    val imageUrl: String,
    val datePublished: LocalDateTime,
    val dateTaken: OffsetDateTime,
    val photo: Photo
)

private fun Photo.toUiModel() = GalleryItemModel(
    link,
    tags,
    imageUrl,
    datePublished,
    dateTaken,
    photo = this
)
