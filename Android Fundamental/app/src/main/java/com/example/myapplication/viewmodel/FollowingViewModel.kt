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

class FollowingViewModel : ViewModel(){

    companion object{
        private const val TAG = "Following ViewModel"
    }

    val ListFollowing = MutableLiveData<List<User>>()

    private val _isLoading = MutableLiveData<Boolean>()

    fun ListFollowing(username: String){ //penamaan camelcase
        _isLoading.value = true
        ApiConfig.apiInstant
            .getFollowing(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        ListFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "OnFailure : ${t.message.toString()}")
                }
            })
    }

    fun getListFollowing() : LiveData<List<User>> {
        return ListFollowing
    }
}