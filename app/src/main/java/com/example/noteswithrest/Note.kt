package com.example.noteswithrest

import android.os.Message
import io.reactivex.internal.operators.single.SingleDoOnSuccess

data class Note(
    val id:Int,
    val title:String,
    val content:String,
    val time:String
)
data class ResponceModel(
    val success: Boolean,
    val message: String,
    val note:Note? = null

)
