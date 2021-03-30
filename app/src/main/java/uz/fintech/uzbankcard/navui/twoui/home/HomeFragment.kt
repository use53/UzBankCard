package uz.fintech.uzbankcard.navui.twoui.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.graphic_fragment.*
import kotlinx.android.synthetic.main.home_card_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.adapter.SavePaymentAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.network.NetworkStatus


class HomeFragment : Fragment(R.layout.home_fragment),
    Toolbar.OnMenuItemClickListener, IHomeCardOnClick {

    private val observable= Observer<NetworkStatus>{
        when(it){
        is NetworkStatus.Loading->showLoading()
        is NetworkStatus.Success->showSuccess()
        is NetworkStatus.Offline->showOffline()
        is NetworkStatus.Error->ErrorItem()
        }
    }

    private fun ErrorItem() {
      //  requireActivity().finish()
    }

    private fun showOffline() {
        home_wp_progressBar.hideProgressBar()
    }

    private fun showSuccess() {
        home_wp_progressBar.hideProgressBar()
    }


    private fun showLoading() {
     home_wp_progressBar.showProgressBar()
    }

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val savePaymentAdapter by lazyFast {
        SavePaymentAdapter()
    }
    private val viewmodel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.dbReadVM()
    }
    private var cardHomeAdapter:CardHomeAdapter?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusServis()
               dbHistoryCard()
           paymentSaveItem()
          bardataApi()

        home_tollbar.inflateMenu(R.menu.home_menu)
        home_tollbar.setOnMenuItemClickListener(this)

    }

    private fun statusServis() {
        viewmodel.statusVM()
        viewmodel.statusLiveData().observe(viewLifecycleOwner, observable)
    }

    private fun paymentSaveItem() {
        home_payment_rec.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        home_payment_rec.adapter=savePaymentAdapter
        viewmodel.historyRead()
        viewmodel.paymentLoad().observe(viewLifecycleOwner, Observer {
            savePaymentAdapter.submitList(it)

        })

    }

    private fun bardataApi() {
        viewmodel.readApiSerVM()
        viewmodel.responsApi().observe(viewLifecycleOwner, Observer {
            home_barchart.data=it
            home_barchart.invalidate()
        })
    }

    private fun dbHistoryCard() {
        cardHomeAdapter = CardHomeAdapter(this)
        home_rec.adapter = cardHomeAdapter
        viewmodel.dbList().observe(viewLifecycleOwner, Observer {
            cardHomeAdapter!!.submitList(it)
        })

    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return if (item!!.itemId == R.id.home_menu_plus) {
            navController.navigate(R.id.add_card_navigation)
            requireActivity().nav_view.isGone=true
            true
        } else if (item.itemId==R.id.home_setting){
            navController.navigate(R.id.home_setting_navigation)
            true
        }else false
    }

    override fun onClickItem(cardModel: CardModel) {
       requireContext().toast("Asosiy kartaga biriktirish")
    }
}