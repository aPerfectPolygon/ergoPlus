package com.aperfectpolygon.smartknee.model.user

import com.google.gson.annotations.Expose
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class User(
	@Id var id: Long = 0,
	@Expose var username: String = "",
	@Expose var birthDate: String = "",
	@Expose var email: String = "",
	@Expose var avatar: String = ""
)