package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import com.womannotfound.odinia.R

class PaymentsItems {
    var imageResource: Int = R.drawable.ic_ingresos
        private set
    var namePayment: String? = null
        private set
    var datePayment: String? = null
        private set
    var categoryPayment: String? = null
        private set
    var amountPayment: String? = null
        private set

    constructor() {
        //empty constructor needed
    }

    constructor(imageResource: Int,namePayment: String?, categoryPayment: String?, amountPayment: String?) {
        this.imageResource = imageResource
        this.namePayment = namePayment
        this.categoryPayment = categoryPayment
        this.amountPayment = amountPayment
    }
}