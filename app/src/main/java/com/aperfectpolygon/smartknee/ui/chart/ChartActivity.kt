package com.aperfectpolygon.smartknee.ui.chart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aperfectpolygon.smartknee.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {

	private lateinit var binding: ActivityChartBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityChartBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}