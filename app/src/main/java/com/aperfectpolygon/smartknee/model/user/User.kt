package com.aperfectpolygon.smartknee.model.user

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class User(
	@Id var id: Long = 0,
	val username: String = "",
	val birthDate: Long = 0,
	val email: String = "",
	var avatar: String = ""
)