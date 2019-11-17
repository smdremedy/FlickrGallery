package pl.szkoleniaandroid.flickrgallery.utils

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import pl.szkoleniaandroid.flickrgallery.R

class ErrorDialogFragment : DialogFragment() {

    private val args by navArgs<ErrorDialogFragmentArgs>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.error))
        builder.setMessage(getString(R.string.connection_error, args.message))
        builder.setPositiveButton(android.R.string.ok, null)
        builder.create()
        return super.onCreateDialog(savedInstanceState)
    }
}