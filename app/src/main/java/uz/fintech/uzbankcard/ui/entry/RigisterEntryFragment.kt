package uz.fintech.uzbankcard.ui.entry

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.rigisentry_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.lazyFast

class RigisterEntryFragment:Fragment(R.layout.rigisentry_fragment){


    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(),R.id.splash_nav)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemAnimation()
        setupOnClick()

    }

    private fun setupOnClick() {
        gb_rig.setOnClickListener {
            navController.navigate(R.id.singup_navigation)
        }
        gb_open.setOnClickListener {
            navController.navigate(R.id.singin_navigation)
        }
    }

    private fun itemAnimation() {
        val anim=AnimationUtils.loadAnimation(requireContext(),R.anim.anim)
        gb_open.animation=anim
        gb_rig.animation=anim
        tv_lenguege.animation=anim

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }

            })
    }
}