package com.aperfectpolygon.ergoplus.helper.api.errorHandler

import com.orhanobut.logger.Logger

interface OnSubscriptionErrorHandler {
	fun onSubscriptionExpired() {
		Logger.e("onSubscriptionExpired")
	}
}