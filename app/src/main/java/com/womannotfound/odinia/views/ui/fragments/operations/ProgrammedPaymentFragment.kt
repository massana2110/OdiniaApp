package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentProgrammedPaymentBinding

class ProgrammedPaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentProgrammedPaymentBinding>(inflater,R.layout.fragment_programmed_payment,container,false)

        binding.btnAdd.setOnClickListener{
            val name = binding.namePayment.text.toString()
            val amount = binding.addAmount.text.toString()
            val category = binding.addCategory.selectedItem.toString()
            val date = binding.addDate.text.toString()

            it.findNavController()
                .navigate(ProgrammedPaymentFragmentDirections.actionProgrammedPaymentFragmentToNavPayments(name,category,amount,date))
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
