package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.model.user.User

interface OnUserRepository {

	val isUserEmpty: Boolean
	val username: String
	var avatar: String
	var user: User
	fun removeUser(id: Long): Boolean
	fun removeUsers()
}