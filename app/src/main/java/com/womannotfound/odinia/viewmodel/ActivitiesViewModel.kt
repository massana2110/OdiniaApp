package com.womannotfound.odinia.viewmodel

import androidx.lifecycle.ViewModel
import com.womannotfound.odinia.views.ui.fragments.controls.adapters.ActivitiesItems

class ActivitiesViewModel : ViewModel(){
    var activityName: String = ""
    var activityDate: String = ""
    var activityAMount: String = ""
    var cardColor: Int = 0

    val list = ArrayList<ActivitiesItems>()
}