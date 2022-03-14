package com.aperfectpolygon.smartknee.helper.api.errorHandler

import com.orhanobut.logger.Logger

interface OnVersionErrorHandler {
	fun onOutOfDate() {
		Logger.i("onOutOfDate")
	}
}