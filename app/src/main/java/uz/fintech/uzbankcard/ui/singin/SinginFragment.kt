package uz.fintech.uzbankcard.ui.singin

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.android.synthetic.main.singin_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.getString
import uz.fintech.uzbankcard.common.hideKeyboard
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.utils.PreferenceManager

class SinginFragment :
    Fragment(R.layout.singin_fragment){

    private val preference by lazy { PreferenceManager.instanse(requireContext()) }
    private var anim: Animation? = null
    private val navController by lazy { Navigation.findNavController(requireActivity(),R.id.splash_nav) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        anim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
           singInAnimation()

        toolbar_singin.setNavigationOnClickListener {
            navController.navigate(R.id.rigentry_navigation)
            it.hideKeyboard()
        }
        gb_next_singin.setOnClickListener {

           val d=Observable.combineLatest(
                gb_name_singin.textChanges(),
                gb_pass_singin.textChanges(),
                BiFunction< CharSequence, CharSequence,Boolean>(this::isValidet)
           ).subscribe()
            if (gb_name_singin.getString().equals(preference.isSingupName) && gb_pass_singin.getString().equals(preference.isSingupPassword)){
                navController.navigate(R.id.codesave_navigation)
            }else{
              requireContext().toast(getString(R.string.error_singin))
            }
        }
    }

    private fun singInAnimation() {
        gb_name_singin.animation=anim
        gb_next_singin.animation=anim
        gb_pass_singin.animation=anim
    }

    private fun isValidet(
        charSequenceName: CharSequence,
        charSequencePass: CharSequence
        ): Boolean {
        if (charSequenceName.toString().equals(preference.isSingupName)) til_singin_name.error=null else til_singin_name.error=getString(R.string.error_singin_name)
        if (charSequencePass.toString().equals(preference.isSingupPassword))   til_singin_pass.error=null  else  til_singin_pass.error=getString(R.string.error_singin_pass)
        return charSequenceName.isNotEmpty()&&charSequencePass.isNotEmpty()
    }


}