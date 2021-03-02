package uz.fintech.uzbankcard.ui.singin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.android.synthetic.main.singin_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.utils.PreferenceManager

class SinginFragment :Fragment(R.layout.singin_fragment){

    private val preference by lazy { PreferenceManager.instanse(requireContext()) }
    private val navController by lazy { Navigation.findNavController(requireActivity(),R.id.splash_nav) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_singin.setNavigationOnClickListener {
            navController.navigate(R.id.rigentry_navigation)
        }
        gb_next_singin.setOnClickListener {

           Observable.combineLatest(
                gb_name_singin.textChanges(),
                gb_pass_singin.textChanges(),
                BiFunction< CharSequence, CharSequence,Boolean>(this::isValidet)
           ).subscribe()
            if (gb_name_singin.text.toString().equals(preference.isSingupName) && gb_pass_singin.text.toString().equals(preference.isSingupPassword)){
                navController.navigate(R.id.codesave_navigation)
            }else{
                Toast.makeText(requireContext(), "oldin ro'yxatdan uting", Toast.LENGTH_SHORT).show()
            }
        }
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