package com.aperfectpolygon.smartknee.database.user

import com.aperfectpolygon.smartknee.helper.ObjectBox
import com.aperfectpolygon.smartknee.model.user.User
import io.objectbox.Box

object UserRepository : OnUserRepository {

	private val mBox: Box<User> by lazy { ObjectBox.boxStore.boxFor(User::class.java) }

	override val isUserEmpty: Boolean
		get() = mBox.isEmpty

	override val username: String
		get() = user.username ?: ""

	override var avatar: String
		get() = user.avatar ?: ""
		set(value) {
			user.apply {
				avatar = value
				user = this
			}
		}

	override var user: User
		get() = mBox.query().build().findFirst() ?: User()
		set(value) = with(mBox) {
			removeUsers()
			put(value)
		}

	override fun removeUsers() = mBox.removeAll()
	override fun removeUser(id: Long): Boolean = mBox.remove(id)
}