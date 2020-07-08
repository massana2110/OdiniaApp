package com.womannotfound.odinia.views.ui.fragments.operations

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPaymentsBinding
import com.womannotfound.odinia.viewmodel.AccountsViewModel
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentsItems


class PaymentsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: PaymentsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPaymentsBinding>(
            inflater,
            R.layout.fragment_payments,
            container,
            false
        )
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        viewModel = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(PaymentsViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnAdd.setOnClickListener {
            it.findNavController()
                .navigate(PaymentsFragmentDirections.actionNavPaymentsToProgrammedPaymentFragment())
        }
        val userID = auth.currentUser?.uid.toString()
        if (viewModel.name != "" && viewModel.amount != "") {
            binding.layoutPayment.removeView(binding.logoView)
            binding.layoutPayment.removeView(binding.txtPaymentSch)
            binding.layoutPayment.removeView(binding.txtMsg)
            binding.layoutPayment.removeView(binding.addMsg)

            binding.recyclerView.isVisible = true

            val accountName = viewModel.account
            val amount = "$${viewModel.amount}"
            val itemB = PaymentsItems(
                R.drawable.ic_ingresos,
                viewModel.name,
                viewModel.category,
                amount,
                viewModel.date
            )
            viewModel.list += itemB



            addPayment(
                userID,
                viewModel.name,
                viewModel.account,
                viewModel.category,
                viewModel.amount,
                viewModel.date,
                viewModel.inputDate
            )

        } else if (viewModel.list.isNotEmpty()) {
            binding.layoutPayment.removeView(binding.logoView)
            binding.layoutPayment.removeView(binding.txtPaymentSch)
            binding.layoutPayment.removeView(binding.txtMsg)
            binding.layoutPayment.removeView(binding.addMsg)

            binding.recyclerView.isVisible = true
        } else {
            getPayments(userID, binding)
        }
        viewModel.name = ""
        viewModel.account = ""
        viewModel.category = ""
        viewModel.amount = ""
        viewModel.date = ""
        viewModel.inputDate=""


        viewManager = LinearLayoutManager(context)
        viewAdapter = PaymentAdapter(viewModel.list)
        recyclerView = binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return binding.root
    }

    private fun addPayment(userID: String, namePayment: String, accountName: String, categoryPayment: String, amountPayment: String, datePayment: String,inputDate: String) {
        val user = hashMapOf(
            "userID" to userID,
            "namePayment" to namePayment,
            "accountName" to accountName,
            "categoryPayment" to categoryPayment,
            "amountPayment" to amountPayment,
            "datePayment" to datePayment,
            "inputDate" to inputDate
        )
        db.collection("payments")
            .add(user)
            .addOnSuccessListener { Log.d("AddPayment", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { Log.w("AddPayment", "Error writing document") }
        updateBalance(userID, accountName, amountPayment)

    }

    private fun getPayments(userID: String, binding: FragmentPaymentsBinding) {
        db.collection("payments")
            .whereEqualTo("userID", userID)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("namePayment").toString()
                    val category = document.getString("categoryPayment").toString()
                    var amount = "$${document.getString("amountPayment").toString()}"
                    val date = document.getString("datePayment").toString()




                    val item = PaymentsItems(R.drawable.ic_ingresos, name, category, amount, date)

                    viewModel.list.add(item)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
                if (!documents.isEmpty) {
                    binding.layoutPayment.removeView(binding.logoView)
                    binding.layoutPayment.removeView(binding.txtPaymentSch)
                    binding.layoutPayment.removeView(binding.txtMsg)
                    binding.layoutPayment.removeView(binding.addMsg)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(
                    "getPayment",
                    "Error getting documents",
                    exception
                )
            }

    }


    private fun updateBalance(userID: String, accountName: String, amount: String) {


        db.collection("accounts")
            .whereEqualTo("userID", userID)
            .whereEqualTo("nameAccount", accountName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val balance =
                        document.getString("balanceAccount").toString()
                    db.collection("accounts")
                        .whereEqualTo("userID", userID)
                        .whereEqualTo("nameAccount", accountName)
                        .addSnapshotListener { snapshot, e ->
                            if (e != null) {
                                Log.w(TAG, "Listen Failed", e)
                                return@addSnapshotListener
                            }
                            if (snapshot != null) {
                                val documents = snapshot.documents
                                documents.forEach {
                                    val account = it.toObject(PaymentsViewModel::class.java)
                                    if (account != null) {
                                        val indocument = it.id


                                        db.collection("accounts")
                                            .document(indocument)
                                            .update(
                                                "balanceAccount",
                                                (balance.toFloat() - amount.toFloat()).toString()

                                            )

                                    }


                                }


                            }

                        }

                }

            }


    }
}









