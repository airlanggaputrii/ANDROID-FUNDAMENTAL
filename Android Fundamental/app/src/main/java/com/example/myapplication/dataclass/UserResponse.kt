package com.example.myapplication.dataclass

data class UserResponse(
	val items: List<User>
)

data class User(
	val login: String,
	val id: Int,
	val avatar_url: String
)

data class DetailResponse(
	val login : String,
	val id : Int,
	val avatar_url : String,
	val followers_url : String,
	val following_url : String,
	val name : String,
	val following : Int,
	val followers : Int
)
