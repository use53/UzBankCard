package uz.fintech.uzbankcard.ui.codesave

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nirigo.mobile.view.passcode.PasscodeView
import com.nirigo.mobile.view.passcode.models.PasscodeItem
import kotlinx.android.synthetic.main.codesave_fragment.*
import uz.fintech.uzbankcard.MainActivity
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.codeadapter.PasscodeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.navui.NavigationActivity
import uz.fintech.uzbankcard.utils.PreferenceManager

class CodeSaveFragment :Fragment(R.layout.codesave_fragment), PasscodeView.OnItemClickListener,
    View.OnClickListener {

    private val passcodeAdapter by lazyFast { PasscodeAdapter(requireContext()) }
    private var  stringBuilder:StringBuilder?=null
    private var list:MutableList<Int>?=null
    private val preference by lazyFast { PreferenceManager.instanse(requireContext()) }
     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stringBuilder=StringBuilder()
         list= mutableListOf()
         passcode_view.adapter=passcodeAdapter

         passcode_view.
                 setOnItemClickListener(this)

    }

    override fun onItemClick(passcodeView: PasscodeView?, position: Int, p2: View?, p3: Any?) {
                if (!passcode_indicator.isAnimationInProgress){
                val passcodeItem=passcodeView!!.adapter.getItem(position)
                     if (passcodeItem.type==PasscodeItem.TYPE_CLEAR){
                              stringBuilder= StringBuilder()
                            passcode_indicator.clear()
                             list!!.clear()
                     }else if(passcodeItem.type==PasscodeItem.TYPE_NUMBER){
                         list!!.add(position)
                           stringBuilder!!.append(stringBuilder?.length)
                         passcode_indicator.indicatorLevel=stringBuilder!!.length
                         if (stringBuilder!!.length== passcode_indicator.getIndicatorLength()){
                             Toast.makeText(requireContext(), list!!.joinToString(""), Toast.LENGTH_SHORT).show()
                                tv_codesave_onclick.setOnClickListener(this)
                         }
                     }
                }


    }

    override fun onClick(v: View?) {
        preference.isCallNumber=true
        val intent=Intent(requireContext(),NavigationActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}




