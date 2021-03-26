package uz.fintech.uzbankcard.ui.codeget

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.nirigo.mobile.view.passcode.PasscodeView
import com.nirigo.mobile.view.passcode.models.PasscodeItem
import kotlinx.android.synthetic.main.code_get_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.codeadapter.PasscodeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.navui.NavigationActivity
import uz.fintech.uzbankcard.network.NetworkStatus

class CodeGetFragment :Fragment(R.layout.code_get_fragment),
    PasscodeView.OnItemClickListener,
    View.OnClickListener {

    private val obserable= Observer<NetworkStatus>{
        when(it){
            is NetworkStatus.Loading->showloading()
            is NetworkStatus.Success->successItem()
            is NetworkStatus.Error->errorFile()
            is NetworkStatus.Offline->errorOfline()
        }
    }

    private fun errorOfline() {
    requireContext().toast(getString(R.string.offline_internet))
    }

    private fun errorFile() {
        passcode_get_indicator.clear()
        passcode_get_indicator.isGone=false
        codesave_get_lottie.visibility=View.INVISIBLE
        codeGetViewModel.codeGetClearVM()
      requireContext().toast(getString(R.string.cade_error))
    }

    private fun successItem() {
          val intent= Intent(requireContext(), NavigationActivity::class.java)
                          startActivity(intent)
                          requireActivity().finish()
    }

    private fun showloading() {
        passcode_get_indicator.isGone=true
        codesave_get_lottie.visibility=View.VISIBLE

    }

    private val passcodeAdapter by lazyFast { PasscodeAdapter(requireContext()) }

    private val codeGetViewModel:CodeGetViewModel by activityViewModels()
    private val navController by lazy { Navigation.findNavController(requireActivity(),R.id.splash_nav) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passcode_get_view.adapter=passcodeAdapter

          passcode_get_view.setOnItemClickListener(this)
          tv_codeget_onclick.setOnClickListener(this)
    }

    override fun onItemClick(passcodeView: PasscodeView?, position: Int, p2: View?, p3: Any?) {
        if (!passcode_get_indicator.isAnimationInProgress){
            val passcodeItem=passcodeView!!.adapter.getItem(position)
            if (passcodeItem.type== PasscodeItem.TYPE_CLEAR){
                passcode_get_indicator.clear()
                codeGetViewModel.codeGetClearVM()
            }else if(passcodeItem.type== PasscodeItem.TYPE_NUMBER){
                codeGetViewModel.CodeGetAddVM(position)
                codeGetViewModel.repitAdd().observe(viewLifecycleOwner,
                Observer {
                  itemIndicator(it)
                })


            }


        }
    }

    private fun itemIndicator(it: Int) {
        passcode_get_indicator.indicatorLevel=it
        if (it==passcode_get_indicator.getIndicatorLength()){
            codeGetViewModel.firebaseSearch(requireContext())
            codeGetViewModel.statusNet().observe(viewLifecycleOwner,obserable)

        }
    }

    override fun onClick(v: View?) {
        navController.navigate(R.id.rigentry_navigation)
    }
}