package com.womannotfound.odinia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class EgressMoneyViewModel: ViewModel() {
    var date: String = ""
    var account: String = ""
    var amount: Float = 0F
    var category: String = ""
    var note: String = ""
    val createdAt: String = ""

    init {
        Log.i("EgressMoneyViewModel", "EgressMoneyVM Created")
    }
}