package com.womannotfound.odinia.views.ui.fragments.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentAccountDashboardBinding
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivitiesItems
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivityAdapter

class AccountDashboardFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ActivityAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var activityRef: CollectionReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= DataBindingUtil.inflate<FragmentAccountDashboardBinding>(inflater,
            R.layout.fragment_account_dashboard, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.textViewGraphs.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }
        binding.imageViewToGraphArrow.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_movements_to_dashboardGrapsFragment)
        }
        val userID = auth.currentUser?.uid.toString()

        activityRef = db.collection("activities")
        setUpRecyclerView(binding, userID)

        return binding.root
    }

    private fun setUpRecyclerView(binding: FragmentAccountDashboardBinding, userID: String){

        val activityQuery: Query = activityRef.whereEqualTo("userID",userID)//.orderBy("createdAt", Query.Direction.ASCENDING)

        val options = FirestoreRecyclerOptions.Builder<ActivitiesItems>().setQuery(activityQuery, ActivitiesItems::class.java).build()

        viewManager = LinearLayoutManager(context)
        viewAdapter = ActivityAdapter(options)
        recyclerView = binding.recyclerViewActivities.apply {
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
        }

        activityQuery.addSnapshotListener { querySnapshot, _ ->
            if(!querySnapshot?.isEmpty!!){
                binding.activityLayout.removeView(binding.textViewHint)
                binding.activityLayout.removeView(binding.textViewEmpty)
            }
        }

    }

    override fun onStart() {
        super.onStart()
        viewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        viewAdapter.stopListening()
    }
}
