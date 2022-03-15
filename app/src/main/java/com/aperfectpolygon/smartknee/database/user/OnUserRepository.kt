package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.model.user.User

interface OnUserRepository {

	val isUserEmpty: Boolean
	var user: User
	fun removeUser(id: Long): Boolean
	fun removeUsers()
}