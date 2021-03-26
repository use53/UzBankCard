package uz.fintech.uzbankcard.ui.codesave

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.nirigo.mobile.view.passcode.PasscodeView
import com.nirigo.mobile.view.passcode.models.PasscodeItem
import kotlinx.android.synthetic.main.codesave_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.codeadapter.PasscodeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.navui.NavigationActivity
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.PreferenceManager

class CodeSaveFragment :Fragment(R.layout.codesave_fragment),
    PasscodeView.OnItemClickListener,
    View.OnClickListener {

    private val firebaseCave= Observer< NetworkStatus>{
        when(it){
            is NetworkStatus.Loading->showloading()
            is NetworkStatus.Success->itemFinish()
            is NetworkStatus.Offline->offlineError()
            is NetworkStatus.Error->ServisError()
        }
    }

    private fun ServisError() {
        passcode_indicator.clear()
        passcode_indicator.isGone=false
        tv_codesave_onclick.isGone=false
        codesave_lottie.visibility=View.INVISIBLE
        requireContext().toast(getString(R.string.error_servis))
    }

    private fun offlineError() {
        passcode_indicator.clear()
        requireContext().toast(getString(R.string.offline_internet))
    }

    private fun itemFinish() {
        val intent=Intent(requireContext(),NavigationActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun showloading() {
        codesave_lottie.visibility=View.VISIBLE
        passcode_indicator.isGone=true
        tv_codesave_onclick.isGone=true
    }

    private val passcodeAdapter by lazyFast { PasscodeAdapter(requireContext()) }

    private val codeSaveViewModel:CodeSaveViewModel by activityViewModels()

    private val preference by lazyFast { PreferenceManager.instanse(requireContext()) }
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         passcode_view.adapter=passcodeAdapter

         passcode_view.setOnItemClickListener(this)

    }

    override fun onItemClick(passcodeView: PasscodeView?,
                             position: Int,
                             p2: View?, p3: Any?) {

                if (!passcode_indicator.isAnimationInProgress){
                val passcodeItem=passcodeView!!.adapter.getItem(position)
                     if (passcodeItem.type==PasscodeItem.TYPE_CLEAR){
                         passcode_indicator.clear()
                         codeSaveViewModel.codeClear()
                     }else if(passcodeItem.type==PasscodeItem.TYPE_NUMBER){
                          val ldLength= codeSaveViewModel.codeAddListVM(position)
                         ldLength.observe(viewLifecycleOwner, Observer {
                             itemListAdd(it)
                         })

                     }
                }


    }

    private fun itemListAdd(it: Int) {
        passcode_indicator.indicatorLevel=it
        if (it== passcode_indicator.getIndicatorLength()){
            tv_codesave_onclick.setOnClickListener(this)
        }
    }

    override fun onClick(v: View?) {
        codeSaveViewModel.firebseSaveVM(requireContext())
            .observe(viewLifecycleOwner,firebaseCave)

    }
}




