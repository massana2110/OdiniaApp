package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialPurchasesBinding

/**
 * A simple [Fragment] subclass.
 */
class OdiniaSocialPurchasesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOdiniaSocialPurchasesBinding>(inflater,
        R.layout.fragment_odinia_social_purchases,container,false)
        binding.shareButton.setOnClickListener{
            it.findNavController().navigate(R.id.nav_odiniaSocial)
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.purchases,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cardview_color,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner2.adapter = adapter
        }

        return binding.root
    }

}
