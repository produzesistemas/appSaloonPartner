package com.produzesistemas.appsaloonpartner.repository

import com.produzesistemas.appsaloonpartner.model.Establishment
import com.produzesistemas.appsaloonpartner.model.LoginUser
import com.produzesistemas.appsaloonpartner.model.Register
import com.produzesistemas.appsaloonpartner.model.Token
import com.produzesistemas.appsaloonpartner.retrofit.RetrofitService
import com.produzesistemas.appsaloonpartner.utils.NetworkState


class LoginRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun login(email: String, secret: String) : NetworkState<Token?> {
        val loginUser = LoginUser(email, secret, "AppBeauty")
        val response = retrofitService.login(loginUser)
        return if (response.isSuccessful) {
            if (response.code() == 200) {
                NetworkState.Success(response.body())
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun register(register: Register) : NetworkState<String?> {
        val response = retrofitService.registerUser(register)
        return if (response.isSuccessful) {
            if (response.code() == 200) {
                NetworkState.Success(response.body())
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun forgot(email: String, secret: String) : NetworkState<String?> {
        val loginUser = LoginUser(email, secret, "AppBeauty")
        val response = retrofitService.forgotPassword(loginUser)
        return if (response.isSuccessful) {
            if (response.code() == 200) {
                NetworkState.Success(response.body())
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

    suspend fun getMyAccount(token: String) : NetworkState<Establishment> {
        val response = retrofitService.getMyAccount(token)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }

}