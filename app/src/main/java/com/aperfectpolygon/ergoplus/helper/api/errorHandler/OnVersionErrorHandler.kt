package com.aperfectpolygon.ergoplus.helper.api.errorHandler

import com.orhanobut.logger.Logger

interface OnVersionErrorHandler {
	fun onOutOfDate() {
		Logger.i("onOutOfDate")
	}
}