package com.produzesistemas.appsaloonpartner.model

data class Register(var email: String = "",
                    var secret: String = "",
                    var base64: String = "",
                    var cnpj: String = "",
                    var responsible: String = "",
                    var address: String = "",
                    var establishmentName: String = "",
                    var idType: Int = 0,
                    var appName: String = "",
                    var telephone: String = ""){
    constructor():this("","","","","","","",0,"","")

}
