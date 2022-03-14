package com.aperfectpolygon.smartknee.ui

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aperfectpolygon.smartknee.databinding.ActivityLauncherBinding
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import java.util.*

class LauncherActivity : AbstractActivity() {

	private lateinit var binding: ActivityLauncherBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)
		binding = ActivityLauncherBinding.inflate(layoutInflater)
		setContentView(binding.root)
		/*when (UserRepository.user.status) {
			"ACTIVE" -> moveTo(from = this, to = DashboardActivity())
			else -> moveTo(from = this, to = AuthenticatorActivity())
		}*/
	}

	private fun socket() {
		runBlocking {
			Socket("192.168.1.35", 8888).apply {
				PrintWriter(outputStream).apply {
					try {
						// print("time${System.currentTimeMillis().div(1000)}")
						print("read")
						flush()
					} catch (e: IOException) {
						Logger.e(e, e.message ?: "")
					}
				}
				CoroutineScope(Dispatchers.IO).launch {
					Scanner(inputStream).apply {
						while (hasNextLine()) nextLine().apply {
							if (!isNullOrEmpty()) also { line ->
								runCatching {
									runOnUiThread { binding.txtSocketStatus.text = line }
									Logger.i("line: $line")
								}.onFailure { Logger.e(it, it.message ?: "") }
							}
						}
					}
				}
			}
		}
	}

	override fun onResume() {
		super.onResume()
		MainScope().launch {
			socket()
		}
	}
}