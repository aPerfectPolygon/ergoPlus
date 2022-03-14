package com.aperfectpolygon.smartknee.helper.api.signin

import com.aperfectpolygon.smartknee.helper.api.errorHandler.OnApiErrorHandler
import com.aperfectpolygon.smartknee.model.user.User

interface SignInListener : OnApiErrorHandler {
	fun onUserSignedIn(result: User?)
	fun onSignInFailed()
}