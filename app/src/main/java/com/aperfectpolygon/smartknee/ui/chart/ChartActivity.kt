package com.aperfectpolygon.smartknee.ui.chart

import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.ActivityChartBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity

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

		}.also { binding = it }
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}