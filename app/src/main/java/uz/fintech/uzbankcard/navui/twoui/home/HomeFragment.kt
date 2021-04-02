package uz.fintech.uzbankcard.navui.twoui.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.graphic_fragment.*
import kotlinx.android.synthetic.main.home_card_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.CardHomeAdapter
import uz.fintech.uzbankcard.adapter.SavePaymentAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.showDialogNoCard
import uz.fintech.uzbankcard.model.CardModel
import uz.fintech.uzbankcard.navui.database.paymentsave.PaymentHistory
import uz.fintech.uzbankcard.navui.onclikc.IHomeCardOnClick
import uz.fintech.uzbankcard.navui.onclikc.IPaymentHistoryDelete
import uz.fintech.uzbankcard.navui.twoui.cards.CardsStatus
import uz.fintech.uzbankcard.navui.twoui.cards.CardsViewModel
import uz.fintech.uzbankcard.network.NetworkStatus


@Suppress("DEPRECATION")
class HomeFragment : Fragment(R.layout.home_fragment),
    Toolbar.OnMenuItemClickListener, IHomeCardOnClick, IPaymentHistoryDelete {

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val savePaymentAdapter by lazyFast {
        SavePaymentAdapter(this)
    }
    private var cardHomeAdapter: CardHomeAdapter? = null

    private val viewmodel: HomeViewModel by activityViewModels()
    private val cardsViewModel: CardsViewModel by activityViewModels()

    private var correntdelete = false
    private var correntsave = false

    private val dialodObser = Observer<CardsStatus> {
            when (it) {
                is CardsStatus.SaveCardError -> dialogError()
                is CardsStatus.SaveCardSuccess -> dialogSave()
                is CardsStatus -> dialogDelete()

        }
    }

    private fun dialogDelete() {
        if (correntdelete)
        requireContext().showDialogNoCard(getString(R.string.dialog_delete))
    }

    private fun dialogSave() {
        if (correntsave)
        requireContext().showDialogNoCard(getString(R.string.dialog_save))
    }

    private fun dialogError() {
        if (correntsave)
        requireContext().showDialogNoCard(getString(R.string.dialog_save_error))
    }


    private val observable = Observer<NetworkStatus> {
        when (it) {
            is NetworkStatus.Loading -> showLoading()
            is NetworkStatus.Success -> showSuccess()
            is NetworkStatus.Error -> ErrorItem()
        }
    }

    private fun ErrorItem() {
        requireActivity().finish()
    }

    private fun showSuccess() {
        home_wp_progressBar.hideProgressBar()
    }


    private fun showLoading() {
        home_wp_progressBar.showProgressBar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.dbReadVM()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusItem()
        dbHistoryCard()
        paymentSaveItem()
        bardataApi()

        home_tollbar.inflateMenu(R.menu.home_menu)
        home_tollbar.setOnMenuItemClickListener(this)

    }

    private fun statusItem() {
        viewmodel.statusVM()
        viewmodel.statusLiveData().observe(viewLifecycleOwner, observable)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun paymentSaveItem() {
        val icon=resources.getDrawable(R.drawable.icon_delete_24)
        viewmodel.onRecyclerviewDeleteItem(savePaymentAdapter,icon)
        home_payment_rec.adapter = savePaymentAdapter
        viewmodel.historyRead()
        viewmodel.paymentLoad().observe(viewLifecycleOwner, Observer {
            savePaymentAdapter.submitList(it)
        })
        viewmodel.ldRecylerviewDeleteId().observe(viewLifecycleOwner, Observer {
            it.attachToRecyclerView(home_payment_rec)
        })

    }

    private fun bardataApi() {
        viewmodel.readApiSerVM()
        viewmodel.responsApi().observe(viewLifecycleOwner, Observer {
            home_barchart.data = it
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
            requireActivity().nav_view.isGone = true
            true
        } else if (item.itemId == R.id.home_setting) {
            navController.navigate(R.id.home_setting_navigation)
            true
        } else false
    }

    override fun onClickItem(cardModel: CardModel) {

        val dialog = AlertDialog.Builder(requireContext())
            .setItems(R.array.delete_list) {
                    dialog, which ->
                cardsViewModel.ldStatusVM()
                cardsViewModel.ldStatusCards().observe(viewLifecycleOwner, dialodObser)
                when (which) {
                    0 -> {
                        correntdelete=true
                        cardsViewModel.cardDeleteVM(cardModel)
                        viewmodel.dbReadVM()
                        viewmodel.dbList().observe(viewLifecycleOwner, Observer {
                            cardHomeAdapter!!.submitList(it)

                        })

                        dialog.dismiss()
                    }
                    1 -> {
                        correntsave=true
                        cardsViewModel.updateCardVM(cardModel)
                    }
                    2 -> {
                        getString(R.string.bloc_card)
                    }
                }
            }
            .create()
        dialog.show()
    }

    override fun onClickDelete(payment: PaymentHistory) {
       // viewmodel.onRecyclerviewTouch(payment)
    }


}