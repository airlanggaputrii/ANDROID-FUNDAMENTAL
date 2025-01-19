package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.api.FavoriteDao
import com.example.myapplication.dataclass.UserFavorite

@Database (entities = [UserFavorite::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteDao

    companion object {
        private var INSTANCES : FavoriteDatabase? = null

        fun getDatabase(context : Context) : FavoriteDatabase? {
            if (INSTANCES == null) {
                synchronized(FavoriteDatabase::class){
                    INSTANCES = Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, "user database").build()
                }
            }
            return INSTANCES
        }
    }
}