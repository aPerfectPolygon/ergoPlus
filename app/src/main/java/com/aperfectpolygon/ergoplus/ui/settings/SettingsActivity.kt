package com.aperfectpolygon.ergoplus.ui.settings

import android.net.Uri
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivitySettingsBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.canVibrate
import com.aperfectpolygon.ergoplus.ui.chart.ChartActivity
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity
import com.aperfectpolygon.ergoplus.ui.sport.SportActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SettingsActivity : AbstractActivity() {

	private lateinit var binding: ActivitySettingsBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySettingsBinding.inflate(layoutInflater).apply {
			setContentView(root)
			circularProgressDrawable = CircularProgressDrawable(this@SettingsActivity).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
			if (UserRepository.avatar.isNotEmpty()) Uri.parse(UserRepository.avatar)?.let { fileUri ->
				Glide.with(this@SettingsActivity).load(fileUri).placeholder(circularProgressDrawable)
					.apply(
						RequestOptions.bitmapTransform(
							RoundedCornersTransformation(
								context = this@SettingsActivity,
								radius = 250,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).fallback(R.drawable.vtr_logo).into(imgAvatar)
			} else Glide.with(this@SettingsActivity).load(R.drawable.vtr_logo)
				.placeholder(circularProgressDrawable)
				.fallback(R.drawable.vtr_logo)
				.apply(
					RequestOptions.bitmapTransform(
						RoundedCornersTransformation(
							context = this@SettingsActivity,
							radius = 250,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).into(imgAvatar)


			switchBand.setOnCheckedChangeListener { buttonView, isChecked -> }
			switchPhone.setOnCheckedChangeListener { buttonView, isChecked -> canVibrate = isChecked }
			imgChart.setOnClickListener { moveTo(this@SettingsActivity, ChartActivity()) }
			imgGifts.setOnClickListener { /*moveTo(this@SettingsActivity, ChartActivity())*/ }
			imgGym.setOnClickListener { moveTo(this@SettingsActivity, SportActivity()) }
			imgVibrate.setOnClickListener { moveTo(this@SettingsActivity, SettingsActivity()) }
			imgAvatar.setOnClickListener {
				if (imgChart.isVisible) {
					imgChart.isGone = true
					imgGifts.isGone = true
					imgGym.isGone = true
					imgVibrate.isGone = true
				} else {
					imgChart.isVisible = true
					imgGifts.isVisible = true
					imgGym.isVisible = true
					imgVibrate.isVisible = true
				}
			}
		}
	}

	override fun onBackPressed() {
		moveTo(this, DashboardActivity())
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}