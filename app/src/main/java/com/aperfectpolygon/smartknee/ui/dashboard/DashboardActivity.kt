package com.aperfectpolygon.smartknee.ui.dashboard

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.VibrationEffect.createOneShot
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.ActivityDashboardBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.ui.chart.ChartActivity


class DashboardActivity : AppCompatActivity() {

	private lateinit var binding: ActivityDashboardBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDashboardBinding.inflate(layoutInflater).apply {
			setContentView(root)
			imgAvatar.setImageURI(Uri.parse(UserRepository.avatar))
			imgChart.setOnClickListener { moveTo(this@DashboardActivity, ChartActivity()) }
			imgGifts.setOnClickListener { moveTo(this@DashboardActivity, ChartActivity()) }
			imgGym.setOnClickListener { moveTo(this@DashboardActivity, ChartActivity()) }
			imgVibrate.setOnClickListener {
				(getSystemService(VIBRATOR_SERVICE) as Vibrator).apply {
					if (SDK_INT >= 26) vibrate(createOneShot(200, DEFAULT_AMPLITUDE)) else vibrate(200)
				}
			}
		}
	}
}