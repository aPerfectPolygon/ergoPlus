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
import com.aperfectpolygon.ergoplus.ui.chart.ChartActivity
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity
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

			recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)
			recyclerView.setHasFixedSize(true)
			recyclerView.adapter = SportAdapter(
				applicationContext,
				listOf(
					Sport(
						"1",
						"https://edef4.pcloud.com/cBZOK3XMFZKWDj3LZZZgcS7r7Z2ZZUHZkZwhApXZbXZyXZFXZRVZAXZPkZtkZfkZnkZKXZakZEkZMXZYkZHguHZzEBLEWFcDi5pwlptjQnyPVIdgX8V/1%2BText.mp4"
					),
					Sport(
						"2",
						"https://edef1.pcloud.com/cBZENvXMFZRtnj3LZZZCpj7r7Z2ZZUHZkZaylGkZjXZIkZDkZrXZikZ9kZhVZxkZHkZTXZ2XZ8VZEXZi7ZHguHZzVz7pDq1JL0uudyijSuLwyUGys1y/2%2BText.mp4"
					),
					Sport(
						"3",
						"https://edef4.pcloud.com/cBZiUT59FZvuxaNLZZZ8Hj7r7Z2ZZUHZkZBNmFXZMXZXVZ3kZDXZ6XZtkZC7ZpVZJVZKXZ4kZYXZAXZmVZHguHZOykA62INeCSoiBXK4APLyp3IE8vX/3.mp4"
					),
					Sport(
						"4",
						"https://edef3.pcloud.com/cBZM2rXMFZxwTj3LZZZDHj7r7Z2ZZUHZkZjh80XZxkZQVZRVZQkZIXZSkZjXZzkZy7ZIkZMXZYXZVVZhXZHguHZGJveu6tet7yF1qOupoUTSHgP1LAV/4%2BText.mp4"
					),
					Sport(
						"5",
						"https://edef4.pcloud.com/cBZK6e59FZihsaNLZZZtHj7r7Z2ZZUHZkZC9P5XZqkZDXZiXZnkZ0VZpVZYVZaXZakZDkZ6XZLXZfVZ8kZHguHZpH8q4hfFYx71wV8yb62biFSDKipk/5.mp4"
					),
					Sport(
						"6",
						"https://edef3.pcloud.com/cBZz6359FZSag3NLZZZvHj7r7Z2ZZUHZkZmA5nXZ3XZhVZOXZAkZA7ZUkZ4kZhXZSkZaXZqkZuVZuXZckZHguHZ7HUyb4pxV9J0eBErBidNOkl101Oy/6.mp4"
					),
					Sport(
						"7",
						"https://edef4.pcloud.com/cBZVKcXMFZuWCj3LZZZBzj7r7Z2ZZUHZkZPpneXZXVZ7XZtkZhVZ9XZrXZsXZPkZrkZbXZKkZzVZ0XZMkZHguHZhE0PB6yoMXLjUJbCa4BAdLgqpc2V/7%2BText.mp4"
					),
					Sport(
						"8",
						"https://edef1.pcloud.com/cBZDToXMFZgFwj3LZZZlzj7r7Z2ZZUHZkZ9q3IkZAXZi7ZUXZdkZjXZ67ZTkZ0VZmXZYXZtZYVZXXZ8kZHguHZbr0mb8ESd60I70uS1lvlPXRgenRX/8%2BText.mp4"
					),
					Sport(
						"9",
						"https://edef4.pcloud.com/cBZ3dd59FZsjV3NLZZZ3zj7r7Z2ZZUHZkZ1hrUkZjkZmVZJVZf7ZEXZ0kZfVZTXZi7ZpVZYXZaXZLXZyVZHguHZaJPwcwrWD6RTI4faKR6sTBJx1nv7/9.mp4"
					)
				)
			)

			imgChart.setOnClickListener { moveTo(this@SportActivity, ChartActivity()) }
			imgGifts.setOnClickListener { /*moveTo(this@SportActivity, ChartActivity())*/ }
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