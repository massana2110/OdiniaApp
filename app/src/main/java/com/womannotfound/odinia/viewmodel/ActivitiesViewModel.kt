package com.womannotfound.odinia.viewmodel

import androidx.lifecycle.ViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivitiesItems

class ActivitiesViewModel : ViewModel(){
    val list = ArrayList<ActivitiesItems>()
}