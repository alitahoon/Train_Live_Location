package com.example.data

import android.database.Observable
import com.example.domain.entity.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    //Admins
    @GET("/api/Admin/GetAdmins")
    suspend fun GetAdmins(): Response<List<Admin>>

    @GET("/api/Admin/GetLoginAdmin/{phone}/{password}")
    suspend fun GetLoginAdmin(
        @Path("phone") adminPhone: String,
        @Path("password") adminPass: String
    ): Response<Admin>

    @GET("/api/Admin/GetAdmin/{id}")
    suspend fun GetAdmin(@Path("id") adminId: Int): Response<Admin>

    @POST("/api/Admin/CreateAdmin")
    suspend fun CreateAdmin(): Response<Admin>

    @DELETE("/api/Admin/DeleteAdmin/{Id}")
    suspend fun DeleteAdmin(@Path("Id") adminId: Int): Response<Admin>

    @PUT("/api/Admin/UpdateAdmin/{Id}")
    suspend fun UpdateAdmin(@Path("Id") adminId: Int): Response<Admin>


    //Comments
    @GET("/api/Comment/GetCommentsForPost/{PostId}")
    suspend fun GetPostComments(@Path("PostId") postId:Int?): Response<ArrayList<PostCommentsResponseItem>>

    @GET("/api/Comment/GetComment/{id}")
    suspend fun GetComment(@Path("id") commentId: Int): Response<Comment>

    @POST("/api/Comment/CreateComment")
    suspend fun CreateComment(@Body commentRequest: CommentRequest): Response<CommentResponse>

    @DELETE("/api/Comment/DeleteComment/{Id}")
    suspend fun DeleteComment(@Path("Id") commentId: Int): Response<Comment>



    //Payment
    @GET("/api/Payment/GetPayments")
    suspend fun GetPayments(): Response<List<Payment>>

    @GET("/api/Payment/GetPayment/{id}")
    suspend fun GetPayment(@Path("id") payId: Int): Response<Payment>

    @POST("/api/Payment/createPayment")
    suspend fun createPayment(): Response<Payment>

    @PUT("/api/Payment/UpdatePayment/{Id}")
    suspend fun UpdatePayment(@Path("Id") payId: Int): Response<UpPayment>

    @DELETE("/api/Payment/DeletePayment/{Id}")
    suspend fun DeletePayment(@Path("Id") payId: Int): Response<Payment>

    //Post
    @GET("/api/Post/GetAllPosts")
    suspend fun GetAllPosts(): Response<ArrayList<PostModelResponse>>

    @GET("/api/Post/GetPost/{id}")
    suspend fun GetPost(@Path("id") postId: Int): Response<Post>

    @POST("/api/Post/CreatePost")
    suspend fun CreatePost(@Body post: Post): Response<PostModelResponse>

    @DELETE("/api/Post/DeletePost/{Id}")
    suspend fun DeletePost(@Path("Id") postId: Int):Response<ResponseBody>

    @PUT("/api/Post/UpdatePost/{Id}")
    suspend fun UpdatePost(@Path("Id") postId: Int): Response<UpPost>


    //Station
    @GET("/api/station/GetAllStation")
    suspend fun GetAllStation(): Response<ArrayList<StationResponseItem>>

    @GET("/api/station/GetStationById")
    suspend fun GetStationById(@Query("Id") staionID:Int): Response<StationResponseItem>

    //Trains
    @GET("/api/train/GetTrains")
    suspend fun GetAllTrains(): Response<ArrayList<TrainResponseItem>>

//    @GET("/api/station/GetStationsForOneTrain")
//    suspend fun GetStationsForOneTrain(@Query("trainId") trainId:Int):Response<List<Station>>
//
//    @GET("/api/station/GetStationById")
//    suspend fun GetStationById(@Query("Id") StationId: Int):Response<Station>
//
//    @POST("/api/station/CreateStation")
//    suspend fun CreateStation():Response<Station>
//
//    @DELETE("/api/station/DeleteStation")
//    suspend fun DeleteStation(@Query("Id") StationId: Int):Response<Station>
//
//    @PUT("/api/station/UpdateStation")
//    suspend fun UpdateStation(@Query("StationId") StationId: Int):Response<Station>

    //Ticket
    @GET("/api/ticket/GetTicketById")
    suspend fun GetTicketById(@Query("Id") ticketId: Int): Response<TicketRequestItem>

    @POST("/api/ticket/CreateTicket")
    suspend fun CreateTicket(@Body ticketRequestItem: TicketRequestItem): Response<TicketResponseItem>

    @PUT("/api/ticket/ScanTicket")
    suspend fun ScanTicket(@Query("ticketId") ticketId: Int): Response<ScanTicket>

    @GET("/api/ticket/CheckIfScannOrNot")
    suspend fun CheckIfScannOrNot(@Query("ticketId") ticketId: Int): Response<ScanTicket>


    //Train
    @GET("/api/train/GetAllConductor")
    suspend fun GetAllConductor()

    @GET("/api/train/GetAllDriver")
    suspend fun GetAllDriver()

    @GET("/api/train/GetTrains")
    suspend fun GetTrains(): Response<List<Train>>

    @GET("/api/train/GetTrainById")
    suspend fun GetTrainById(@Query("Id") trainId: Int): Response<Train>

    @POST("/api/train/CreateTrain")
    suspend fun CreateTrain(): Response<Train>

    @DELETE("/api/train/DeleteTrain")
    suspend fun DeleteTrain(@Query("Id") trainId: Int): Response<Train>

    @PUT("/api/train/UpdateTrain")
    suspend fun UpdateTrain(@Query("trainId") trainId: Int): Response<Train>

    //User

    @GET("/api/user/GetUsers")
    suspend fun GetUsers(): Response<List<UserResponseItem>>

    @GET("/api/user/GetUserById")
    suspend fun GetUserById(@Query("Id") userId: Int): Response<UserResponseItem>

    @GET("api/user/UserLogin")
    suspend fun getUserData(
        @Query("phone") userPhone: String, @Query("password") userPassword: String
    ): Response<UserResponseItem>

    @POST("api/user/CreateUser")
    suspend fun addNewUser(@Body user: RegisterUser?): Response<UserResponseItem>

    @DELETE("/api/user/DeleteUser")
    suspend fun DeleteUser(@Query("Id") userId: Int): Response<UserResponseItem>

    @PUT("/api/user/UpdateUser")
    suspend fun UpdateUser(@Body userRequest: RegisterUser,@Query("userId") userId:Int): Response<ResponseBody>


    //location
    @POST("/api/LiveLocation/CreateLocation")
    suspend fun AddLocation(@Body location_Request: Location_Request): Response<Location_Request_with_id>

    @GET("/api/LiveLocation/GetLocation")
    suspend fun GetLocation(@Query("TrainId") trainid: Int): Response<Location_Response>

    @GET("/api/user/DoctorsInTrain")
    suspend fun GetDoctors(@Query("trainId") trainId:Int): Response<ArrayList<DoctorResponseItem>>

    @GET("/api/user/UserInStation")
    suspend fun GetUserNotificationInStation(@Query("stationId") stationId:Int): Response<ArrayList<StationUsersNotificationResponseItem>>

    @GET("/api/user/UsersInTrain")
    suspend fun GetUserInTrain(@Query("trainId") trainId:Int): Response<ArrayList<UserInTrainResponseItem>>

    @GET("/api/news/GetNews")
    suspend fun GetNews(): Response<ArrayList<GetNewsResponseItem>>

    @POST("/api/news/CreateNews")
    suspend fun CreateNews(): Response<CreateNewsResponseItem>

    @GET("/api/news/GetNewsById")
    suspend fun GetNewsById(@Query("newsId") newsId:Int): Response<GetNewsByIdResponseItem>
    @GET("/api/user/GetUserTokenById")
    suspend fun GetUserTokenById(@Query("id") trainId:Int): Response<NotificationTokenResponse>

    @GET("/api/user/UsersTokenInTrain?TrainId")
    suspend fun GetUsersTokenInTrain(@Query("id") trainId:Int): Response<ArrayList<NotificationTokenResponse>>
}