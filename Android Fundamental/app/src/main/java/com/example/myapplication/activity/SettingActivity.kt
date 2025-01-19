package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySettingBinding
import com.example.myapplication.preferences.SettingPreferences
import com.example.myapplication.viewmodel.SettingViewModel

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel by viewModels<SettingViewModel> {
        SettingViewModel.SettingFactory(SettingPreferences(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            title = this@SettingActivity.getString(R.string.setting)
        }

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getThemeSettings().observe(this@SettingActivity){
            if (it) {
                binding.switchTheme.text = resources.getString(R.string.dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }  else {
                binding.switchTheme.text = resources.getString(R.string.light_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding.switchTheme.isChecked = it
            showProgressBar(false)
        }

        viewModel.getThemeSettings()?.observe(this) {
            if (it) {
                binding.switchTheme.text = this@SettingActivity.getString(R.string.dark_mode)
            } else {
                binding.switchTheme.text = this@SettingActivity.getString(R.string.light_mode)
            }
            binding.switchTheme.isChecked = it
        }

        viewModel.isLoading.observe(this) {
            showProgressBar(it)
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSettings(isChecked)
        }

        showProgressBar(true)
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}