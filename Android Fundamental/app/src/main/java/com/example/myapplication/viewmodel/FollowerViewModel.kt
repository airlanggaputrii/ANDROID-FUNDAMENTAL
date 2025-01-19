package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.dataclass.User
import com.example.myapplication.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModel : ViewModel(){

    val ListFollower = MutableLiveData<List<User>>()

    private val _isLoading = MutableLiveData<Boolean>()

    fun listFollower (username : String) { //penamaan camelcase
        _isLoading.value = true
        ApiConfig.apiInstant
            .getFollowers(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        ListFollower.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "OnFailure : ${t.message.toString()}")
                }
            })
    }

    fun getListFollower() : LiveData<List<User>>{
        return ListFollower
    }

    companion object{
        private const val TAG = "Follower ViewModel"
    }
}