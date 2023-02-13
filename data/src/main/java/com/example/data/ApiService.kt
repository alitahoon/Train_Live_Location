package com.example.data

import com.example.domain.entity.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/user/UserLogin")
    suspend fun getUserData(
        @Query("phone") userPhone: String, @Query("password") userPassword: String
    ): Response<ArrayList<userResponseItem>>

    @POST("api/user/CreateUser")
    suspend fun addNewUser(@Body user: RegisterUser?): Response<ArrayList<userResponseItem>>




    //Admins
    @GET("/api/Admin/GetAdmins")
    suspend fun GetAdmins():Response<Admin>

    @GET("/api/Admin/GetLoginAdmin/{phone}/{password}")
    suspend fun GetLoginAdmin(@Path ("phone") adminPhone:String, @Path("password")  adminPass:String):Response<Admin>

    @GET("/api/Admin/GetAdmin/{id}")
    suspend fun GetAdmin(@Path("id") adminId:Int):Response<Admin>

    @POST("/api/Admin/CreateAdmin")
    suspend fun CreateAdmin():Response<Admin>

    @DELETE("/api/Admin/DeleteAdmin/{Id}")
    suspend fun DeleteAdmin(@Path("Id") adminId:Int):Response<Admin>

    @PUT("/api/Admin/UpdateAdmin/{Id}")
    suspend fun UpdateAdmin(@Path("Id") adminId: Int):Response<Admin>


    //Comments
    @GET("/api/Comment/GetAllComments")
    suspend fun GetAllComments():Response<Comment>

    @GET("/api/Comment/GetComment/{id}")
    suspend fun GetComment(@Path("id") commentId:Int):Response<Comment>

    @POST("/api/Comment/CreateComment")
    suspend fun CreateComment():Response<Comment>

    @DELETE("/api/Comment/DeleteComment/{Id}")
    suspend fun DeleteComment(@Path("Id") commentId: Int):Response<Comment>

    @PUT("/api/Comment/UpdateComment/{Id}")
    suspend fun UpdateComment(@Path("Id") commentId: Int):Response<UpComment>

    //Payment
    @GET("/api/Payment/GetPayments")
    suspend fun GetPayments():Response<Payment>

    @GET("/api/Payment/GetPayment/{id}")
    suspend fun GetPayment(@Path("id") payId: Int):Response<Payment>

    @POST("/api/Payment/createPayment")
    suspend fun createPayment():Response<Payment>

    @PUT("/api/Payment/UpdatePayment/{Id}")
    suspend fun UpdatePayment(@Path("Id") payId: Int):Response<Payment>

    @DELETE("/api/Payment/DeletePayment/{Id}")
    suspend fun DeletePayment(@Path("Id") payId: Int):Response<Payment>

    //Post
    @GET("/api/Post/GetAllPosts")
    suspend fun GetAllPosts():Response<Post>

    @GET("/api/Post/GetPost/{id}")
    suspend fun GetPost(@Path("id") postId: Int):Response<Post>

    @POST("/api/Post/CreatePost")
    suspend fun CreatePost():Response<Post>

    @DELETE("/api/Post/DeletePost/{Id}")
    suspend fun DeletePost(@Path("Id") postId: Int):Response<Post>

    @PUT("/api/Post/UpdatePost/{Id}")
    suspend fun UpdatePost(@Path("Id") postId: Int):Response<Post>


    //Station
    @GET("/api/station/GetAllStation")
    suspend fun GetAllStation():Response<Station>

    @GET("/api/station/GetStationsForOneTrain")
    suspend fun GetStationsForOneTrain(@Query("trainId") trainId:Int):Response<Station>

    @GET("/api/station/GetStationById")
    suspend fun GetStationById(@Query("Id") Id: Int):Response<Station>

    @POST("/api/station/CreateStation")
    suspend fun CreateStation():Response<Station>

    @DELETE("/api/station/DeleteStation")
    suspend fun DeleteStation(@Query("Id") Id: Int):Response<Station>

    @PUT("/api/station/UpdateStation")
    suspend fun UpdateStation(@Query("StationId") StationId: Int):Response<Station>

    //Ticket
    @GET("/api/ticket/GetTicketById")
    suspend fun GetTicketById(@Query("Id") Id: Int):Response<Ticket>

    @POST("/api/ticket/CreateTicket")
    suspend fun CreateTicket():Response<Ticket>

    @PUT("/api/ticket/ScanTicket")
    suspend fun ScanTicket(@Query("ticketId") ticketId:Int):Response<ScanTicket>

    @GET("/api/ticket/CheckIfScannOrNot")
    suspend fun CheckIfScannOrNot(@Query("ticketId")ticketId:Int):Response<ScanTicket>












}