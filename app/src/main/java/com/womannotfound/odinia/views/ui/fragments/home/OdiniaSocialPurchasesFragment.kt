package com.womannotfound.odinia.views.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialBinding
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialPurchasesBinding
import com.womannotfound.odinia.viewmodel.SocialViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialItems


class OdiniaSocialPurchasesFragment : Fragment(), AdapterView.OnItemSelectedListener,
    SocialAdapter.OnSocialItemClickListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var vm: SocialViewModel

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        vm = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(SocialViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val binding = DataBindingUtil.inflate<FragmentOdiniaSocialPurchasesBinding>(
            inflater,
            R.layout.fragment_odinia_social_purchases, container, false
        )
        binding.shareButton.setOnClickListener {
            addPost(vm.userID, vm.username, vm.description, vm.date, vm.color, 0, 0)
            viewAdapter.notifyDataSetChanged()
            it.findNavController().navigate(R.id.nav_odiniaSocial)
        }

        val userID: String = auth.currentUser!!.uid.toString()
        val spinnerPurchases = binding.spinnerPurchases

        vm.userID = userID

        populateSpinnerPurchases(spinnerPurchases as Spinner, userID)
        getUsername(userID)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cardview_color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCardColor.adapter = adapter
        }

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = SocialAdapter(vm.list, this)

        binding.spinnerPurchases.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val description = spinnerPurchases.selectedItem?.toString()
                    getExpenses(userID, description)
                    binding.textViewDescription.setText(description)
                    binding.textViewDate.setText(vm.date)

                    viewAdapter.notifyDataSetChanged()
                }

            }

        binding.spinnerCardColor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    vm.color = binding.spinnerCardColor.selectedItem.toString()
                    changeCardViewColor(vm.color, binding)
                    viewAdapter.notifyDataSetChanged()
                }

            }

        return binding.root
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    private fun populateSpinnerPurchases(spinner: Spinner, userID: String) {
        val expensesRef = db.collection("expenses")
        val userPurchases: ArrayList<String> = ArrayList()
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            userPurchases
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        expensesRef
            .whereEqualTo("userId", userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val purchase = document.getString("descriptionEgress").toString()
                        userPurchases.add(purchase)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

    }

    private fun getExpenses(userID: String, descriptionEgress: String?) {
        db.collection("expenses")
            .whereEqualTo("userId", userID)
            .whereEqualTo("descriptionEgress", descriptionEgress)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    vm.description = document.getString("descriptionEgress").toString()
                    vm.date = document.getString("dateEgress").toString()

                }

            }
            .addOnFailureListener { exception ->
                Log.w(
                    "getExpenses",
                    "Error getting documents",
                    exception
                )
            }
    }


    private fun changeCardViewColor(
        selectedColor: String?,
        binding: FragmentOdiniaSocialPurchasesBinding
    ) {
        val socialCard: CardView = binding.cardView
        when (selectedColor) {
            "Azul Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#252850"))
            "Gris Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#828282"))
            "Verde Oscuro" -> socialCard.setCardBackgroundColor(Color.parseColor("#2d572c"))
            "Ocre" -> socialCard.setCardBackgroundColor(Color.parseColor("#b9935a"))
            "Plateado" -> socialCard.setCardBackgroundColor(Color.parseColor("#8a9597"))
            else -> socialCard.setCardBackgroundColor(Color.parseColor("#393F56"))

        }
    }

    private fun getUsername(userID: String) {
        db.collection("users").document(userID).get().addOnSuccessListener { document ->
            if (document != null) {
                vm.username = document["username"].toString()
                vm.userImg = document["photoUrl"].toString()
                Log.w("getUsername", "Getting document succesful")
            } else {
                Log.d("getUsername", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("getUsername", "get failed with", exception)
        }
    }

    private fun addPost(
        userId: String,
        username: String,
        descriptionPost: String?,
        datePost: String,
        cardColor: String,
        likeCounter: Int,
        dislikeCounter: Int
    ) {
        val post = hashMapOf(
            "userID" to userId,
            "username" to username,
            "descriptionPost" to descriptionPost,
            "datePost" to datePost,
            "cardColor" to cardColor,
            "likeCounter" to likeCounter,
            "dislikeCounter" to dislikeCounter
        )
        db.collection("posts").add(post)
    }

    override fun onItemClick(item: SocialItems, position: Int) {

    }


}
