package com.womannotfound.odinia.views.ui.fragments.controls.adapters

class ActivitiesItems{
    var cardColor: String = "#985E6D"
        private set
    var activityName: String? = null
        private set
    var activityAmount: String? = null
        private set
    var activityDate: String? = null
        private set

    constructor() {
        //empty constructor needed
    }

    constructor(cardColor: String,activityName: String?, activityDate: String?, activityAmount: String?) {
        this.cardColor = cardColor
        this.activityName = activityName
        this.activityDate = activityDate
        this.activityAmount = activityAmount
    }
}