package uz.fintech.uzbankcard.navui.twoui.payments


import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.add_card_fragment.*
import kotlinx.android.synthetic.main.dialog_layout.view.*
import kotlinx.android.synthetic.main.payments_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.PaymentsAdapter
import uz.fintech.uzbankcard.common.etExtension
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.common.toast
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.navui.onclikc.IPaymentOnClick
import uz.fintech.uzbankcard.network.NetworkStatus
import uz.fintech.uzbankcard.network.StatusLoading


@Suppress("DEPRECATION")
class PaymentsFragment : Fragment(R.layout.payments_fragment), IPaymentOnClick {

    private var isBoolean=false
    private val obserable= Observer<StatusLoading>{
       when(it){
           is StatusLoading.LoadingBigin->showBeginLoading()
           is StatusLoading.Success->showSuccess()
           is StatusLoading.Error->showBeginLoading()
           is StatusLoading.Loading->showLoading()
           is StatusLoading.SuccessUpdate->showUpdateSuccess()
       }
    }

    private fun showUpdateSuccess() {
       if (isBoolean){
           val handler=Handler()
           lottie_okay_payment.visibility=View.VISIBLE
           payment_lottie.visibility=View.INVISIBLE
           handler.postDelayed({
               payment_rec.isGone=false
               lottie_okay_payment.visibility=View.INVISIBLE
           },3_000)
           isBoolean=false
       }
    }

    private fun showLoading() {
        payment_lottie.visibility=View.VISIBLE
       payment_rec.isGone=true
    }

    private fun showSuccess() {
        payment_lottie.visibility=View.INVISIBLE
    }

    private fun showBeginLoading() {
        payment_lottie.visibility=View.VISIBLE
    }

    private val navController by lazyFast {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private val viewModel:PaymentViewMOdel by activityViewModels()
  private val  paymentAdapter by lazyFast { PaymentsAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          onAdapterRepo()
         ItemStatusVM()

    }

    private fun ItemStatusVM() {
        viewModel.statusVM().observe(viewLifecycleOwner,obserable)
    }

    private fun onAdapterRepo() {
        val layoutManager=GridLayoutManager(requireContext(),3)
        viewModel.ItemPayment()
        payment_rec.layoutManager=layoutManager
        payment_rec.adapter=paymentAdapter
        viewModel.ldpaymentVM().observe(viewLifecycleOwner, Observer {
             paymentAdapter.submitList(it)
        })
    }

    override fun onClickListener(paymentModel: PaymentModel) {

        val view= layoutInflater.inflate(R.layout.dialog_layout,null)
 val dialog=AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(R.string.positiv_dialog) { dialog, which ->

                val money= etExtension( view.gb_money_dialog)
                val name= etExtension(view.gb_code_dialog)
                if (money.isNotEmpty() && name.length>5){
                viewModel.paymentMoney(money,paymentModel.payname)
                    isBoolean=true
                }else{
                    requireContext().toast(getString(R.string.update_payment))
                }

            }
     .setNegativeButton(R.string.negativ_dialog) { dialog, which ->
         dialog.dismiss()
     }.create()
  dialog.show()
    }
}