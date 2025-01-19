package com.example.myapplication.viewmodel

import androidx.lifecycle.*
import com.example.myapplication.preferences.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel (private val pref: SettingPreferences): ViewModel(){

    @Suppress("UNCHECKED_CAST")
    class SettingFactory(private val pref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T  = SettingViewModel(pref) as T

    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getThemeSettings() =  pref.getThemeSetting().asLiveData()

    fun saveThemeSettings(isDarkModeActive : Boolean){
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}