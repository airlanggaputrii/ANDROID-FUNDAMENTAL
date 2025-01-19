package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.dataclass.DetailResponse
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.api.FavoriteDao
import com.example.myapplication.dataclass.UserFavorite
import com.example.myapplication.db.FavoriteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (app : Application) : AndroidViewModel(app) {

    private val _isLoading = MutableLiveData<Boolean>()
    private val userDao : FavoriteDao?
    private val userDB : FavoriteDatabase?
    val user = MutableLiveData<DetailResponse>()

    init {
        userDB = FavoriteDatabase.getDatabase(app)
        userDao = userDB?.favoriteUserDao()
    }

    fun detailUser(username : String){
        _isLoading.value = true
        ApiConfig.apiInstant
            .getUsersDetail(username)
            .enqueue(object : Callback<DetailResponse>{

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure : ${t.message.toString()}")
                }

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        user.postValue(response.body())
                    }
                }

            })
    }

    fun getDetailUser() : LiveData<DetailResponse>{
        return user
    }

    fun addToFavorite (username : String, id : Int, avatarURL : String){
        CoroutineScope(Dispatchers.IO).launch {
            val user = UserFavorite(
                username,
                id,
                avatarURL)
            userDao?.addToFavorite(user)
        }
    }

    suspend fun checkForFavorite(id: Int) = userDao?.checkFromFavorite(id)

    fun removeFromFavorite(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeUserFromFavorite(id)
        }
    }

    companion object{
        private const val TAG = "Detail ViewModel"
    }
}