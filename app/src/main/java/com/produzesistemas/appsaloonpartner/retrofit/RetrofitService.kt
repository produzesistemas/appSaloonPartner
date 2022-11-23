package com.produzesistemas.appsaloonpartner.retrofit
import com.google.gson.GsonBuilder
import com.produzesistemas.appsaloonpartner.model.LoginUser
import com.produzesistemas.appsaloonpartner.model.Token
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface RetrofitService {

    @Headers("Content-Type:application/json")
    @POST("account/loginVendasNow")
    suspend fun login(@Body loginUser: LoginUser): Response<Token>

    @Headers("Content-Type:application/json")
    @POST("account/register")
    suspend fun registerUser(@Body loginUser: LoginUser): Response<String>

    @Headers("Content-Type:application/json")
    @POST("account/recoverPassword")
    suspend fun forgotPassword(@Body loginUser: LoginUser): Response<String>


    companion object {
        private const val BASE_URL: String = "https://produzesistemas.com.br/api/"

        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create()

                val gsonConverterFactory = GsonConverterFactory.create(gson)
                val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }

                val client: OkHttpClient = OkHttpClient.Builder().apply {
                    this.addInterceptor(interceptor)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                }.build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(gsonConverterFactory)
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}