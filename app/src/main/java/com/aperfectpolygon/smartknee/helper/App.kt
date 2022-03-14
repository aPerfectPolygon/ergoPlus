package com.aperfectpolygon.smartknee.helper

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import com.aperfectpolygon.smartknee.BuildConfig
import com.aperfectpolygon.smartknee.helper.ObjectBox.init
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant

class App : Application() {

	override fun onCreate() {
		super.onCreate()
		init()
		Logger.addLogAdapter(AndroidLogAdapter())
		PrettyFormatStrategy.newBuilder().apply {
			showThreadInfo(true)
			methodCount(0)
			methodOffset(7)
			tag("_Polygon")
		}.build().also { Logger.addLogAdapter(AndroidLogAdapter(it)) }
		Logger.addLogAdapter(
			object : AndroidLogAdapter() {
				override fun isLoggable(priority: Int, tag: String?): Boolean = BuildConfig.DEBUG
			}
		)
		plant(
			object : DebugTree() {
				override fun log(priority: Int, tag: String?, message: String, t: Throwable?) =
					Logger.log(priority, tag, message, t)
			}
		)
		StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
	}
}