package com.example.myapplication.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.dataclass.User
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.databinding.ActivityFavoriteBinding
import com.example.myapplication.dataclass.UserFavorite
import com.example.myapplication.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapter : UserAdapter
    private lateinit var viewModel : FavoriteViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            title = getString(R.string.favorite)
        }

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallback {
            override fun onItemClick(Data: User) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, Data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, Data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, Data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            recycleGithuser.setHasFixedSize(true)
            recycleGithuser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            recycleGithuser.adapter = adapter
        }

        showProgressBar(true)
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        viewModel.getFavUser()?.observe(this){
            if (it!=null) {
                val list = favList(it)
                adapter.setItem(list)
            }
            showProgressBar(false)
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.ProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun favList(users: List<UserFavorite>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userList = User(
                login = user.login,
                avatar_url = user.avatarURL,
                id = user.id
            )
            listUsers.add(userList)
        }
        return listUsers
    }
}