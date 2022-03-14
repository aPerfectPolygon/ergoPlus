package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.model.user.User

interface OnUserRepository {

	val isUserEmpty: Boolean
	val username: String
	val token: String
	val firstName: String
	val lastName: String
	val fullName: String
	val phoneNumber: String
	var avatar: String
	var user: User
	val limitMarkets: List<String>

	fun getUserByID(id: Long): User
	fun removeUser(id: Long): Boolean
	fun removeUsers()
}