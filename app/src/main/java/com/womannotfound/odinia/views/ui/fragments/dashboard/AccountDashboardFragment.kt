package com.womannotfound.odinia.views.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountDashboardBinding
import com.womannotfound.odinia.viewmodel.ActivitiesViewModel

class AccountDashboardFragment : Fragment() {
    private lateinit var activityViewModel: ActivitiesViewModel
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= DataBindingUtil.inflate<FragmentAccountDashboardBinding>(inflater,
            R.layout.fragment_account_dashboard, container, false)

        activityViewModel = activity?.run {
            ViewModelProvider(this,defaultViewModelProviderFactory).get(ActivitiesViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.textViewGraphs.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }
        binding.imageViewToGraphArrow.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }

        return binding.root
    }

}
