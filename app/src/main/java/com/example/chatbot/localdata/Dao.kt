package com.example.chatbot.localdata

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface Dao {

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messageentity ORDER BY time ASC ")
    fun bringMessages(): LiveData<List<MessageEntity>>

    @Query("SELECT COUNT(*) FROM messageentity")
    suspend fun getTheSize(): Int
}