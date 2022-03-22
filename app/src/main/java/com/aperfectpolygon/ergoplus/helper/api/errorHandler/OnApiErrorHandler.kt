package com.aperfectpolygon.ergoplus.helper.api.errorHandler

import com.aperfectpolygon.ergoplus.helper.api.ApiInterface.Companion.badToken
import com.aperfectpolygon.ergoplus.helper.api.ApiInterface.Companion.inactiveUser
import com.aperfectpolygon.ergoplus.helper.api.ApiInterface.Companion.outOfDate
import com.aperfectpolygon.ergoplus.helper.api.ApiInterface.Companion.subscriptionExpired
import com.aperfectpolygon.ergoplus.helper.api.ApiInterface.Companion.suspendedUser
import com.orhanobut.logger.Logger

interface OnApiErrorHandler : OnSubscriptionErrorHandler, OnUserActivationErrorHandler,
	OnVersionErrorHandler {

	fun onDescription(code: Int, description: String?, link: String?) {
		if (code in 200..300)
			Logger.i("code: $code, description: $description, link: $link")
		else
			Logger.e("code: $code, description: $description, link: $link")
	}

	fun onRequestFailed() {
		Logger.e("onRequestFailed")
	}

	fun onApiErrorHandler(message: String?) {
		when (message) {
			badToken -> onBadToken()
			subscriptionExpired -> onSubscriptionExpired()
			inactiveUser -> onInactiveUser()
			suspendedUser -> onSuspendedUser()
			outOfDate -> onOutOfDate()
			else -> onRequestFailed()
		}
	}
}