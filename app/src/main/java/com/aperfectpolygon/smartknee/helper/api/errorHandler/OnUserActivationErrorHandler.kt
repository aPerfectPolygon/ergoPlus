package com.aperfectpolygon.smartknee.helper.api.errorHandler

import com.orhanobut.logger.Logger

interface OnUserActivationErrorHandler {
	fun onBadToken() {
		Logger.e("onBadToken")
	}

	fun onInactiveUser() {
		Logger.e("onInactiveUser")
	}

	fun onSuspendedUser() {
		Logger.e("onSuspendedUser")
	}
}
