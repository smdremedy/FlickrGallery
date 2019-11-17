package pl.szkoleniaandroid.flickrgallery.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pl.szkoleniaandroid.flickrgallery.databinding.PhotoDetailsFragmentBinding
import pl.szkoleniaandroid.flickrgallery.domain.model.Photo

class PhotoDetailsFragment : Fragment() {

    private val args by navArgs<PhotoDetailsFragmentArgs>()
    private val viewModel by viewModel<PhotoDetailsViewModel> { parametersOf(args.photo) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PhotoDetailsFragmentBinding.inflate(inflater, container, false).apply {
        vm = viewModel
        lifecycleOwner = viewLifecycleOwner
    }.root
}

class PhotoDetailsViewModel(val photo: Photo) : ViewModel()
