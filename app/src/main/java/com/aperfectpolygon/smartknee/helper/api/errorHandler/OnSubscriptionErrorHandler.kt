package com.aperfectpolygon.smartknee.helper.api.errorHandler

import com.orhanobut.logger.Logger

interface OnSubscriptionErrorHandler {
	fun onSubscriptionExpired() {
		Logger.e("onSubscriptionExpired")
	}
}