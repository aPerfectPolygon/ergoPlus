package com.aperfectpolygon.smartknee.helper.router

interface OnNavigate {

	companion object {
		const val MAIN: Int = 0
		const val SIGN_IN: Int = 1
		const val SIGNUP: Int = 2
		const val FORGOT_PASSWORD: Int = 3

	}

	fun navigateTo(targetId: Int?)
}