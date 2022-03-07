package com.example.e_buyer

import com.example.e_buyer.Constants.Contants
import com.example.e_buyer.`interface`.NotificationApi
import com.example.e_buyer.model.NotificationData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofitlnstance {

    companion object{
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Contants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }
        val api by lazy {
            retrofit.create(NotificationApi::class.java)
        }
    }
}