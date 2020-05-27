package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPaymentsBinding
import com.womannotfound.odinia.views.ui.classes.PaymentAdapter
import com.womannotfound.odinia.views.ui.classes.PaymentsItems
import kotlinx.android.synthetic.main.fragment_payments.*

class PaymentsFragment : Fragment() {
    private val list = ArrayList<PaymentsItems>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPaymentsBinding>(
            inflater,
            R.layout.fragment_payments,
            container,
            false
        )

        binding.btnAdd.setOnClickListener {
            it.findNavController()
                .navigate(PaymentsFragmentDirections.actionNavPaymentsToProgrammedPaymentFragment())
        }

        val args = PaymentsFragmentArgs.fromBundle(arguments!!)

        if (args.name != " " && args.category != " " && args.amount != " " && args.date != " " ) {
            binding.layoutPayment.removeView(binding.logoView)
            binding.layoutPayment.removeView(binding.txtPaymentSch)
            binding.layoutPayment.removeView(binding.txtMsg)
            binding.layoutPayment.removeView(binding.addMsg)
            //binding.layoutPayment.removeView(binding.btnAdd)
            val amount = "$${args.amount}"

            binding.recyclerView.isVisible = true
            val item = PaymentsItems(
                R.drawable.ic_ingresos,
                args.name,
                args.category,
                amount,
                args.date
            )
            list += item
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recycler_view.adapter = PaymentAdapter(list)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }
}