package uz.fintech.uzbankcard.ui.singup

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function4
import kotlinx.android.synthetic.main.singup_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.etExtension
import uz.fintech.uzbankcard.common.hideKeyboard
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.utils.IPreferences
import uz.fintech.uzbankcard.utils.PhoneNumberTextWatcher
import uz.fintech.uzbankcard.utils.PreferenceManager

class SingUpFragment : Fragment(R.layout.singup_fragment), View.OnClickListener {

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.splash_nav)
    }
   val singUpViewModel:SingUpViewModel by activityViewModels()

    private var anim: Animation? = null
    private var callnum: String? = null
    private var name: String? = null
    private var password: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anim = AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
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
            Function4<CharSequence, CharSequence, CharSequence, Boolean, Boolean>
            { phone, name, password, isChecked -> return@Function4 phone.isNotEmpty()
                    && name.isNotEmpty() && password.isNotEmpty() && isChecked }
        )
            .doOnNext {
                gb_next_singup.isEnabled = it
            }
            .subscribe()
    }

    private fun isValidate(
        phone: CharSequence,
        name: CharSequence,
        password: CharSequence,
        isChecked: Boolean
    ): Boolean {
        if (phone.length < 12) til_singup_call.error =
            getString(R.string.error_phone_req) else til_singup_call.error = null
        if (name.length < 4) til_singup_name.error =
            getString(R.string.error_name_req) else til_singup_name.error = null
        if (password.length < 8) til_sinup_pass.error =
            getString(R.string.error_password_req) else til_sinup_pass.error = null
        if (!isChecked) mchb_terms.error =
            getString(R.string.error_terms_of_use_agreement) else mchb_terms.error = null
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
            it.hideKeyboard()
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

        callnum = etExtension(gb_call_singup)
        name = etExtension(gb_name_singup)
        password = etExtension(gb_pass_singup)


        if (callnum!!.length > 8 && name!!.length > 4 && password!!.length > 7) {
           singUpViewModel.singUpSave(callnum.toString(),
               name.toString(),password.toString())
            navController.navigate(R.id.codesave_navigation)

        }
    }
}