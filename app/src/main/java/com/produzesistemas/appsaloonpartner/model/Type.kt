package com.produzesistemas.appsaloonpartner.model

data class Type (
        var description: String = "",
        var id: Int = 0) {
    constructor():this("",0)

    override fun toString(): String {
        return description
    }
}
