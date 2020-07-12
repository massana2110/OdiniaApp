package com.womannotfound.odinia.views.ui.fragments.dashboard

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountDashboardBinding
import com.womannotfound.odinia.viewmodel.ActivitiesViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivitiesItems
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivityAdapter

class AccountDashboardFragment : Fragment() {
    private lateinit var activityViewModel: ActivitiesViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= DataBindingUtil.inflate<FragmentAccountDashboardBinding>(inflater,
            R.layout.fragment_account_dashboard, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        activityViewModel = activity?.run {
            ViewModelProvider(this,defaultViewModelProviderFactory).get(ActivitiesViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.textViewGraphs.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }
        binding.imageViewToGraphArrow.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }
        val userID = auth.currentUser?.uid.toString()

        if(activityViewModel.list.isEmpty()){
            getActivities(userID, binding)
        }/*else{
            //Consulta muchas veces la base de datos
            activityViewModel.list.clear()
            getActivities(userID,binding)
        }*/

        viewManager = LinearLayoutManager(context)
        viewAdapter = ActivityAdapter(activityViewModel.list)
        recyclerView = binding.recyclerViewActivities.apply{
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
        }
        return binding.root
    }

    private fun getActivities(userID: String, binding: FragmentAccountDashboardBinding) {
        db.collection("entries_money")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("noteEntry").toString()
                    val date = document.getString("dateEntry").toString()
                    val amount = document.getString("amountEntry").toString()

                    val item = ActivitiesItems(Color.parseColor("#8CC63F"), name, date, amount, "Entry")

                    activityViewModel.list.add(item)
                }

                if (!documents.isEmpty) {
                    binding.activityLayout.removeView(binding.textViewHint)
                    binding.activityLayout.removeView(binding.textViewEmpty)
                }
            }

        db.collection("payments")
            .whereEqualTo("userID",userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val name = document.getString("namePayment").toString()
                    val date = document.getString("datePayment").toString()
                    val amount = "$${document.getString("amountPayment").toString()}"

                    val item = ActivitiesItems(Color.parseColor("#985E6D"),name,date,amount,"Payment")

                    activityViewModel.list.add(item)
                }

                if(!documents.isEmpty){
                    binding.activityLayout.removeView(binding.textViewHint)
                    binding.activityLayout.removeView(binding.textViewEmpty)
                }
            }

        db.collection("expenses")
            .whereEqualTo("userID",userID)
            .get()
            .addOnSuccessListener { documents ->
                for( document in documents){
                    val name = document.getString("descriptionEgress").toString()
                    val date = document.getString("dateEgress").toString()
                    val amount = document.getString("amountEgress").toString()

                    val item = ActivitiesItems(Color.parseColor("#FF0000"),name,date,amount,"Egress")

                    activityViewModel.list.add(item)
                }

                if(!documents.isEmpty){
                    binding.activityLayout.removeView(binding.textViewHint)
                    binding.activityLayout.removeView(binding.textViewEmpty)
                }
            }
    }
}
