package com.aperfectpolygon.ergoplus.ui.sport

import android.net.Uri
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivitySportBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.model.Sport
import com.aperfectpolygon.ergoplus.ui.chart.ChartActivity
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity
import com.aperfectpolygon.ergoplus.ui.gifts.GiftsActivity
import com.aperfectpolygon.ergoplus.ui.settings.SettingsActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class SportActivity : AbstractActivity() {

	private lateinit var binding: ActivitySportBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivitySportBinding.inflate(layoutInflater).apply {
			setContentView(root)
			circularProgressDrawable = CircularProgressDrawable(this@SportActivity).apply {
				strokeWidth = 5f
				centerRadius = 30f
				start()
			}
			if (UserRepository.avatar.isNotEmpty()) Uri.parse(UserRepository.avatar)?.let { fileUri ->
				Glide.with(this@SportActivity).load(fileUri).placeholder(circularProgressDrawable).apply(
					RequestOptions.bitmapTransform(
						RoundedCornersTransformation(
							context = this@SportActivity,
							radius = 250,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).fallback(R.drawable.vtr_logo).into(imgAvatar)
			} else Glide.with(this@SportActivity).load(R.drawable.vtr_logo)
				.placeholder(circularProgressDrawable)
				.fallback(R.drawable.vtr_logo)
				.apply(
					RequestOptions.bitmapTransform(
						RoundedCornersTransformation(
							context = this@SportActivity,
							radius = 250,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).into(imgAvatar)

			recyclerView.layoutManager = GridLayoutManager(this@SportActivity, 2)
			recyclerView.setHasFixedSize(true)
			recyclerView.adapter = SportAdapter(
				this@SportActivity,
				listOf(
					Sport(
						title = "1",
						url = "https://drive.google.com/file/d/1Vk5bHTdNrcZRqSzWxwDBZTsBhEqyc0Ch/view?usp=sharing",
						thumbnail = R.mipmap.arm
					),
					Sport(
						title = "2",
						url = "https://drive.google.com/file/d/1unSU1cgjnnPa2o39s3aueQfNa6UxEL_Z/view?usp=sharing",
						thumbnail = R.mipmap.run
					),
					Sport(
						title = "3",
						url = "https://drive.google.com/file/d/1YRrV076z9BEVfLGd30XjJZQBYmbC2keZ/view?usp=sharing",
						thumbnail = R.mipmap.ghesmate_miani
					),
					Sport(
						title = "4",
						url = "https://drive.google.com/file/d/1pUMa8h2tBMvtPi4pjLKlpEGl_ChENQVX/view?usp=sharing",
						thumbnail = R.mipmap.arm
					),
					Sport(
						title = "5",
						url = "https://drive.google.com/file/d/1qsS9O6hfyhhuaNp9CrMim3lElLk2Eoqd/view?usp=sharing",
						thumbnail = R.mipmap.bbb
					),
					Sport(
						title = "6",
						url = "https://drive.google.com/file/d/12tjefxH0zvfxI1znq5rDGazZ8fwIJ2Cl/view?usp=sharing",
						thumbnail = R.mipmap.bbb
					),
					Sport(
						title = "7",
						url = "https://drive.google.com/file/d/1SKumk4kPBLFO6YgEW12E2SAjEwFm-SWD/view?usp=sharing",
						thumbnail = R.mipmap.azolate_lagan_va_run
					),
					Sport(
						title = "8",
						url = "https://drive.google.com/file/d/1Xf7G8_xZWKRvzZq9uqjPLhwLp8Qv6_Ta/view?usp=sharing",
						thumbnail = R.mipmap.ketf_o_dast
					),
					Sport(
						title = "9",
						url = "https://drive.google.com/file/d/1svkwOR1ZcKZNdof4WcP1ucLLVvKte1n-/view?usp=sharing",
						thumbnail = R.mipmap.moch
					)
				)
			)

			imgChart.setOnClickListener { moveTo(this@SportActivity, ChartActivity()) }
			imgGifts.setOnClickListener { moveTo(this@SportActivity, GiftsActivity()) }
			imgGym.setOnClickListener { moveTo(this@SportActivity, SportActivity()) }
			imgVibrate.setOnClickListener { moveTo(this@SportActivity, SettingsActivity()) }
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