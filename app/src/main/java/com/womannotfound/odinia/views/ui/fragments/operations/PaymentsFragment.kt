package com.womannotfound.odinia.views.ui.fragments.operations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPaymentsBinding

class PaymentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPaymentsBinding>(inflater,R.layout.fragment_payments,container,false)

        binding.btnAdd.setOnClickListener{
            it.findNavController().navigate(PaymentsFragmentDirections.actionNavPaymentsToProgrammedPaymentFragment())
        }

        return binding.root
    }

}
