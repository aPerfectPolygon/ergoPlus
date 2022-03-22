package com.aperfectpolygon.ergoplus.helper

import android.content.Context
import com.aperfectpolygon.ergoplus.model.user.MyObjectBox
import io.objectbox.BoxStore

// import io.objectbox.android.AndroidObjectBrowser

object ObjectBox {

	lateinit var boxStore: BoxStore private set

	fun Context.init() {
		boxStore =
			MyObjectBox.builder().androidContext(applicationContext).name("aPerfectPolygon_SmartKneeStrap_17_Mar_2022").build()

		// if (!BuildConfig.BUILD_TYPE.contains("debug")) return
		// Logger.d(TAG, "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
		// val started = AndroidObjectBrowser(boxStore).start(applicationContext)
		// Logger.i(TAG, "Started: $started")
	}
}