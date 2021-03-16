package uz.fintech.uzbankcard.navui.twoui.payments


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.payments_fragment.*
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.PaymentsAdapter
import uz.fintech.uzbankcard.common.lazyFast
import uz.fintech.uzbankcard.model.PaymentModel
import uz.fintech.uzbankcard.navui.onclikc.IHomeOnClick


class PaymentsFragment : Fragment(R.layout.payments_fragment), IHomeOnClick {

    private val viewModel:PaymentViewMOdel by activityViewModels()
  private val  paymentAdapter by lazyFast { PaymentsAdapter(this) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager=GridLayoutManager(requireContext(),3)
        viewModel.ItemPayment()
        payment_rec.layoutManager=layoutManager
        payment_rec.adapter=paymentAdapter


        viewModel.loadPayment.observe(viewLifecycleOwner, Observer {
            paymentAdapter.submitList(it)
        })
    }

    override fun onClickListener(paymentModel: PaymentModel) {
        Toast.makeText(requireContext(), "${paymentModel.payname}", Toast.LENGTH_SHORT).show()
    }
}