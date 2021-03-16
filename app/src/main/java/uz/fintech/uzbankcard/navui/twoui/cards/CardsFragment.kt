package uz.fintech.uzbankcard.navui.twoui.cards


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.cards_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.navui.twoui.home.HomeDB


class CardsFragment : Fragment(R.layout.cards_fragment), IHomeCardOnClick {


    private val homeDB by lazyFast { HomeDB(requireContext()) }
   private val adapter by lazyFast { CardHomeAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeDB.loadDb()
        cards_rec.adapter=adapter
        homeDB.itemList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

    }

    override fun onClickItem(cardModel: CardModel) {

    }
}