package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.dataclass.User
import com.example.myapplication.dataclass.UserResponse
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.preferences.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class MainViewModel (private val pref: SettingPreferences) : ViewModel() {

    private val _item = MutableLiveData<List<User>>()
    val item: MutableLiveData<List<User>> = _item

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    init {
        findUser(username = "q")
        searchUser(username = String())
    }

    private fun findUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.apiInstant.getUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    item.value = response.body()?.items
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchUser(username: String) {
        _isLoading.value = true
        ApiConfig.apiInstant
            .getUsers(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        item.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message.toString()}")
                }

            })
    }
    fun getSearchUser(): LiveData<List<User>> {
        return item
    }

    class MainFactory(private val pref: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(pref) as T
    }

    fun getThemeSetting() = pref.getThemeSetting().asLiveData()

}