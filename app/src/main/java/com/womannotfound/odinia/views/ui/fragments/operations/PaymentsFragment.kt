package com.womannotfound.odinia.views.ui.fragments.operations

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentPaymentsBinding
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentAdapter
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.PaymentsItems

class PaymentsFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var viewModel: PaymentsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: PaymentAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var paymentsRef: CollectionReference

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

            addPayment(userID, viewModel.name, viewModel.account, viewModel.category, viewModel.amount, viewModel.date, viewModel.inputDate)

            addActivity(userID, viewModel.name, viewModel.date, viewModel.amount,"#985E6D", viewModel.inputDate, viewModel.account)
        }

        paymentsRef = db.collection("payments")

        viewModel.name = ""
        viewModel.account = ""
        viewModel.category = ""
        viewModel.amount = ""
        viewModel.date = ""
        viewModel.inputDate = ""

        setUpRecyclerView(binding,userID)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        viewAdapter.stopListening()
    }

    private fun addPayment(userID: String, namePayment: String, accountName: String, categoryPayment: String, amountPayment: String, datePayment: String, inputDate: String) {
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
            .addOnSuccessListener {
                Log.d("AddPayment", "DocumentSnapshot successfully written!")
                updateBalance(userID, accountName, amountPayment)
            }
            .addOnFailureListener { Log.w("AddPayment", "Error writing document") }


    }

    private fun updateBalance(userID: String, accountName: String, amount: String) {
        var i = 0
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

                                        if (i == 0) {
                                            db.collection("accounts")
                                                .document(indocument)
                                                .update(
                                                    "balanceAccount",
                                                    (balance.toFloat() - amount.toFloat()).toString()

                                                )
                                            i=+1

                                        }
                                    }


                                }


                            }

                        }

                }

            }


    }

    private fun addActivity(userID: String, activityName: String, activityDate: String, activityAmount: String, cardColor: String, inputDate: String, account: String){
        val activity = hashMapOf(
            "userID" to userID,
            "activityName" to activityName,
            "activityDate" to activityDate,
            "activityAmount" to activityAmount,
            "cardColor" to cardColor,
            "createdAt" to inputDate,
            "accountName" to account
        )

        db.collection("activities").add(activity)
    }

    private fun setUpRecyclerView(binding: FragmentPaymentsBinding, userID: String){

        val paymentsQuery: Query = paymentsRef.whereEqualTo("userID",userID)

        val options = FirestoreRecyclerOptions.Builder<PaymentsItems>().setQuery(paymentsQuery, PaymentsItems::class.java).build()

        viewManager = LinearLayoutManager(context)
        viewAdapter = PaymentAdapter(options)
        recyclerView = binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager= viewManager
            adapter= viewAdapter
        }

        paymentsQuery.addSnapshotListener { querySnapshot, _ ->
            if(!querySnapshot?.isEmpty!!){
                binding.layoutPayment.removeView(binding.logoView)
                binding.layoutPayment.removeView(binding.txtPaymentSch)
                binding.layoutPayment.removeView(binding.txtMsg)
                binding.layoutPayment.removeView(binding.addMsg)
            }
        }

    }
}