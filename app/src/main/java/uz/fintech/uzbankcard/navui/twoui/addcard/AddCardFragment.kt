package uz.fintech.uzbankcard.navui.twoui.addcard

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.add_card_fragment.*
import uz.click.mobilesdk.utils.CardExpiryDateFormatWatcher
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.CardNumberFormatWatcher

@Suppress("DEPRECATION")
class AddCardFragment ():Fragment(R.layout.add_card_fragment),
        View.OnClickListener {

    private val obsevast=Observer<CardModel>{
        saveCard(it)
    }
    private lateinit var handler:Handler
    private var isEditing=true
    private var number:String?=null
    private var interval:String?=null
    private fun saveCard(it: CardModel?) {
        card_add_money.text= "${it!!.money}"
        card_firstname.text=it.firstname
        card_surname.text=it.surname

    }

    private val observerstatus=Observer<NetworkStatus>{
        when(it){
            is NetworkStatus.Loading ->showloading()
            is NetworkStatus.Error->showError(it.errorMsg)
            is NetworkStatus.Offline->showError(it.errorMsg)
            is NetworkStatus.Success->showCard()
        }
    }

    private fun showCard() {

        viewModel.liveAddCard.observe(viewLifecycleOwner,obsevast)
        handler= Handler()
        lottie_okay.visibility=View.VISIBLE
        wp_progressBar.hideProgressBar()
        handler.postDelayed({
            lottie_okay.visibility=View.INVISIBLE
        },3000)
    }

    private fun showError(errorMsg: Int) {

    }

    private fun showloading() {
       wp_progressBar.showProgressBar()
        ed_card_add.isGone=true
        ed_card_interval.isGone=true
        card_add_button.isGone=true
        et_card_number.isGone=true
        isEditing=false

    }

    private val viewModel:AddCardViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.addcard_menu)
        updateTollbar()


        val cardNumberFormatWatcher=object :CardNumberFormatWatcher(ed_card_add){
            override fun afterTextWithoutPattern(cardNumber: String) {
              number=cardNumber
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

    private fun updateTollbar() {
        isEditing = !isEditing

        val saveItem =toolbar.menu.findItem(R.id.add_card_menu)
        saveItem.isVisible = isEditing
    }


    private fun itemAddInterval(text: String) {
        interval=text
        val textParsed = when {
            text.length >= 3 -> {
                text.substring(0, 2) + " / " + text.substring(2)
            }
            else -> text
        }
        card_add_date_year.text=textParsed
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
        if (number!!.length>15 && interval!!.length>3){
        viewModel.liveCard("${number}${interval}")
        viewModel.livestatus.observe(viewLifecycleOwner, observerstatus)}
        else{
            Toast.makeText(requireContext(), "karta raqami yoki muddati xato", Toast.LENGTH_SHORT).show()
        }
    }

}