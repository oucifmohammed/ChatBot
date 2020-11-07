package com.example.chatbot.di

import android.content.Context
import androidx.room.Room
import com.example.chatbot.localdata.ChatBotDataBase
import com.example.chatbot.other.Constants
import com.example.chatbot.other.Constants.DATABASENAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideChatDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ChatBotDataBase::class.java,
            DATABASENAME
        ).build()

    @Provides
    @Singleton
    fun provideChatDao(database: ChatBotDataBase) = database.chatDao()
}