package com.aperfectpolygon.ergoplus.helper.api.signin

import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractPresenter
import com.aperfectpolygon.ergoplus.helper.api.ApiInterface
import com.aperfectpolygon.ergoplus.model.user.User
import com.orhanobut.logger.Logger
import retrofit2.create

object SignInPresenter : AbstractPresenter<SignInListener, User>() {

	fun signIn(username: String, password: String) = try {
		content {
			addProperty("username", username)
			addProperty("password", password)
		}
		safeRequest { create<ApiInterface>().signIn(headers, contents) }
		onResponse { listener?.onUserSignedIn(body) }
		onFailure { listener?.onApiErrorHandler(this) }
		// onDescription { code, description, link -> listener?.onDescription(code, description, link) }
		onError { listener?.onSignInFailed() }
	} catch (e: Exception) {
		listener?.onRequestFailed()
		Logger.e(e, e.message ?: "")
	}
}