package com.example.chatbot.repositories

import com.example.chatbot.localdata.Dao
import com.example.chatbot.localdata.MessageEntity
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: Dao
){

    suspend fun insertMessage(message: MessageEntity) = chatDao.insertMessage(message)

    fun bringAllMessages() = chatDao.bringMessages()

    suspend fun getTheSize() = chatDao.getTheSize()
}