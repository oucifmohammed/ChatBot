package com.example.chatbot.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatbot.localdata.MessageEntity
import com.example.chatbot.repositories.ChatRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel @ViewModelInject constructor(
    private val repository: ChatRepository
): ViewModel(){

    fun insert(message: MessageEntity) = viewModelScope.launch {
        repository.insertMessage(message)
    }

    fun bringAllMessages() = repository.bringAllMessages()

    suspend fun getSize() = withContext(viewModelScope.coroutineContext) {
        repository.getTheSize()
    }
}