package com.aperfectpolygon.ergoplus.ui.gifts

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivityChartBinding
import com.aperfectpolygon.ergoplus.databinding.ActivityGiftsBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.firstGift
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.getLogs
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.secondGift
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity
import com.aperfectpolygon.ergoplus.ui.settings.SettingsActivity
import com.aperfectpolygon.ergoplus.ui.sport.SportActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.orhanobut.logger.Logger
import java.util.*


class GiftsActivity : AbstractActivity() {

	private lateinit var binding: ActivityGiftsBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		ActivityGiftsBinding.inflate(layoutInflater).apply {
			setContentView(root)
			circularProgressDrawable = CircularProgressDrawable(this@GiftsActivity).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
			if (UserRepository.avatar.isNotEmpty()) Uri.parse(UserRepository.avatar)?.let { fileUri ->
				Glide.with(this@GiftsActivity).load(fileUri).placeholder(circularProgressDrawable)
					.fallback(R.drawable.vtr_logo)
					.apply(
						RequestOptions.bitmapTransform(
							RoundedCornersTransformation(
								context = this@GiftsActivity,
								radius = 250,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).into(imgAvatar)
			} else Glide.with(this@GiftsActivity).load(R.drawable.vtr_logo)
				.placeholder(circularProgressDrawable).fallback(R.drawable.vtr_logo).apply(
					RequestOptions.bitmapTransform(
						RoundedCornersTransformation(
							context = this@GiftsActivity,
							radius = 250,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).into(imgAvatar) 

			if (!firstGift.isNullOrEmpty()) edtFirstGift.setText(firstGift)
			if (!secondGift.isNullOrEmpty()) edtSecondGift.setText(secondGift)

			btnSubmit.setOnClickListener {
				firstGift = edtFirstGift.text.toString()
				secondGift = edtSecondGift.text.toString()
			}

			imgChart.setOnClickListener { moveTo(this@GiftsActivity, GiftsActivity()) }
			imgGifts.setOnClickListener { moveTo(this@GiftsActivity, GiftsActivity()) }
			imgGym.setOnClickListener { moveTo(this@GiftsActivity, SportActivity()) }
			imgVibrate.setOnClickListener { moveTo(this@GiftsActivity, SettingsActivity()) }
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

		}.also { binding = it }
	}

	override fun onBackPressed() {
		moveTo(this@GiftsActivity, DashboardActivity())
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}