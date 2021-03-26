package uz.fintech.uzbankcard.ui

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.ui.splash.SplashViewModel
import uz.fintech.uzbankcard.utils.PreferenceManager


@Suppress("DEPRECATION")
class SplashFragment : Fragment(R.layout.splash_fragment) {


    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.splash_nav)
    }
    private val splashViewModel:SplashViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val handler = Handler()

        handler.postDelayed({
            ItemNavigation()
        }, 3000)

    }

    private fun ItemNavigation() {
        splashViewModel.splashRepo()
        splashViewModel.isSplash.observe(viewLifecycleOwner, Observer {
            if (it) {
                navController.navigate(R.id.code_get_navigation)
            } else {
                navController.navigate(R.id.rigentry_navigation)
            }
        })

    }

}


