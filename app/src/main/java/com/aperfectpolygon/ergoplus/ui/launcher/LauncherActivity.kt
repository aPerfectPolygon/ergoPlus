package com.aperfectpolygon.ergoplus.ui.launcher

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.databinding.ActivityLauncherBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.ui.auth.AuthenticatorActivity
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity

class LauncherActivity : AbstractActivity() {

	private lateinit var binding: ActivityLauncherBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityLauncherBinding.inflate(layoutInflater).apply {
			setContentView(root)
			Handler(Looper.getMainLooper()).postDelayed({
				if (UserRepository.user.username.isEmpty())
					moveTo(this@LauncherActivity, AuthenticatorActivity())
				else
					moveTo(this@LauncherActivity, DashboardActivity())
			}, 5000)
		}
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}