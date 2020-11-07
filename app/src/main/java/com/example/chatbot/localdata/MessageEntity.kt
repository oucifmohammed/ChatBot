package com.example.chatbot.localdata

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MessageEntity(
    val source: String,
    val destination: String,
    val message: String,
    val senderId: Int,
    val time: Date
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}