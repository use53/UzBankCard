package uz.fintech.uzbankcard.ui.singup

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function4
import kotlinx.android.synthetic.main.singup_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.utils.IPreferences
import uz.fintech.uzbankcard.utils.PhoneNumberTextWatcher
import uz.fintech.uzbankcard.utils.PreferenceManager
import java.lang.Exception

class SingUpFragment : Fragment(R.layout.singup_fragment), View.OnClickListener {

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.splash_nav)
    }
  private val preference:IPreferences by lazyFast {
         PreferenceManager.instanse(requireContext())
  }
    private var anim:Animation?=null
    private var callnum: String? = null
    private var name: String? = null
    private var password: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         anim= AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
        itemGetEditText()
        singupAimation()
        rxBindingEditText()
        itemMask()
    }

    private fun itemGetEditText() {
        gb_next_singup.setOnClickListener(this)
    }

    private fun rxBindingEditText() {
        Observable.combineLatest(
            gb_call_singup.textChanges(),
            gb_name_singup.textChanges(),
            gb_pass_singup.textChanges(),
            mchb_terms.checkedChanges(),
            Function4 <CharSequence, CharSequence, CharSequence, Boolean, Boolean>
            { phone, name, password, isChecked -> return@Function4 phone.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && isChecked }
        )
            .doOnNext { gb_next_singup.isEnabled = it
                         gb_next_singup.animation=anim}
            .subscribe()
    }

    private fun isValidate(
        phone: CharSequence,
        name: CharSequence,
        password: CharSequence,
        isChecked: Boolean
    ): Boolean {
      if (phone.length<12) til_singup_call.error = getString(R.string.error_phone_req) else til_singup_call.error=null
        if (name.length<4) til_singup_name.error = getString(R.string.error_name_req) else til_singup_name.error=null
        if (password.length<8) til_sinup_pass.error = getString(R.string.error_password_req) else til_sinup_pass.error=null
        if (!isChecked) mchb_terms.error = getString(R.string.error_terms_of_use_agreement) else mchb_terms.error=null
        return true
    }

    private fun itemMask() {
        val phone = PhoneNumberTextWatcher(gb_call_singup)
        gb_call_singup.addTextChangedListener(phone)
    }

    private fun singupAimation() {
        gb_name_singup.animation = anim
        gb_pass_singup.animation = anim
        gb_call_singup.animation = anim
        gb_next_singup.animation = anim
        toolbar_singup.setNavigationOnClickListener {
            navController.navigate(R.id.rigentry_navigation)
        }
    }

    override fun onClick(v: View) {
        Observable.combineLatest(
            gb_call_singup.textChanges(),
            gb_name_singup.textChanges(),
            gb_pass_singup.textChanges(),
            mchb_terms.checkedChanges(),
            Function4(this::isValidate)
        ).subscribe()

        callnum = gb_call_singup.text.toString()
        name = gb_name_singup.text.toString()
        password = gb_pass_singup.text.toString()


        if (callnum!!.length>8 && name!!.length>4 && password!!.length>7){
               preference.isSingupName=name.toString()
            preference.isSingupPassword=password.toString()
             navController.navigate(R.id.codesave_navigation)

        }else{
            Toast.makeText(requireContext(), "xato", Toast.LENGTH_SHORT).show()
        }
    }
}