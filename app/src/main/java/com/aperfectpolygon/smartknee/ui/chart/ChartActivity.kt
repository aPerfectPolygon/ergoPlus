package com.aperfectpolygon.smartknee.ui.chart

import android.graphics.Color
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.events.EventsRepo
import com.aperfectpolygon.smartknee.database.events.EventsRepo.events
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.ActivityChartBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.ui.dashboard.DashboardActivity
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue


class ChartActivity : AbstractActivity() {

	private lateinit var binding: ActivityChartBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		ActivityChartBinding.inflate(layoutInflater).apply {
			setContentView(root)
			if (UserRepository.user.avatar.isNotEmpty()) {
				circularProgressDrawable = CircularProgressDrawable(this@ChartActivity).apply {
					strokeWidth = 5f
					centerRadius = 30f
					start()
				}
				imgAvatar.loadImageResourceWithGlide(UserRepository.user.avatar)
			}
			imgChart.setOnClickListener { moveTo(this@ChartActivity, ChartActivity()) }
			// imgGifts.setOnClickListener { moveTo(this@ChartActivity, GiftActivity()) }
			// imgGym.setOnClickListener { moveTo(this@ChartActivity, GymActivity()) }

			lineChart.apply {
				isInteractive = true
				zoomType = ZoomType.HORIZONTAL
				setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)
				ArrayList<PointValue>().also { values ->
					events?.data?.forEach { values.add(PointValue(it[0].toFloat(), it[1].toFloat())) }
					ArrayList<Line>().also { lines ->
						lines.add(Line(values).setColor(Color.BLUE).setCubic(true))
						LineChartData().also { data ->
							data.lines = lines
							lineChartData = data
						}
					}
				}
			}


		}.also { binding = it }
	}

	override fun onBackPressed() {
		moveTo(this@ChartActivity, DashboardActivity())
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}