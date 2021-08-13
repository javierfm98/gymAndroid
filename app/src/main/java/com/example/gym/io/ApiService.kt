package com.example.gym.io

import com.example.gym.io.response.CheckResponse
import com.example.gym.io.response.LoginResponse
import com.example.gym.models.Training
import com.example.gym.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    fun postLogin(@Query("email") email: String , @Query("password") password: String): Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    @GET("trainings")
    fun getTrainings(@Query("day") day: String): Call<ArrayList<Training>>

    @POST("trainings")
    fun reservation(@Header("Authorization") authHeader: String,
                    @Query("training") trainingId: Int): Call<Void>

    @GET("trainings/reserves")
    fun getReserves(@Header("Authorization") authHeader: String): Call<ArrayList<Training>>

    @GET("trainings/check")
    fun checkReservation(@Header("Authorization") authHeader: String,
                         @Query("date") date: String): Call<CheckResponse>

    @GET("user")
    fun getUser(@Header("Authorization") authHeader: String): Call<User>

    companion object Factory{
        private const val BASE_URL ="http://64.225.72.59/api/"

        fun create(): ApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client= OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}