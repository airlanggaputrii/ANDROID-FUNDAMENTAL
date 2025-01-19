package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.dataclass.User
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.preferences.SettingPreferences
import com.example.myapplication.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.MainFactory(SettingPreferences(this))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallback{
            override fun onItemClick(Data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_ID, Data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, Data.avatar_url)
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME,Data.login)
                    startActivity(it)
                }
            }
        })

        binding.apply {

            recycleGithuser.layoutManager = LinearLayoutManager(this@MainActivity)
            recycleGithuser.setHasFixedSize(true)
            recycleGithuser.adapter = adapter

            btnSearch.setOnClickListener{
                searchUser()

            }
            inputSearch.setOnKeyListener{ _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false

            }
        }

        viewModel.isLoading.observe(this@MainActivity){
            showProgressBar(it)
        }

        viewModel.getThemeSetting().observe(this@MainActivity){
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.getSearchUser().observe(this@MainActivity) {
            if (it != null) {
                adapter.setItem(it as ArrayList<User>)
                showProgressBar(false)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_favorite -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_setting -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
    private fun searchUser(){
        binding.apply {
            val query = inputSearch.text.toString()
            if (query.isEmpty()) return
            showProgressBar(true)
            viewModel.searchUser(query)
        }
    }
}
