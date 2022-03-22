@file:Suppress("DEPRECATION")

package com.aperfectpolygon.ergoplus.helper

import android.app.Application
import android.os.*
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S
import android.os.StrictMode.ThreadPolicy
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.aperfectpolygon.ergoplus.BuildConfig
import com.aperfectpolygon.ergoplus.helper.ObjectBox.init
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.canVibrate
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.getLogs
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.logs
import com.aperfectpolygon.ergoplus.utils.isWifiConnected
import com.aperfectpolygon.ergoplus.utils.localWifiIpAddress
import com.aperfectpolygon.ergoplus.utils.vpn
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.runBlocking

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
		StrictMode.setThreadPolicy(ThreadPolicy.Builder().permitAll().build())
		try {
			socket()
		} catch (e: Exception) {
			Logger.e(e, e.message ?: "")
		}
	}

	private fun socket() {
		if (isWifiConnected and !vpn)
			kotlin.runCatching {
				runBlocking {
					Logger.i(
						localWifiIpAddress?.removeSuffix(localWifiIpAddress?.split('.')?.get(3) ?: "")
							.plus("35")
					)

					if (!localWifiIpAddress.isNullOrEmpty()) ShpHelper.socket.apply {
						host = localWifiIpAddress?.removeSuffix(localWifiIpAddress?.split('.')?.get(3) ?: "")
							.plus("35")
						onEventReceived {
							if (canVibrate)
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
							/*Handler(Looper.getMainLooper()).post {
								makeText(applicationContext, "Event Received", LENGTH_SHORT).show()
							}*/
						}
						onLogsReceived {
							getLogs.apply {
								it.data?.let { it1 -> addAll(it1) }
								applicationContext.logs = distinctBy { it[0] }.sortedBy { it[0] }.toString()
							}
							/*Handler(Looper.getMainLooper()).post {
								makeText(
									applicationContext, "Logs Received", LENGTH_SHORT
								).show()
							}*/
						}
						onRetry {
							Handler(Looper.getMainLooper()).post {
								makeText(
									applicationContext, "Retrying to connect please be patient!", LENGTH_SHORT
								).show()
							}
						}
						onConnect {
							setTime
							Handler(Looper.getMainLooper()).post {
								makeText(applicationContext, "Connected!", LENGTH_SHORT).show()
							}
						}
						onDisconnect {
							Handler(Looper.getMainLooper()).post {
								makeText(applicationContext, "Disconnected!", LENGTH_SHORT).show()
							}
						}
						connect()
					} else Handler(Looper.getMainLooper()).post {
						makeText(applicationContext, "Check Network Connection", LENGTH_SHORT).show()
					}
				}
			}
	}

}