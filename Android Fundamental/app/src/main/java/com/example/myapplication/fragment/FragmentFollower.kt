package com.example.myapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.dataclass.User
import com.example.myapplication.viewmodel.FollowerViewModel
import com.example.myapplication.activity.DetailUserActivity
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.databinding.FragmentFollowerBinding

class FragmentFollower : Fragment(R.layout.fragment_follower) {

    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowerViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowerBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        adapter = UserAdapter()

        adapter.notifyDataSetChanged()

        binding.apply {
            recycleFollow.setHasFixedSize(true)
            recycleFollow.layoutManager = LinearLayoutManager(activity)
            recycleFollow.adapter = adapter
        }

        showProgressBar(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowerViewModel::class.java]
        viewModel.listFollower(username)
        viewModel.getListFollower().observe(viewLifecycleOwner){
            if (it != null){
                adapter.setItem(it as ArrayList<User>)
                showProgressBar(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}