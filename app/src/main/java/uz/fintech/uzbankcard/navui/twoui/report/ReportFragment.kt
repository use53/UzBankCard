package uz.fintech.uzbankcard.navui.twoui.report

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.report_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.cardDateUtils
import uz.fintech.uzbankcard.common.cardNumber
import uz.fintech.uzbankcard.common.formatDecimals
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.twoui.home.HomeDB
import uz.fintech.uzbankcard.utils.CardExpiryDateFormatWatcher
import uz.fintech.uzbankcard.utils.CardNumberFormatWatcher
import uz.fintech.uzbankcard.utils.PreferenceManager

class ReportFragment :Fragment(R.layout.report_fragment){

    private val preferense by lazyFast {
        PreferenceManager.instanse(requireContext())
    }
    private val viewModel:ReportViewModel by activityViewModels()
 private val homeDB by lazyFast {
     HomeDB(requireContext())
 }
    private var cardNumbers:String?=null
    private var cardDate:String?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                homeDB.loadDb()
                homeDB.itemList.observe(viewLifecycleOwner, Observer {
                    it.forEach {
                        if (it.cardnum==preferense.isCardModelSave){

                        }
                    }
                })


        val edoptions=object :CardNumberFormatWatcher(ed_report_card_add){
            override fun afterTextWithoutPattern(cardNumber: String) {
              cardNumbers=cardNumber
            }
        }
        ed_report_card_add.addTextChangedListener(edoptions)
        val edDate=object :CardExpiryDateFormatWatcher(ed_report_card_interval){
            override fun afterTextWithoutPattern(expiredDate: String) {
              cardDate=expiredDate
            }
        }
        ed_report_card_interval.addTextChangedListener(edDate)



        gb_report.setOnClickListener {
            if (cardNumbers!!.length.equals(16) && cardDate!!.length.equals(4)){
                ed_report_card_money.visibility=View.VISIBLE
                viewModel.loadItemRt("${cardNumbers}${cardDate}")
                viewModel.loadCard.observe(viewLifecycleOwner, Observer {
                    reportItem(it)
                })
            }
        }

    }

    private fun reportItem(it: CardModel) {
        report_add_money.text=it.money.toString().toDouble().formatDecimals()
        report_add_number.text= cardNumber(it.cardnum!!.substring(0,16))
        report_add_date_year.text= cardDateUtils(it.cardnum.substring(16,20))
        report_firstname.text=it.firstname
        report_surname.text=it.surname
    }
}