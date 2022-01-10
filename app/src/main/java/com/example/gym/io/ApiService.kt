package com.example.gym.io

import android.graphics.Bitmap
import com.example.gym.io.response.*
import com.example.gym.models.Body
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
import okhttp3.RequestBody

import okhttp3.MultipartBody

import okhttp3.ResponseBody

import retrofit2.http.POST

import retrofit2.http.Multipart
import java.io.File


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

    @GET("chart/store")
    fun storeStats(@Header("Authorization") authHeader: String,
                   @Query("day") date: String,
                   @Query("weight") weight: String,
                   @Query("body_fat") bodyFat: String): Call<Void>

    @GET("chart/goal/store")
    fun storeGoal(@Header("Authorization") authHeader: String,
                  @Query("goal_weight") goalWeight: String,
                  @Query("goal_body_fat") goalBodyFat: String): Call<Void>

    @GET("chart/goal/show")
    fun getGoal(@Header("Authorization") authHeader: String): Call<GoalResponse>

    @GET("chart/list")
    fun getBodyData(@Header("Authorization") authHeader: String): Call<ArrayList<Body>>

    @POST("chart/destroy/{body}")
    fun removeBody(@Header("Authorization") authHeader: String,
                   @Path("body") bodyId: Int): Call<ArrayList<Body>>

    @Multipart
    @POST("image")
    fun uploadImage(@Header("Authorization") authHeader: String,
                    @Part photo: MultipartBody.Part,
                    @Query("name") name:String): Call<Void>

    @POST("users/password")
    fun updatePassword(@Header("Authorization") authHeader: String,
                      @Query("current_password") currentPassword: String,
                      @Query("new_password") newPassword: String,
                      @Query("confirm_password") confirmPassword: String): Call<PasswordResponse>


    companion object Factory{
        private const val BASE_URL ="http://90.69.39.12/gym/public/api/"

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