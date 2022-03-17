package com.aperfectpolygon.smartknee.ui.launcher

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.ActivityLauncherBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.ui.auth.AuthenticatorActivity
import com.aperfectpolygon.smartknee.ui.dashboard.DashboardActivity

class LauncherActivity : AbstractActivity() {

	private lateinit var binding: ActivityLauncherBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)
		binding = ActivityLauncherBinding.inflate(layoutInflater).apply {
			setContentView(root)
			if (UserRepository.user.username.isEmpty())
				moveTo(this@LauncherActivity, AuthenticatorActivity())
			else
				moveTo(this@LauncherActivity, DashboardActivity())
		}
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}