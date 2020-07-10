package com.womannotfound.odinia.views.ui.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.graphics.toColorInt
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentOdiniaSocialPurchasesBinding
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.SocialItems
import kotlinx.android.synthetic.main.fragment_odinia_social_purchases.*
import kotlinx.android.synthetic.main.social_items.view.*

class OdiniaSocialPurchasesFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val binding = DataBindingUtil.inflate<FragmentOdiniaSocialPurchasesBinding>(
            inflater,
            R.layout.fragment_odinia_social_purchases, container, false
        )
        binding.shareButton.setOnClickListener {
            it.findNavController().navigate(R.id.nav_odiniaSocial)
        }

        val userID: String = auth.currentUser!!.uid.toString()
        val spinnerPurchases = binding.spinnerPurchases
        val selectedCategory: String? = binding.spinnerPurchases.selectedItem?.toString()
        val selectedColor:String? = binding.spinnerCardColor.selectedItem?.toString()


        populateSpinnerPurchases(spinnerPurchases as Spinner, userID)

        binding.spinnerPurchases.onItemSelectedListener = this

        //changeCardViewColor(selectedColor, binding)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cardview_color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCardColor.adapter = adapter
        }

        val list = ArrayList<SocialItems>()
        getExpenses(userID,selectedCategory,list)

        viewManager = LinearLayoutManager(context)
        viewAdapter = SocialAdapter(list)
        recyclerView = binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
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
                        val purchase = document.getString("categoryEgress").toString()
                        userPurchases.add(purchase)
                    }
                    adapter.notifyDataSetChanged()
                }
            }

    }

    private fun getExpenses(userID: String,categoryEgress:String?, socialList: ArrayList<SocialItems>) {
        db.collection("expenses")
            .whereEqualTo("userId",userID)
            .whereEqualTo("categoryEgress", categoryEgress)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    val category = document.getString("categoryEgress").toString()
                    val date = document.getString("dateEgress").toString()
                    val item = SocialItems(R.drawable.ic_gastos,category,date)

                    socialList.add(item)
                    recyclerView.adapter?.notifyDataSetChanged()
                }

            }
            .addOnFailureListener{ exception -> Log.w("getExpenses", "Error getting documents", exception) }
    }


    /*private fun changeCardViewColor(
        selectedColor: String?,
        binding: FragmentOdiniaSocialPurchasesBinding
    ) {
        val socialCard: CardView = binding.socialLayout.cardView
        when (selectedColor) {
            "Azul" -> socialCard.setCardBackgroundColor(Color.BLUE)
            "Gris Oscuro" -> socialCard.setCardBackgroundColor(Color.DKGRAY)
            "Verde" -> socialCard.setCardBackgroundColor(Color.GREEN)
            "Magenta" -> socialCard.setCardBackgroundColor(Color.MAGENTA)
            "Cyan" -> socialCard.setCardBackgroundColor(Color.CYAN)
            else -> socialCard.setCardBackgroundColor(Color.parseColor("#393F56"))

        }
    }
*/
}
