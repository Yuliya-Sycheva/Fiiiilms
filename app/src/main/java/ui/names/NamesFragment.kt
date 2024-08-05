package ui.names

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itproger.spr_15_clean_architecture_films.R
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentMoviesBinding
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentNamesBinding
import domain.models.Movie
import domain.models.Person
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.movies.MoviesSearchViewModel
import presentation.names.NamesSearchViewModel
import ui.details.DetailsFragment
import ui.movies.MoviesAdapter
import ui.movies.MoviesFragment
import ui.movies.models.MoviesState
import ui.names.models.NamesState

class NamesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val adapter = NamesAdapter()

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var personsList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<NamesSearchViewModel>()

    private lateinit var binding: FragmentNamesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNamesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeholderMessage = binding.placeholderMessage
        queryInput = binding.queryInput
        personsList = binding.locations
        progressBar = binding.progressBar

        personsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        personsList.adapter = adapter

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        textWatcher?.let { queryInput.addTextChangedListener(it) }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeToastState().observe(viewLifecycleOwner) {
            if (it != null) {
                showToast(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, NamesFragment.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
    }

    private fun render(state: NamesState) {
        when (state) {
            is NamesState.Loading -> showLoading()
            is NamesState.Content -> showContent(state.persons)
            is NamesState.Error -> showError()
            is NamesState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        personsList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        personsList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.setText(getString(R.string.something_went_wrong))
    }

    private fun showEmpty() {
        personsList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.setText(getString(R.string.nothing_found))
    }

    private fun showContent(persons: List<Person>) {
        personsList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter.persons.clear()
        adapter.persons.addAll(persons)
        adapter.notifyDataSetChanged()
    }
}