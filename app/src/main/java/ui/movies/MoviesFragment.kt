package ui.movies

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itproger.spr_15_clean_architecture_films.R
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentMoviesBinding
import domain.models.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.movies.MoviesSearchViewModel
import ui.details.DetailsFragment
import ui.movies.models.MoviesState
import ui.root.RootActivity
import utils.debounce

class MoviesFragment : Fragment() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var onMovieClickDebounce: (Movie) -> Unit

    private var adapter: MoviesAdapter? = null

    private lateinit var queryInput: EditText
    private lateinit var placeholderMessage: TextView
    private lateinit var moviesList: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var textWatcher: TextWatcher? = null

    private var isClickAllowed = true

    //  private val handler = Handler(Looper.getMainLooper())

    private val viewModel by viewModel<MoviesSearchViewModel>()

    private lateinit var binding: FragmentMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onMovieClickDebounce = debounce<Movie>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { movie ->
            findNavController().navigate(
                R.id.action_moviesFragment_to_detailsFragment,
                DetailsFragment.createArgs(movie.image, movie.id)
            )
        }
        //эта часть является четвёртым параметром функции debounce.
        // В Kotlin существует синтаксическая особенность, которая позволяет передавать
        // последнюю лямбда-функцию (т.е. функцию, которая передаётся в качестве
        // параметра другой функции) за пределы круглых скобок. Это делает код более читаемым


        adapter = MoviesAdapter { movie ->
            (activity as RootActivity).animateBottomNavigationView()
            onMovieClickDebounce(movie)
//        if (clickDebounce()) {
//
//            findNavController().navigate(
//                R.id.action_moviesFragment_to_detailsFragment,
//                DetailsFragment.createArgs(movie.image, movie.id)
//            )
//        }
        }

        placeholderMessage = binding.placeholderMessage
        queryInput = binding.queryInput
        moviesList = binding.locations
        progressBar = binding.progressBar

        moviesList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        moviesList.adapter = adapter

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
        adapter = null
        moviesList.adapter = null
        textWatcher?.let { queryInput.removeTextChangedListener(it) }
    }

//    private fun clickDebounce(): Boolean { // Функция возвращает значение current, которое указывает, было ли нажатие разрешено в момент вызова функции (gpt)
//        val current = isClickAllowed
//        if (isClickAllowed) {
//            isClickAllowed = false
//            Log.d("myTAG", "isClickAllowed1 $isClickAllowed")
//
//            lifecycleScope.launch {  //Вместо использования viewLifecycleOwner.lifecycleScope, попробуйте использовать lifecycleScope фрагмента напрямую,
//                // чтобы гарантировать, что корутина завершит выполнение своей работы, прежде чем фрагмент будет уничтожен
//                delay(CLICK_DEBOUNCE_DELAY)
//                isClickAllowed = true
//                Log.d("myTAG", "isClickAllowed2 $isClickAllowed")
//            }
//            Log.d("myTAG", "isClickAllowed3 $isClickAllowed")
//            //handler.postDelayed({ isClickAllowed = true }, MoviesFragment.CLICK_DEBOUNCE_DELAY)
//        }
//        return current
//    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
            .show()
    }

    private fun render(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> showLoading()
            is MoviesState.Content -> showContent(state.movies)
            is MoviesState.Error -> showError()
            is MoviesState.Empty -> showEmpty()
        }
    }

    private fun showLoading() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.setText(getString(R.string.something_went_wrong))
    }

    private fun showEmpty() {
        moviesList.visibility = View.GONE
        placeholderMessage.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        placeholderMessage.setText(getString(R.string.nothing_found))
    }

    private fun showContent(movies: List<Movie>) {
        moviesList.visibility = View.VISIBLE
        placeholderMessage.visibility = View.GONE
        progressBar.visibility = View.GONE

        adapter?.movies?.clear()
        adapter?.movies?.addAll(movies)
        adapter?.notifyDataSetChanged()
    }
}