package ui.root

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itproger.spr_15_clean_architecture_films.R
import com.itproger.spr_15_clean_architecture_films.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment, R.id.moviesCastFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Вы действительно хотите выйти из приложения?")
            .setNegativeButton("Нет") { dialog, which ->

            }.setPositiveButton("Да") { dialog, which ->
                //выходим:
                finish()
            }

        // добавление слушателя для обработки нажатия на кнопку Back
        onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmDialog.show()
            }
        })
    }

    fun animateBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }
}
