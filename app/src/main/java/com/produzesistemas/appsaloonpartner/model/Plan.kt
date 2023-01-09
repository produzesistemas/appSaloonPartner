package com.produzesistemas.appsaloonpartner.model

data class Plan (
    var description: String = "",
    var days: Int = 0,
    var value: Double = 0.00,
    var id: Int = 0,
    var active: Boolean) {
    override fun toString(): String {
        return description
    }
}
