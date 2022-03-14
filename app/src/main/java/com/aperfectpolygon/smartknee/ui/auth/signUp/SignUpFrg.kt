package com.aperfectpolygon.smartknee.ui.auth.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aperfectpolygon.smartknee.databinding.FragmentSignupBinding
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractFragment

class SignUpFrg : AbstractFragment() {
	companion object {
		val newInstance: SignUpFrg
			get() = SignUpFrg()
	}

	private lateinit var binding: FragmentSignupBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View = FragmentSignupBinding.inflate(inflater, container, false).apply {

	}.also { binding = it }.root
}