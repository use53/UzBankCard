package uz.fintech.uzbankcard.navui.twoui.addcard

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.add_card_fragment.*
import uz.click.mobilesdk.utils.CardExpiryDateFormatWatcher
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.common.cardDateUtils
import uz.fintech.uzbankcard.common.cardNumber
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.HistoryDB
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.utils.CardNumberFormatWatcher

@Suppress("DEPRECATION")
class AddCardFragment ():Fragment(R.layout.add_card_fragment),
        View.OnClickListener {

    private var cardModel:CardModel?=null
    private val obsevast=Observer<CardModel>{
        saveCard(it)
        cardModel=it
    }
    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(),R.id.nav_host_fragment) }
    private lateinit var handler:Handler
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
        save_card_toolbar.visibility=View.VISIBLE
        wp_progressBar.hideProgressBar()
        handler.postDelayed({
            lottie_okay.visibility=View.INVISIBLE
        },3000)
    }

    private fun showError(errorMsg: Int) {
        wp_progressBar.hideProgressBar()
        ed_card_add.isGone=false
        ed_card_interval.isGone=false
        card_add_button.isGone=false
        et_card_number.isGone=false

    }

    private fun showloading() {
       wp_progressBar.showProgressBar()
        ed_card_add.isGone=true
        ed_card_interval.isGone=true
        card_add_button.isGone=true
        et_card_number.isGone=true

    }

    private val viewModel:AddCardViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        save_card_toolbar.setOnClickListener {
           val bundle=Bundle().apply {
           }
            Log.d("key", "onViewCreated: ${cardModel.toString()}")
                 viewModel.saveRoom("${number}${interval}")
            navController.popBackStack(R.id.home_navigation, false)


        }
    }


    private fun itemAddInterval(text: String) {
        interval=text
        val textParsed= cardDateUtils(text)
        card_add_date_year.text=textParsed
    }

    private fun tvItemCardNumber(text: String) {
        val textParsed=cardNumber(text)
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