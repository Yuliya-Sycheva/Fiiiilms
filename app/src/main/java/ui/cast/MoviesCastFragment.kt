package ui.cast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.itproger.spr_15_clean_architecture_films.R
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentCastBinding
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import presentation.cast.MoviesCastViewModel
import ui.cast.models.CastState
import ui.details.DetailsFragment

class MoviesCastFragment : Fragment() {

    companion object {
        const val ID_KEY = "id"

        fun createCastArgs(id: String) : Bundle = bundleOf(ID_KEY to id)

//        fun newInstance(id: String) = MoviesCastFragment().apply {
//            arguments = bundleOf(ID_KEY to id)
//        }

        // Тег для использования во FragmentManager
        const val TAG = "MoviesCastFragment"
    }

    private lateinit var binding: FragmentCastBinding

    private val adapter = ListDelegationAdapter(
        movieCastHeaderDelegate(),
        movieCastPersonDelegate(),
    )

    private val viewModel by viewModel<MoviesCastViewModel> {
        parametersOf(
            requireArguments().getString("id") ?: ""
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCastBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.moviesCastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.moviesCastRecyclerView.adapter = adapter

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is CastState.Content -> showContent(it)
                is CastState.Error -> showError(it)
                is CastState.Loading -> showLoading()
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            contentContainer.visibility = View.GONE
            errorMessageTextView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showContent(state: CastState.Content) {
        with(binding) {
            contentContainer.visibility = View.VISIBLE
            movieTitle.text = state.fullTitle
            errorMessageTextView.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
        adapter.items = state.items
        adapter.notifyDataSetChanged()
    }

    private fun showError(castState: CastState.Error) {
        with(binding) {
            contentContainer.visibility = View.GONE
            progressBar.visibility = View.GONE
            errorMessageTextView.visibility = View.VISIBLE
            errorMessageTextView.setText(getString(R.string.something_went_wrong))
        }
    }
}