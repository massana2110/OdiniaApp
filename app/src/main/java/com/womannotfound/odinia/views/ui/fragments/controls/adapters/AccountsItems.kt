package com.womannotfound.odinia.views.ui.fragments.controls.adapters

import com.womannotfound.odinia.R

class AccountsItems {
    var imageResource = R.drawable.ic_ingresos
        private set
    var nameAccount: String? = null
        private set
    var typeAccount: String? = null
        private set
    var balanceAccount: String? = null
        private set

    constructor() {
        //empty constructor needed
    }

    constructor(imageResource: Int,nameAccount: String?, typeAccount: String?, balanceAccount: String?) {
        this.imageResource = imageResource
        this.nameAccount = nameAccount
        this.typeAccount = typeAccount
        this.balanceAccount = balanceAccount
    }
}