package com.aperfectpolygon.smartknee.ui.auth.signIn

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import com.aperfectpolygon.smartknee.R
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.databinding.FragmentSigninBinding
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractFragment
import com.aperfectpolygon.smartknee.helper.api.signin.SignInListener
import com.aperfectpolygon.smartknee.helper.api.signin.SignInPresenter
import com.aperfectpolygon.smartknee.helper.router.OnNavigate
import com.aperfectpolygon.smartknee.model.user.User
import com.aperfectpolygon.smartknee.utils.*
import com.google.android.material.textfield.TextInputLayout

class SigninFrg : AbstractFragment(), SignInListener, View.OnClickListener {

	companion object {
		val newInstance: SigninFrg
			get() = SigninFrg()
	}

	private fun initPresenter(userName: String, password: String) {
		if (!isAdded or isDetached) return
		SignInPresenter.apply {
			onAttach(this@SigninFrg)
			signIn(userName, password)
		}
	}

	override fun onErrorHandler() {
		if (!isAdded or isDetached) return
		with(binding.signin) {
			isEnabled = true
			setTextColor(getColor(R.color.primary))
		}
	}

	override lateinit var rootLayout: View
	private lateinit var binding: FragmentSigninBinding

	override fun onDispose() {
		SignInPresenter.onDispose()
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View = FragmentSigninBinding.inflate(inflater, container, false).apply {
		this@SigninFrg.rootLayout = root
		signup.setOnClickListener(this@SigninFrg)
		forgotPassword.setOnClickListener(this@SigninFrg)
		forgotUsername.setOnClickListener(this@SigninFrg)
		signin.setOnClickListener(this@SigninFrg)
	}.also { binding = it }.root

	private fun isDataValid(username: String, password: String): Boolean = when {
		!username.isUserNameValid -> false
		!password.isPasswordValid -> false
		else -> true
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.tilPassword.apply {
			endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
			isEndIconVisible = false
			binding.edtPassword.afterTextChanged { txt ->
				txt.isNotEmpty().also { isNotEmpty ->
					isEndIconVisible = isNotEmpty
					isEndIconCheckable = isNotEmpty
				}
			}
		}

		// disable sign-in button unless both username / password is valid
		binding.signin.apply {
			isEnabled = isDataValid(binding.edtPhoneNumber.textString, binding.edtPassword.textString)
			when (isEnabled) {
				true -> getColor(R.color.primary)
				else -> getColor(R.color.disableTextColor)
			}.also { setTextColor(it) }
		}


		binding.edtPhoneNumber.afterTextChanged {
			binding.signin.apply {
				isEnabled = isDataValid(binding.edtPhoneNumber.textString, binding.edtPassword.textString)
				when (isEnabled) {
					true -> getColor(R.color.primary)
					else -> getColor(R.color.disableTextColor)
				}.also { setTextColor(it) }
			}
		}
		binding.edtPassword.apply {
			afterTextChanged {
				binding.signin.apply {
					isEnabled = isDataValid(binding.edtPhoneNumber.textString, binding.edtPassword.textString)
					when (isEnabled) {
						true -> getColor(R.color.primary)
						else -> getColor(R.color.disableTextColor)
					}.also { setTextColor(it) }
				}
			}

			setOnEditorActionListener { _, actionId, _ ->
				if (actionId == IME_ACTION_DONE && binding.signin.isEnabled) binding.signin.performClick()
				false
			}
		}
	}

	private fun initService() {
		if (!isAdded or isDetached) return
	}

	override fun onUserSignedIn(result: User?) {
		if (!isAdded or isDetached) return
		binding.signin.isEnabled = false
		binding.signin.setTextColor(getColor(R.color.disableTextColor))
		result?.let {
			UserRepository.user = result
			initService()
			// updateUiWithUser(signInResult.result)
			if (activity is OnNavigate) {
				requireActivity().setResult(Activity.RESULT_OK)
				// Complete and destroy sign-in fragment once successful
				(activity as AbstractActivity).navigate(OnNavigate.MAIN)
			}
		}
	}

	override fun onSignInFailed() {
		if (!isAdded or isDetached) return
		onErrorHandler()
	}

	override fun onClick(v: View?) {
		when (v?.id) {
			binding.signup.id -> (activity as AbstractActivity).navigate(OnNavigate.SIGNUP)
			binding.forgotPassword.id -> (activity as AbstractActivity).navigate(OnNavigate.FORGOT_PASSWORD)
			binding.signin.id -> binding.signin.apply {
				clearAnimation()
				isEnabled = false
				binding.signin.setTextColor(getColor(R.color.disableTextColor))
				initPresenter(
					userName = binding.edtPhoneNumber.textString.persianToEnglishNumber,
					password = binding.edtPassword.textString
				)
			}
		}
	}
}