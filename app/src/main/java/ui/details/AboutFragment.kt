package ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.itproger.spr_15_clean_architecture_films.R
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentAboutBinding
import domain.models.MovieDetails
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import presentation.details.AboutViewModel
import ui.cast.MoviesCastFragment
import ui.details.models.AboutState

class AboutFragment : Fragment() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"

        fun getAboutId(id: String) = AboutFragment().apply {
            arguments = bundleOf(MOVIE_ID to id)
        }
    }

 //   private val router : Router by inject()

    private val aboutViewModel: AboutViewModel by viewModel {
        parametersOf(requireArguments().getString(MOVIE_ID))
    }

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        aboutViewModel.observeAbout().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.showCastButton.setOnClickListener() {

            findNavController().navigate(
                R.id.action_detailsFragment_to_moviesCastFragment,
                MoviesCastFragment.createCastArgs(requireArguments().getString(MOVIE_ID).orEmpty())
            )

            //router.openFragment(MoviesCastFragment.newInstance(requireArguments().getString(MOVIE_ID).orEmpty()))

        }
    }

    private fun render(state: AboutState) {
        when (state) {
            is AboutState.Content -> showContent(state.movie)
            is AboutState.Error -> showError(state.message)
        }
    }

    private fun showContent(movieDetails: MovieDetails?) {
        binding.apply {
            details.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE
            title.text = movieDetails?.title
            ratingValue.text = movieDetails?.imDbRating
            yearValue.text = movieDetails?.year
            countryValue.text = movieDetails?.countries
            genreValue.text = movieDetails?.genres
            directorValue.text = movieDetails?.directors
            writerValue.text = movieDetails?.writers
            castValue.text = movieDetails?.stars
            plot.text = movieDetails?.plot
        }
    }

    private fun showError(message: String) {
        binding.details.visibility = View.GONE
        binding.errorMessage.visibility = View.VISIBLE
        binding.errorMessage.text = message
    }
}