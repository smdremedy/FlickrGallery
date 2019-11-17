package pl.szkoleniaandroid.flickrgallery.features.gallery

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.szkoleniaandroid.flickrgallery.FlickrGalleryNavGraphDirections
import pl.szkoleniaandroid.flickrgallery.R
import pl.szkoleniaandroid.flickrgallery.databinding.GalleryFragmentBinding
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo

class GalleryFragment : Fragment() {

    private val viewModel by sharedViewModel<GalleryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = GalleryFragmentBinding.inflate(inflater, container, false).apply {
        vm = viewModel
        lifecycleOwner = viewLifecycleOwner
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showSortDialogEvent.observe(viewLifecycleOwner) {
            showSortDialog(it)
        }
        viewModel.showErrorDialogEvent.observe(viewLifecycleOwner) {
            showErrorDialog(it)
        }
        viewModel.showDetailsEvent.observe(viewLifecycleOwner) {
            showDetails(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.flicker_gallery, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_sort) {
            viewModel.selectSortOrder()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showErrorDialog(errorMessage: String) {
        findNavController().navigate(FlickrGalleryNavGraphDirections.showErrorDialog(errorMessage))
    }

    private fun showSortDialog(sortOrder: SortOrder) {
        findNavController().navigate(FlickrGalleryNavGraphDirections.showSortOrderDialog(sortOrder))
    }

    private fun showDetails(photo: Photo) {
        findNavController().navigate(GalleryFragmentDirections.showDetails(photo))
    }
}
