package com.example.chatbot.localdata

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MessageEntity::class], version = 1)
@TypeConverters(Converter::class)
abstract class ChatBotDataBase: RoomDatabase(){
    abstract fun chatDao(): Dao
}