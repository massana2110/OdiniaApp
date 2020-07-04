package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentProgrammedPaymentBinding
import com.womannotfound.odinia.viewmodel.PaymentsViewModel

class ProgrammedPaymentFragment : Fragment() {
    private lateinit var viewModel: PaymentsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProgrammedPaymentBinding>(inflater,R.layout.fragment_programmed_payment,container,false)

        viewModel = activity?.run {
            ViewModelProvider(this, defaultViewModelProviderFactory).get(PaymentsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAdd.setOnClickListener{
            viewModel.name =binding.namePayment.text.toString()
            viewModel.category =binding.addCategory.selectedItem.toString()
            viewModel.amount = binding.addAmount.text.toString()
            //date
            val day = binding.addDate.dayOfMonth.toString()
            val month = binding.addDate.month + 1
            val year = binding.addDate.year.toString()
            val date = "$day/$month/$year"

            viewModel.date =date

            if( (viewModel.name == " " && viewModel.amount == " ")|| (viewModel.name == "" && viewModel.amount == "")){
                Toast.makeText(context,"Proporcione datos validos",Toast.LENGTH_SHORT).show()
            }else{
                it.findNavController()
                    .navigate(ProgrammedPaymentFragmentDirections.actionProgrammedPaymentFragmentToNavPayments())
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category_payments,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.addCategory.adapter = adapter
        }

        return binding.root
    }
}
