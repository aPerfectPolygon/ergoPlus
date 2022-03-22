package com.aperfectpolygon.ergoplus.helper.api.signin

import com.aperfectpolygon.ergoplus.helper.api.errorHandler.OnApiErrorHandler
import com.aperfectpolygon.ergoplus.model.user.User

interface SignInListener : OnApiErrorHandler {
	fun onUserSignedIn(result: User?)
	fun onSignInFailed()
}