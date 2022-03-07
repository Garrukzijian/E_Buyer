package com.example.e_buyer.`interface`

import com.example.e_buyer.Constants.Contants.Companion.CONTENT_TYPE
import com.example.e_buyer.Constants.Contants.Companion.SERVER_KEY
import com.example.e_buyer.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {
    @Headers("Authorzation:key=$SERVER_KEY","contant_type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification:PushNotification
    ):Response<ResponseBody>

}