package uz.fintech.uzbankcard.navui.twoui.cards


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.cards_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.navui.twoui.home.HomeViewModel


class CardsFragment : Fragment(R.layout.cards_fragment), IHomeCardOnClick {


   private val adapter by lazyFast { CardHomeAdapter(this) }
    private val homeViewModel:HomeViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        cards_rec.adapter=adapter
        homeViewModel.dbReadVM()
       homeViewModel.dbList().observe(viewLifecycleOwner, Observer {
           adapter.submitList(it)
       })

    }

    override fun onClickItem(cardModel: CardModel) {

    }
}