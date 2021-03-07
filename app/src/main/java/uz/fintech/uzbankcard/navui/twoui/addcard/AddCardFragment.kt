package uz.fintech.uzbankcard.navui.twoui.addcard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kotlinx.android.synthetic.main.add_card_fragment.*
import uz.click.mobilesdk.utils.CardExpiryDateFormatWatcher
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.utils.CardNumberFormatWatcher

class AddCardFragment ():Fragment(R.layout.add_card_fragment),
        View.OnClickListener {

    private val viewModel:AddCardViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val cardNumberFormatWatcher=object :CardNumberFormatWatcher(ed_card_add){
            override fun afterTextWithoutPattern(cardNumber: String) {
                tvItemCardNumber(cardNumber)
            } }
        val cardExpiryDateFormatWatcher=object :CardExpiryDateFormatWatcher(ed_card_interval){
            override fun afterTextWithoutPattern(expiredDate: String) {
                   itemAddInterval(expiredDate)
            }
        }
        ed_card_interval.addTextChangedListener(cardExpiryDateFormatWatcher)
        ed_card_add.addTextChangedListener(cardNumberFormatWatcher)
        card_add_button.setOnClickListener(this)
    }

    private fun itemAddInterval(text: String) {
        val textParsed = when {
            text.length >= 3 -> {
                text.substring(0, 2) + "/" + text.substring(2)
            }
            else -> text
        }
        card_add_date_year.text=text
    }

    private fun tvItemCardNumber(text: String) {
        val textParsed = when {
            text.length >= 12 -> {
                text.substring(0, 4) + "  " + text.substring(4, 8) +
                        "  " + text.subSequence(8, 12) + "  " + text.substring(12)
            }
            text.length >= 8 -> {
                text.substring(0, 4) + "  " + text.substring(4, 8) + "  " + text.substring(8)
            }
            text.length >= 4 -> {
                text.substring(0, 4) + "  " + text.substring(4)
            }
            else -> text
        }
        card_add_number.text=textParsed
    }

    override fun onClick(v: View?) {
       val cardnumber= ed_card_add.text.toString()
        val cardinterval=ed_card_interval.text.toString()
        val cardModel=CardModel(cardnum = cardnumber,interval = cardinterval)
        viewModel.liveCard(cardModel)

    }
}