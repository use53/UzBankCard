package uz.fintech.uzbankcard.navui.twoui.home
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.home_card_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick


class HomeFragment : Fragment(R.layout.home_fragment),
        Toolbar.OnMenuItemClickListener, IHomeCardOnClick {

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
   // private val viewmodel: HomeViewModel by activityViewModels()
    private var iHomeDB:HomeDB?=null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter=CardHomeAdapter(this)
         home_rec.adapter=adapter
          iHomeDB=HomeDB(requireContext())
        iHomeDB!!.loadDb()
        iHomeDB!!.itemList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
      /*  viewmodel.loadHome.observe(viewLifecycleOwner,
                Observer {
            adapter.submitList(it)
        })*/

        home_tollbar.inflateMenu(R.menu.home_menu)
        home_tollbar.setOnMenuItemClickListener(this)

    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return if (item!!.itemId == R.id.home_menu_plus) {
            navController.navigate(R.id.add_card_navigation)
            true
        } else false
    }

    override fun onClickItem(cardModel: CardModel) {
        TODO("Not yet implemented")
    }
}