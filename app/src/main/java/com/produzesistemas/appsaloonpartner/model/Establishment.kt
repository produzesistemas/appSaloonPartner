package com.produzesistemas.appsaloonpartner.model

data class Establishment(
    var typeId: Int = 0,
    var name: String = "",
    var responsible: String = "",
    var cnpj: String = "",
    var description: String = "",
    var scheduling: Boolean = false,
    var address: String = "",
    var id: Int = 0) {

    override fun toString(): String {
        return name
    }
}
