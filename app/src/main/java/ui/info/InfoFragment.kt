package ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentInfoBinding
import com.itproger.spr_15_clean_architecture_films.databinding.FragmentNamesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import presentation.names.NamesSearchViewModel

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoBinding.inflate(inflater, container, false)

        return binding.root
    }
}