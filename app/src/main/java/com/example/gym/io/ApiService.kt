package com.example.gym.io

import com.example.gym.io.response.ChartResponse
import com.example.gym.io.response.CheckResponse
import com.example.gym.io.response.LoginResponse
import com.example.gym.io.response.TrainingDetailsResponse
import com.example.gym.models.Subscription
import com.example.gym.models.Training
import com.example.gym.models.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import retrofit2.http.*


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

    @POST("trainings/{training}")
    fun removeReservation(@Header("Authorization") authHeader: String,
                          @Path("training") trainingId: Int): Call<ArrayList<Training>>

    @GET("trainings/{training}")
    fun showTraining(@Header("Authorization") authHeader: String,
                     @Path("training") trainingId: Int): Call<TrainingDetailsResponse>

    @POST("users/update")
    fun updateProfile(@Header("Authorization") authHeader: String,
                      @Query("name") name: String,
                      @Query("surname") surname: String,
                      @Query("phone") phone: String,
                      @Query("username") username: String,
                      @Query("email") email: String): Call<User>

    @GET("users/subscription")
    fun getSubs(@Header("Authorization") authHeader: String): Call<ArrayList<Subscription>>

    @GET("chart")
    fun getDataChart(@Header("Authorization") authHeader: String): Call<ChartResponse>


    companion object Factory{
        private const val BASE_URL ="http://64.225.72.59/api/"

        fun create(): ApiService {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client= OkHttpClient.Builder().addInterceptor(interceptor).build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}