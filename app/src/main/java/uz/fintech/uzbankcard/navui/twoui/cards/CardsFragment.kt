package uz.fintech.uzbankcard.navui.twoui.cards

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.cards_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.adapter.CardsColorAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.model.CardColorModel
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.ICardColorOnClick
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.navui.twoui.home.HomeViewModel


class CardsFragment :
    Fragment(R.layout.cards_fragment),
    IHomeCardOnClick,
    ICardColorOnClick, Toolbar.OnMenuItemClickListener {


   private val adapter by lazyFast { CardHomeAdapter(this) }
    private val homeViewModel:HomeViewModel by activityViewModels()
    private val cardsViewModel:CardsViewModel by activityViewModels()
    private var cardModel:CardModel?=null
    private var corrent=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tollbar_cards.inflateMenu(R.menu.cards_menu)
        tollbar_cards.setOnMenuItemClickListener(this)
        colorListItem()
        cards_rec.adapter=adapter
        homeViewModel.dbReadVM()
       homeViewModel.dbList().observe(viewLifecycleOwner, Observer {
           adapter.submitList(it)

       })

    }

    private fun colorListItem() {
        val cardsColorAdapter=CardsColorAdapter(this)
        color_rec.adapter=cardsColorAdapter
        cardsViewModel.colorListVM()
        cardsViewModel.ldColorVM().observe(viewLifecycleOwner, Observer {
            cardsColorAdapter.submitList(it)
        })
    }

    override fun onClickItem(cardModel: CardModel) {
        this.cardModel=cardModel

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

}