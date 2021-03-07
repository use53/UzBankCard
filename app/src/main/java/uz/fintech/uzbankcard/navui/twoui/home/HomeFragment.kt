package uz.fintech.uzbankcard.navui.twoui.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.home_card_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*

import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeOnClick


class HomeFragment : Fragment(R.layout.home_fragment),IHomeOnClick, Toolbar.OnMenuItemClickListener {

    private var cardHomeAdapter:CardHomeAdapter?=null
    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(),R.id.nav_host_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list= mutableListOf<CardModel>()
        list.add(CardModel())
        list.add(CardModel())
        cardHomeAdapter= CardHomeAdapter(requireContext(),list,this)
        home_rec.layoutManager= LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        home_rec.adapter=cardHomeAdapter
        home_tollbar.inflateMenu(R.menu.home_menu)
        home_tollbar.setOnMenuItemClickListener(this)

    }

    override fun onClickListener(cardModel: CardModel) {
        Toast.makeText(requireContext(), "tug'ri", Toast.LENGTH_SHORT).show()
        cardHomeAdapter!!.itemList(cardModel)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item!!.itemId==R.id.home_menu_plus){
             navController.navigate(R.id.add_card_navigation)
            return true
        }else return false


    }

}