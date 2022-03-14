package com.aperfectpolygon.smartknee.ui.auth

import android.os.Bundle
import com.aperfectpolygon.smartknee.databinding.ActivityAutherticatorBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.TARGET
import com.aperfectpolygon.smartknee.helper.ActivityHelper.idSignIn
import com.aperfectpolygon.smartknee.helper.ActivityHelper.idSignUp
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.helper.router.OnNavigate
import com.aperfectpolygon.smartknee.helper.router.OnNavigate.Companion.SIGNUP
import com.aperfectpolygon.smartknee.helper.router.OnNavigate.Companion.SIGN_IN

class AuthenticatorActivity : AbstractActivity(), OnNavigate {

	private var target = 0

	private lateinit var binding: ActivityAutherticatorBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAutherticatorBinding.inflate(layoutInflater)
		setContentView(binding.root)
		hideKeyboard

		intent?.extras?.let { extras ->
			extras.keySet().forEach { key: String ->
				when (key) {
					TARGET -> target = extras.getInt(key)
				}
			}
		}

		when (target) {
			idSignIn -> navigateTo(SIGN_IN)
			idSignUp -> navigateTo(SIGNUP)
			else -> navigateTo(SIGN_IN)
		}
	}

	override fun navigateTo(targetId: Int?) {
		when (targetId) {

		}
	}
}