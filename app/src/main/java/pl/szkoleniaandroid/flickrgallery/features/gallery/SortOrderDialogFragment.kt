package pl.szkoleniaandroid.flickrgallery.features.gallery

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.szkoleniaandroid.flickrgallery.R

class SortOrderDialogFragment : DialogFragment() {

    private val args by navArgs<SortOrderDialogFragmentArgs>()

    private val galleryViewModel by sharedViewModel<GalleryViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.sort_order_dialog_title))
        builder.setSingleChoiceItems(R.array.sort_types, args.sortOrder.ordinal) { dialog, which ->
            val newSortOrder = SortOrder.values()[which]
            galleryViewModel.updateSortOrder(newSortOrder)
            dialog.dismiss()
        }
        return builder.create()
    }
}