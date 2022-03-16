package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.helper.ObjectBox
import com.aperfectpolygon.smartknee.model.user.User
import io.objectbox.Box

object UserRepository : OnUserRepository {

	private val mBox: Box<User> by lazy { ObjectBox.boxStore.boxFor(User::class.java) }

	override val isUserEmpty: Boolean
		get() = mBox.isEmpty

	override var user: User
		get() = mBox.query().build().findFirst() ?: User()
		set(value) = with(mBox) {
			removeUsers()
			put(value)
		}
	override var username: String
		get() = user.username
		set(value) {
			user.apply { username = value }.also { user = it }
		}
	override var birthDate: String
		get() = user.birthDate
		set(value) {
			user.apply { birthDate = value }.also { user = it }
		}
	override var email: String
		get() = user.email
		set(value) {
			user.apply { email = value }.also { user = it }
		}
	override var avatar: String
		get() = user.avatar
		set(value) {
			user.apply { avatar = value }.also { user = it }
		}

	override fun removeUsers() = mBox.removeAll()
	override fun removeUser(id: Long): Boolean = mBox.remove(id)
}