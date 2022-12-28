package com.produzesistemas.appsaloonpartner.model

data class LoginUser(val email: String,
                     val secret: String,
                     val appName: String,
)