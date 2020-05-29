package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPaymentsBinding
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentsItems
import kotlinx.android.synthetic.main.fragment_payments.*

class PaymentsFragment : Fragment() {
    private lateinit var viewModel: PaymentsViewModel
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
        viewModel = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(PaymentsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAdd.setOnClickListener {
            it.findNavController()
                .navigate(PaymentsFragmentDirections.actionNavPaymentsToProgrammedPaymentFragment())
        }

        if( viewModel.name != " " && viewModel.amount != " " && viewModel.list.isEmpty()){
            binding.layoutPayment.removeView(binding.logoView)
            binding.layoutPayment.removeView(binding.txtPaymentSch)
            binding.layoutPayment.removeView(binding.txtMsg)
            binding.layoutPayment.removeView(binding.addMsg)

            binding.recyclerView.isVisible = true
            val amount = "$${viewModel.amount}"
            val itemB = PaymentsItems(
                R.drawable.ic_ingresos,
                viewModel.name,
                viewModel.category,
                amount,
                viewModel.date
            )
            viewModel.list += itemB
            viewModel.name = ""
            viewModel.category = ""
            viewModel.amount = ""
            viewModel.date = ""

        }else if(viewModel.list.isNotEmpty()){
            binding.layoutPayment.removeView(binding.logoView)
            binding.layoutPayment.removeView(binding.txtPaymentSch)
            binding.layoutPayment.removeView(binding.txtMsg)
            binding.layoutPayment.removeView(binding.addMsg)

            binding.recyclerView.isVisible = true

            if( viewModel.name != "" && viewModel.amount != ""){
                val amount = "$${viewModel.amount}"
                val itemB = PaymentsItems(
                    R.drawable.ic_ingresos,
                    viewModel.name,
                    viewModel.category,
                    amount,
                    viewModel.date
                )
                viewModel.list += itemB
                viewModel.name = ""
                viewModel.category = ""
                viewModel.amount = ""
                viewModel.date = ""
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recycler_view.adapter = PaymentAdapter(viewModel.list)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.setHasFixedSize(true)
    }
}