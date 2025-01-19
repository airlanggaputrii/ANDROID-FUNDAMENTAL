package com.example.myapplication.api

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.dataclass.UserFavorite

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addToFavorite(
        userFavoriteDao: UserFavorite
    )

    @Query("SELECT * FROM favorite")
    fun getFromFavorite():LiveData<List<UserFavorite>>

    @Query("SELECT count (*) FROM favorite WHERE favorite.id = :id")
    suspend fun checkFromFavorite(id: Int) : Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    suspend fun removeUserFromFavorite(id: Int): Int
}