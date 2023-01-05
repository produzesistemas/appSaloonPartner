package com.produzesistemas.appsaloonpartner.model

data class Register(var email: String = "",
                    var secret: String = "",
                    var base64: String = "",
                    var cnpj: String = "",
                    var responsible: String = "",
                    var address: String = "",
                    var code: String = "",
                    var userName: String = "",
                    var description: String = "",
                    var name: String = "",
                    var typeId: Int = 0,
                    var appName: String = "",
                    var phoneNumber: String = "",
var scheduling: Boolean = false){
    constructor():this("","","","","","","","","","")

}
