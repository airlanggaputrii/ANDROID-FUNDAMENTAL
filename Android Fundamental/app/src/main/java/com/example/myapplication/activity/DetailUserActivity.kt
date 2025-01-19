package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.adapter.SectionPagerAdapter
import com.example.myapplication.viewmodel.DetailViewModel
import com.example.myapplication.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var viewModel : DetailViewModel
    private lateinit var detailBinding : ActivityDetailUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val intentID = intent.getIntExtra(EXTRA_ID, 0)
        val avatarURL = intent.getStringExtra(EXTRA_URL)
        var isChecked = false
        val bundle = Bundle()
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)

        bundle.putString(EXTRA_USERNAME, username)

        supportActionBar?.apply {
            title = getString(R.string.DetailActionBar)
        }

        detailBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_detail_user)
        setContentView(detailBinding.root)

        detailBinding.apply {
            viewPager.adapter = sectionPagerAdapter
            table.setupWithViewPager(viewPager)
        }

        supportActionBar?.apply{
            title = getString(R.string.DetailActionBar)
        }

        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        showProgressBar(true)

        viewModel.detailUser(username.toString())
        viewModel.getDetailUser().observe(this){

            if (it != null){
                detailBinding.apply {
                    tvNameDetail.text = it.name
                    tvDetailUsername.text = it.login
                    Followers.text = "${it.followers}" + " followers"
                    Following.text = "${it.following}" + " following"
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .into(ivUserDetailAvatar)
                    showProgressBar(false)
                }
            }

        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkForFavorite(intentID)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0){
                        detailBinding.btnfav.isChecked = true
                        isChecked = true
                    } else {
                        detailBinding.btnfav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        detailBinding.btnfav.setOnClickListener{
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addToFavorite(
                    username = username.toString(),
                    id = intentID,
                    avatarURL = avatarURL.toString())
            } else {
                viewModel.removeFromFavorite(intentID)
            }
            detailBinding.btnfav.isClickable
        }

        detailBinding.apply {
            viewPager.adapter = sectionPagerAdapter
            table.setupWithViewPager(viewPager)
        }

    }

    private fun showProgressBar(isLoading: Boolean) {
        detailBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object{
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_ID = "extra_id"
    }
}