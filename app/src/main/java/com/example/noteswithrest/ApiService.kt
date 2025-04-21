package com.example.noteswithrest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("insertnote.php")
    fun insertnote(
        @Field("title") title:String,
        @Field("content") content:String,
        @Field("time") time:String
    ):Call<ResponceModel>

    @GET("selectnotes.php")
    fun getallnotes():Call<List<Note>>

    @FormUrlEncoded
    @POST("updatenotes.php")
    fun updatenotes(
        @Field("id") id:Int,
        @Field("title") title:String,
        @Field("content") content:String,
        @Field("time") time:String
    ):Call<ResponceModel>

    @FormUrlEncoded
    @POST("deletenote.php")
    fun deletenote(
        @Field("id") id:Int
    ):Call<ResponceModel>
}