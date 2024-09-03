package com.algostack.nir.services.api
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.services.model.userPostSoldFieldUpdate
import com.algostack.nir.services.model.userUpdateRequestResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PublicPostApi {
    @GET("/all-posts")
    suspend fun getPublicPost() : Response<PublicPostResponse>

    @GET("/nearest-posts/{place}")
    suspend fun getNearestPost(@Path("place") place: String) : Response<PublicPostResponse>

//    @GET("/single-user/:id")
//    suspend fun getSingleUserPost() : Response<PublicPostResponse>
    @POST ("/create-post")
    suspend fun createPost(@Body createPost: CreatePost) : Response<CreatePostResponse>

    @PUT("/update-post/{id}")
    suspend fun updateSoldFiled(
        @Path("id") _id: String,
        @Body userPostSoldFieldUpdate : userPostSoldFieldUpdate
    ) : Response<userUpdateRequestResponse>

    @Multipart
    @POST("/upload")
    suspend fun uploadImage(@Part files: List<MultipartBody.Part>): Response<UploadImageResponse>


}