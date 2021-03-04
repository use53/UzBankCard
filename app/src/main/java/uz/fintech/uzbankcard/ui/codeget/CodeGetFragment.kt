package uz.fintech.uzbankcard.ui.codeget

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.nirigo.mobile.view.passcode.PasscodeView
import com.nirigo.mobile.view.passcode.models.PasscodeItem
import kotlinx.android.synthetic.main.code_get_fragment.*
import uz.fintech.uzbankcard.MainActivity
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.codeadapter.PasscodeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.navui.NavigationActivity

class CodeGetFragment :Fragment(R.layout.code_get_fragment), PasscodeView.OnItemClickListener,
    View.OnClickListener {

    private val passcodeAdapter by lazyFast { PasscodeAdapter(requireContext()) }
    private var  stringBuilder:StringBuilder?=null
    private var list:MutableList<Int>?=null
    private val navController by lazy { Navigation.findNavController(requireActivity(),R.id.splash_nav) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passcode_get_view.adapter=passcodeAdapter
        stringBuilder=StringBuilder()
        list= mutableListOf()
          passcode_get_view.setOnItemClickListener(this)
          tv_codeget_onclick.setOnClickListener(this)
    }

    override fun onItemClick(passcodeView: PasscodeView?, position: Int, p2: View?, p3: Any?) {
        if (!passcode_get_indicator.isAnimationInProgress){
            val passcodeItem=passcodeView!!.adapter.getItem(position)
            if (passcodeItem.type== PasscodeItem.TYPE_CLEAR){
                stringBuilder= StringBuilder()
                passcode_get_indicator.clear()
                list!!.clear()
            }else if(passcodeItem.type== PasscodeItem.TYPE_NUMBER){
                list!!.add(position)
                stringBuilder!!.append(stringBuilder?.length)
                passcode_get_indicator.indicatorLevel=stringBuilder!!.length
                if (stringBuilder!!.length== passcode_get_indicator.getIndicatorLength()){
                    Toast.makeText(requireContext(), list!!.joinToString(""), Toast.LENGTH_SHORT).show()
                    val intent=Intent(requireContext(),NavigationActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }


        }
    }

    override fun onClick(v: View?) {
        navController.navigate(R.id.rigentry_navigation)
    }
}