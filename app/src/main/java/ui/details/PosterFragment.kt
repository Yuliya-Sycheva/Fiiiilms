package ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentPosterBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import presentation.details.PosterViewModel

class PosterFragment : Fragment() {

    companion object {
        const val URL_KEY = "URL_KEY"

        fun getURL(url: String) = PosterFragment().apply {
            arguments = bundleOf(URL_KEY to url)
        }
    }

    private val posterViewModel: PosterViewModel by viewModel {
        parametersOf(requireArguments().getString(URL_KEY))
    }

    private lateinit var binding: FragmentPosterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPosterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        posterViewModel.observeUrl().observe(this.viewLifecycleOwner) {
            showImage(it)
        }
    }

    fun showImage(url: String) {
        context?.let {
            Glide.with(it)
                .load(url)
                .into(binding.poster)
        }
    }
}