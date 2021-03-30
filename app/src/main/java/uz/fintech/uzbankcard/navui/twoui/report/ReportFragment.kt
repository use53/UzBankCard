package uz.fintech.uzbankcard.navui.twoui.report

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.report_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.cardDateUtils
import uz.fintech.uzbankcard.common.cardNumber
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.CardExpiryDateFormatWatcher
import uz.fintech.uzbankcard.utils.CardNumberFormatWatcher
import uz.fintech.uzbankcard.utils.PreferenceManager

@Suppress("DEPRECATION")
class ReportFragment : Fragment(R.layout.report_fragment) {

    private val observable = Observer<NetworkStatus>{
        when(it){
            is NetworkStatus.Loading->showLoading()
            is NetworkStatus.Success->showSuccess()
            is NetworkStatus.Error->ErrorCodeItem()
        }
    }

    private fun ErrorCodeItem() {
      itemIsBoolean(false)
    }

    private fun itemIsBoolean(isboolean: Boolean) {
        ed_report_card_money.isGone=isboolean
        ed_report_card_interval.isGone=isboolean
        ed_report_card_add.isGone=isboolean
        gb_report.isGone=isboolean
        til_report_card_number.isGone=isboolean
    }

    private fun showSuccess() {
     if (icCorrent){
         report_lottie.visibility=View.INVISIBLE
         lottie_okay_report.visibility=View.VISIBLE
         Handler().postDelayed({
             lottie_okay_report.visibility=View.INVISIBLE
             itemIsBoolean(false)

         },3_000)
         icCorrent=false
         ed_report_card_money.text?.clear()

     }
    }

    private fun showLoading() {
        itemIsBoolean(true)
        report_lottie.visibility=View.VISIBLE
    }


    private val viewModel: ReportViewModel by activityViewModels()

    private var cardNumbers: String? = null
    private var cardDate: String? = null
    private var icCorrent=false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val edoptions = object : CardNumberFormatWatcher(ed_report_card_add) {
            override fun afterTextWithoutPattern(cardNumber: String) {
                cardNumbers = cardNumber
            }
        }
        ed_report_card_add.addTextChangedListener(edoptions)
        val edDate = object : CardExpiryDateFormatWatcher(ed_report_card_interval) {
            override fun afterTextWithoutPattern(expiredDate: String) {
                cardDate = expiredDate
            }
        }
        ed_report_card_interval.addTextChangedListener(edDate)



        gb_report.setOnClickListener {

            if (!cardNumbers.isNullOrEmpty() && !cardDate.isNullOrEmpty()){
            if (cardNumbers!!.length.equals(16) &&
                cardDate!!.length.equals(4) &&
                ed_report_card_money.text.isNullOrEmpty()
            ) {
                    viewModel.loadItemRt("${cardNumbers}${cardDate}")
                    viewModel.ldcardModelVM().observe(viewLifecycleOwner, Observer {
                        if (it != null) {
                            reportItem(it)
                            ed_report_card_money.visibility = View.VISIBLE
                            gb_report.text = getString(R.string.card_update)

                        }
                    })
                    icCorrent=true



            } else if (!ed_report_card_money.text.isNullOrEmpty()) {

                viewModel.updateMoney(
                    ed_report_card_money.text.toString(),
                    "${cardNumbers}${cardDate}"
                )
            }}else{
                requireContext().toast(getString(R.string.isnull_edittext))
            }
        }
        viewModel.ldStatus().observe(viewLifecycleOwner,observable)

    }

    private fun reportItem(it: CardModel) {
        report_add_number.text = cardNumber(it.cardnum!!.substring(0, 16))
        report_add_date_year.text = cardDateUtils(it.cardnum.substring(16, 20))
        report_firstname.text = it.firstname
        report_surname.text = it.surname
    }
}