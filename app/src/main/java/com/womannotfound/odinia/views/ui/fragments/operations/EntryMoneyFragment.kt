package com.womannotfound.odinia.views.ui.fragments.operations

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.womannotfound.odinia.R
import com.womannotfound.odinia.databinding.FragmentEntryMoneyBinding
import com.womannotfound.odinia.viewmodel.EntryMoneyViewModel
import com.womannotfound.odinia.viewmodel.PaymentsViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EntryMoneyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var entryMoneyViewModel: EntryMoneyViewModel
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val binding = DataBindingUtil.inflate<FragmentEntryMoneyBinding>(
            inflater,
            R.layout.fragment_entry_money, container, false
        )

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val calendar: Calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        entryMoneyViewModel = activity?.run {
            ViewModelProvider(
                this,
                defaultViewModelProviderFactory
            ).get(EntryMoneyViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.btnDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view: DatePicker?, dpYear: Int, dpMonth: Int, dpDay: Int ->
                    //Set to text View
                    binding.selectedDate.text = "${dpDay}/${dpMonth + 1}/${dpYear}"
                }, year, month, day
            )
            datePickerDialog.show()
        }

        val userID = auth.currentUser?.uid.toString()

        binding.btnAdd!!.setOnClickListener {
            entryMoneyViewModel.date = binding.selectedDate.text.toString()
            entryMoneyViewModel.account = binding.spinnerAccounts.selectedItem.toString()
            entryMoneyViewModel.amount = binding.addAmount!!.text.toString()
            entryMoneyViewModel.category =
                binding.spinnerEntryCategories.selectedItem.toString()
            entryMoneyViewModel.note = binding.editText7!!.text.toString()

            if (entryMoneyViewModel.amount == "" || entryMoneyViewModel.date == "" || entryMoneyViewModel.note == "") {
                Toast.makeText(context, "Por favor, no dejar campos vacios", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //Toast.makeText(context, "Sus ingresos han sido añadidos exitosamente", Toast.LENGTH_SHORT).show()
                addEntryMoney(userID, entryMoneyViewModel.date, entryMoneyViewModel.account, entryMoneyViewModel.amount, entryMoneyViewModel.category, entryMoneyViewModel.note, currentDate)
                //updateAccountBalance(userID, entryMoneyViewModel.amount, entryMoneyViewModel.account)
                addActivity(userID,entryMoneyViewModel.note,entryMoneyViewModel.date,entryMoneyViewModel.amount,"#98878F",currentDate, entryMoneyViewModel.account)
            }
        }

        val spinnerCategories = binding.spinnerEntryCategories
        val spinnerAccounts: Spinner = binding.spinnerAccounts


        populateSpinnerAccounts(spinnerAccounts as Spinner, userID)
        populateSpinnerCategories(spinnerCategories as Spinner, userID)


        binding.spinnerAccounts.onItemSelectedListener = this
        binding.spinnerEntryCategories.onItemSelectedListener = this

        return binding.root
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }


    private fun populateSpinnerAccounts(spinner: Spinner, userID: String) {
        val userAccounts: ArrayList<String> = ArrayList()
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            userAccounts
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        db.collection("accounts").whereEqualTo("userID", userID).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document: QueryDocumentSnapshot in task.result!!) {
                        val account: String? = document.getString("nameAccount")
                        userAccounts.add(account!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
    }

    private fun populateSpinnerCategories(spinner: Spinner, userID: String) {
        val categoriesRef: CollectionReference = db.collection("entries_categories")
        val entryCategories: ArrayList<String> = ArrayList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            entryCategories
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = adapter;

        categoriesRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    if (document.getString("userID").toString() == userID) {
                        val category = document.getString("name")
                        entryCategories.add(category!!)
                    }
                    if (document.getString("userID").toString() == "null") {
                        val category = document.getString("name")
                        entryCategories.add(category!!)
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateAccountBalance(userID: String, amountEntry: String, nameAccount: String) {
    var i = 0

    val balanceRef = db.collection("accounts")
    balanceRef
        .whereEqualTo("userID", userID)
        .whereEqualTo("nameAccount", nameAccount)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val balance: String =
                    document.getString("balanceAccount").toString()
                balanceRef
                    .whereEqualTo("userID", userID)
                    .whereEqualTo("nameAccount", nameAccount)
                    .addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w("updateBalance", "Listen Failed", e)
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val documents: MutableList<DocumentSnapshot> = snapshot!!.documents
                            documents.forEach {
                                val account = it.toObject(PaymentsViewModel::class.java)
                                if (account != null) {
                                    val indocument = it.id
                                    if (i == 0) {
                                        db.collection("accounts")
                                            .document(indocument)
                                            .update(
                                                "balanceAccount",
                                                (balance.toFloat() + amountEntry.toFloat()).toString()

                                            )
                                        i = +1
                                    }
                                }
                            }

                        }
                    }
            }
        }
}

    private fun addEntryMoney(userId: String, dateEntry: String, accountEntry: String, amountEntry: String, categoryEntry: String, noteEntry: String, createdAt: String) {
        val entry = hashMapOf(
            "userID" to userId,
            "dateEntry" to dateEntry,
            "accountEntry" to accountEntry,
            "amountEntry" to amountEntry,
            "categoryEntry" to categoryEntry,
            "noteEntry" to noteEntry,
            "createdAt" to createdAt
        )
        db.collection("entries_money")
            .add(entry)
            .addOnSuccessListener {
                Toast.makeText(context, "Sus ingresos han sido añadidos exitosamente", Toast.LENGTH_SHORT).show()
                updateAccountBalance(userId, amountEntry, accountEntry)
            }
            .addOnFailureListener { Toast.makeText(context, "Sus ingresos no se han registrado, intente de nuevo.", Toast.LENGTH_SHORT).show() }
    }

    private fun addActivity(userID: String, activityName: String, activityDate: String, activityAmount: String, cardColor: String, inputDate: String,account: String){
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
}
