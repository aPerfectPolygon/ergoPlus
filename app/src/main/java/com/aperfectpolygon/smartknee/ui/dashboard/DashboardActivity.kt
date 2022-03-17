@file:Suppress("DEPRECATION")

package com.aperfectpolygon.smartknee.ui.dashboard

import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.Vibrator
import android.os.VibratorManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.ActivityDashboardBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.RoundedCornersTransformation
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.helper.sharedPrefrences.ShpHelper.socket
import com.aperfectpolygon.smartknee.ui.chart.ChartActivity
import com.aperfectpolygon.smartknee.utils.isWifiConnected
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform


class DashboardActivity : AbstractActivity() {

	private lateinit var binding: ActivityDashboardBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDashboardBinding.inflate(layoutInflater).apply {
			setContentView(root)
			Uri.parse(UserRepository.avatar)?.let { fileUri ->
				circularProgressDrawable = CircularProgressDrawable(this@DashboardActivity).apply {
					strokeWidth = 5f
					centerRadius = 30f
					start()
				}
				Glide.with(this@DashboardActivity).load(fileUri).placeholder(circularProgressDrawable)
					.apply(
						bitmapTransform(
							RoundedCornersTransformation(
								context = this@DashboardActivity,
								radius = 200,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).into(imgAvatar)
			}
			imgChart.setOnClickListener { moveTo(this@DashboardActivity, ChartActivity()) }
			imgGifts.setOnClickListener { socket.readVoltage }
			imgGym.setOnClickListener { socket.read }
			imgVibrate.setOnClickListener {
				when {
					SDK_INT >= Build.VERSION_CODES.S -> (getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager).apply {
						defaultVibrator.vibrate(createOneShot(200, DEFAULT_AMPLITUDE))
					}
					else -> (getSystemService(VIBRATOR_SERVICE) as Vibrator).apply {
						when {
							SDK_INT >= 26 -> vibrate(createOneShot(200, DEFAULT_AMPLITUDE))
							else -> vibrate(200)
						}
					}
				}
				socket.event0
			}
			isWifiConnected
		}
	}


	override fun onResume() {
		super.onResume()
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}