package com.example.myapplication.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite")
data class UserFavorite (
    val login: String,
    @PrimaryKey
    val id : Int,
    val avatarURL : String
) : Serializable