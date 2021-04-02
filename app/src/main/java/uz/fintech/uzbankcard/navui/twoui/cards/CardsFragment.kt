package uz.fintech.uzbankcard.navui.twoui.cards

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.cards_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.adapter.CardsColorAdapter
import uz.fintech.uzbankcard.adapter.SavePaymentAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.navui.onclikc.ICardColorOnClick
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.navui.onclikc.IPaymentHistoryDelete
import uz.fintech.uzbankcard.navui.twoui.home.HomeViewModel


class CardsFragment :
    Fragment(R.layout.cards_fragment),
    IHomeCardOnClick,
    ICardColorOnClick, Toolbar.OnMenuItemClickListener, IPaymentHistoryDelete {


   private val adapter by lazyFast { CardHomeAdapter(this) }
    private val homeViewModel:HomeViewModel by activityViewModels()
    private val cardsViewModel:CardsViewModel by activityViewModels()
    private var cardModel:CardModel?=null
    private var corrent=false
    private var correntColor=false
    private val savePaymentAdapter by lazyFast { SavePaymentAdapter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tollbar_cards.inflateMenu(R.menu.cards_menu)
        tollbar_cards.setOnMenuItemClickListener(this)
        colorListItem()
        searchlist()
        cards_rec.adapter=adapter
        homeViewModel.dbReadVM()
       homeViewModel.dbList().observe(viewLifecycleOwner, Observer {
           adapter.submitList(it)

       })

    }

    private fun searchlist() {
        rec_history_payment.adapter=savePaymentAdapter
    }

    private fun colorListItem() {
        val cardsColorAdapter=CardsColorAdapter(this)
        color_rec.adapter=cardsColorAdapter
        cardsViewModel.colorListVM()
        cardsViewModel.ldColorVM().observe(viewLifecycleOwner, Observer {
            cardsColorAdapter.submitList(it)
            button_card.setOnClickListener {
                if (!correntColor){
                    color_rec.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    correntColor=true
                }else{
                    color_rec.layoutManager=GridLayoutManager(requireContext(),3)
                    correntColor=false
                }
            }
        })
    }

    override fun onClickItem(cardModel: CardModel) {
        this.cardModel=cardModel
        cardsViewModel.historyListPayment(cardModel.cardnum.toString())
        cardsViewModel.listSearchVM().observe(viewLifecycleOwner, Observer {
            savePaymentAdapter.submitList(it)
        })


    }

    override fun colorOnClickListener(cardColorModel: CardColorModel) {
        if (cardModel!=null) {
            homeViewModel.onClickCardColorVM(cardModel!!, cardColorModel)
            homeViewModel.dbReadVM()
            homeViewModel.dbList().observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                requireContext().toast("tug'ri")
            })
        }else{
            requireContext().toast(getString(R.string.card_color))
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return if (item!!.itemId==R.id.vertical_menu){
            if (!corrent){
                cards_rec.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
               corrent=true
            }else{
                cards_rec.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                corrent=false
            }
            true
        }else{
            false
        }
    }

    override fun onClickDelete(payment: PaymentHistory) {

    }

}