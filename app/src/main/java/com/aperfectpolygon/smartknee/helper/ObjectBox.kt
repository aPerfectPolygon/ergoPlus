package com.aperfectpolygon.smartknee.helper

import android.content.Context
import com.aperfectpolygon.smartknee.R
import com.aperfectpolygon.smartknee.model.user.MyObjectBox
import io.objectbox.BoxStore

// import io.objectbox.android.AndroidObjectBrowser

object ObjectBox {

	lateinit var boxStore: BoxStore private set

	fun Context.init() {
		boxStore =
			MyObjectBox.builder().androidContext(applicationContext).name("getString(R.string.db_name)")
				.build()

		// if (!BuildConfig.BUILD_TYPE.contains("debug")) return
		// Logger.d(TAG, "Using ObjectBox ${BoxStore.getVersion()} (${BoxStore.getVersionNative()})")
		// val started = AndroidObjectBrowser(boxStore).start(applicationContext)
		// Logger.i(TAG, "Started: $started")
	}
}