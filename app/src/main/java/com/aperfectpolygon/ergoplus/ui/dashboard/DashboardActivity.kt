@file:Suppress("DEPRECATION")

package com.aperfectpolygon.ergoplus.ui.dashboard

import android.net.Uri
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivityDashboardBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.BaseSocket.readAndFlush
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.ui.chart.ChartActivity
import com.aperfectpolygon.ergoplus.ui.gifts.GiftsActivity
import com.aperfectpolygon.ergoplus.ui.settings.SettingsActivity
import com.aperfectpolygon.ergoplus.ui.sport.SportActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform


class DashboardActivity : AbstractActivity() {

	private lateinit var binding: ActivityDashboardBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDashboardBinding.inflate(layoutInflater).apply {
			setContentView(root)
			circularProgressDrawable = CircularProgressDrawable(this@DashboardActivity).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
			if (UserRepository.avatar.isNotEmpty()) Uri.parse(UserRepository.avatar)?.let { fileUri ->
				Glide.with(this@DashboardActivity).load(fileUri).placeholder(circularProgressDrawable)
					.apply(
						bitmapTransform(
							RoundedCornersTransformation(
								context = this@DashboardActivity,
								radius = 250,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).fallback(R.drawable.vtr_logo).into(imgAvatar)
			} else Glide.with(this@DashboardActivity).load(R.drawable.vtr_logo)
				.placeholder(circularProgressDrawable)
				.fallback(R.drawable.vtr_logo)
				.apply(
					bitmapTransform(
						RoundedCornersTransformation(
							context = this@DashboardActivity,
							radius = 250,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).into(imgAvatar)
			txtUsername.text = UserRepository.username
			imgChart.setOnClickListener {
				readAndFlush
				moveTo(this@DashboardActivity, ChartActivity())
			}
			imgGifts.setOnClickListener { moveTo(this@DashboardActivity, GiftsActivity()) }
			imgGym.setOnClickListener { moveTo(this@DashboardActivity, SportActivity()) }
			imgVibrate.setOnClickListener { moveTo(this@DashboardActivity, SettingsActivity()) }
		}
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}