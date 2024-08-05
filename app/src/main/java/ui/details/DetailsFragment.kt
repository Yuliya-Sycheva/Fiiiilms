package ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentDetailsBinding
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentMoviesBinding

class DetailsFragment : Fragment() {

    companion object {
        const val POSTER_KEY = "poster"
        const val ID_KEY = "id"

//        fun newInstance(poster: String, id: String) = DetailsFragment().apply {
//            arguments = bundleOf(POSTER_KEY to poster, ID_KEY to id)

        fun createArgs(poster: String, id: String) : Bundle = bundleOf(POSTER_KEY to poster, ID_KEY to id)


 //       const val TAG = "DetailsFragment"
    }

    private lateinit var binding: FragmentDetailsBinding

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val poster = requireArguments().getString("poster") ?: ""
        val movieId = requireArguments().getString("id") ?: ""


        val adapter = PagerAdapter(childFragmentManager, lifecycle, poster, movieId)
        //childFragmentManager используется внутри фрагмента для управления вложенными фрагментами
        //supportFragmentManager используется в активити для управления фрагментами, потому заменили

        binding.viewPager.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "ПОСТЕР"
                1 -> tab.text = "О ФИЛЬМЕ"
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}