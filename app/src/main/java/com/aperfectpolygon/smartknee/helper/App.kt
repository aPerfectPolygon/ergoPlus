@file:Suppress("DEPRECATION")

package com.aperfectpolygon.smartknee.helper

import android.app.Application
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.os.StrictMode.ThreadPolicy
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.aperfectpolygon.smartknee.BuildConfig
import com.aperfectpolygon.smartknee.database.events.EventsRepo
import com.aperfectpolygon.smartknee.helper.ObjectBox.init
import com.aperfectpolygon.smartknee.helper.sharedPrefrences.ShpHelper
import com.aperfectpolygon.smartknee.utils.localWifiIpAddress
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.runBlocking
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
		socket()
	}

	private fun socket() {
		kotlin.runCatching {
			runBlocking {
				Logger.i(localWifiIpAddress ?: "cant find the ip Address")
				Logger.i(localWifiIpAddress?.let { it.replace(it.split('.')[3], "35") }
					?: "cant find the ip Address")
				if (!localWifiIpAddress.isNullOrEmpty()) ShpHelper.socket.apply {
					host = localWifiIpAddress?.let { it.replace(it.split('.')[3], "35") }!!
					onEventReceived {
						when {
							SDK_INT >= S -> (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).apply {
								defaultVibrator.vibrate(createOneShot(200, DEFAULT_AMPLITUDE))
							}
							else -> (getSystemService(VIBRATOR_SERVICE) as Vibrator).apply {
								when {
									SDK_INT >= 26 -> vibrate(createOneShot(200, DEFAULT_AMPLITUDE))
									else -> vibrate(200)
								}
							}
						}
						Handler(Looper.getMainLooper()).post {
							makeText(this@App.applicationContext, it.type, LENGTH_SHORT).show()
						}
					}
					onLogsReceived { EventsRepo.events = it }
					onConnect {
						Handler(Looper.getMainLooper()).post {
							makeText(this@App.applicationContext, "Connected!", LENGTH_SHORT).show()
						}
					}
					onDisconnect {
						Handler(Looper.getMainLooper()).post {
							makeText(this@App.applicationContext, "Disconnected!", LENGTH_SHORT).show()
						}
					}
					connect()
				} else Handler(Looper.getMainLooper()).post {
					makeText(this@App.applicationContext, "Check Network Connection", LENGTH_SHORT).show()
				}
			}
		}
	}

}