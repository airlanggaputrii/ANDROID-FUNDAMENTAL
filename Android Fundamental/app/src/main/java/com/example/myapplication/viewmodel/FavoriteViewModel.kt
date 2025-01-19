package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.api.FavoriteDao
import com.example.myapplication.dataclass.UserFavorite
import com.example.myapplication.db.FavoriteDatabase

class FavoriteViewModel (app: Application): AndroidViewModel(app){

    private var userDao: FavoriteDao?
    private var userDb: FavoriteDatabase?

    init {
        userDb = FavoriteDatabase.getDatabase(app)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavUser(): LiveData<List<UserFavorite>>? {
        return userDao?.getFromFavorite()
    }
}