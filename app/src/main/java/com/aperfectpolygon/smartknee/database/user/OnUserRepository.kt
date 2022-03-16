package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.model.user.User

interface OnUserRepository {

	val isUserEmpty: Boolean
	var user: User
	var username: String
	var birthDate: String
	var email: String
	var avatar: String
	fun removeUser(id: Long): Boolean
	fun removeUsers()
}