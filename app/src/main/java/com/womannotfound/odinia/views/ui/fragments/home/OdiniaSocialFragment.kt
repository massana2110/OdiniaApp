package com.womannotfound.odinia.views.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialBinding

/**
 * A simple [Fragment] subclass.
 */
class OdiniaSocialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val binding = DataBindingUtil.inflate<FragmentOdiniaSocialBinding>(inflater,
          R.layout.fragment_odinia_social,container,false)

        binding.button.setOnClickListener{

            it.findNavController().navigate(R.id.action_nav_odiniaSocial_to_nav_odiniaSocialPurchases)

        }

        return binding.root
    }

}
