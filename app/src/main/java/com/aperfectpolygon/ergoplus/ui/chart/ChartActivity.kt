package com.aperfectpolygon.ergoplus.ui.chart

import android.net.Uri
import android.os.Bundle
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivityChartBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.helper.sharedPrefrences.ShpHelper.getLogs
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


class ChartActivity : AbstractActivity() {

	private lateinit var binding: ActivityChartBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		ActivityChartBinding.inflate(layoutInflater).apply {
			setContentView(root)
			Uri.parse(UserRepository.avatar)?.let { fileUri ->
				circularProgressDrawable = CircularProgressDrawable(this@ChartActivity).apply {
					strokeWidth = 5f
					centerRadius = 30f
					start()
				}
				Glide.with(this@ChartActivity).load(fileUri).placeholder(circularProgressDrawable)
					.apply(
						RequestOptions.bitmapTransform(
							RoundedCornersTransformation(
								context = this@ChartActivity,
								radius = 250,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).into(imgAvatar)
			}
			imgChart.setOnClickListener { moveTo(this@ChartActivity, ChartActivity()) }
			// imgGifts.setOnClickListener { moveTo(this@ChartActivity, GiftActivity()) }
			// imgGym.setOnClickListener { moveTo(this@ChartActivity, GymActivity()) }

			Logger.i("logs: $getLogs")
			getLogs.groupBy {
				Calendar.getInstance().apply {
					time = Date(it[0].times(1000))
				}.let {
					"${
						it.getDisplayName(
							Calendar.DAY_OF_WEEK,
							Calendar.SHORT,
							Locale.ENGLISH
						)
					}, ${
						it.get(Calendar.DAY_OF_MONTH)
					}, ${
						it.getDisplayName(
							Calendar.MONTH,
							Calendar.SHORT,
							Locale.ENGLISH
						)
					}"
				}
			}.also { map ->
				Logger.i("map: $map")
				AAChartModel().apply {
					chartType(AAChartType.Line)
					title("title")
					subtitle("subtitle")
					backgroundColor(getColor(R.color.transparent))
					dataLabelsEnabled(true)
					Logger.i("keys : ${map.keys}")
					series = arrayOf(
						AASeriesElement().apply {
							name("")
							categories = map.keys.toTypedArray()
							mutableListOf<Int>().also { data ->
								map.keys.onEach { key ->
									map.keys.size
									data.add(map.getValue(key).size)
								}
								data(data.toTypedArray())
							}
						}
					)
				}.also { lineChart.aa_drawChartWithChartModel(it) }
			}

			imgChart.setOnClickListener { moveTo(this@ChartActivity, ChartActivity()) }
			imgGifts.setOnClickListener { /*moveTo(this@ChartActivity, ChartActivity())*/ }
			imgGym.setOnClickListener { moveTo(this@ChartActivity, SportActivity()) }
			imgVibrate.setOnClickListener { moveTo(this@ChartActivity, SettingsActivity()) }
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
		moveTo(this@ChartActivity, DashboardActivity())
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}