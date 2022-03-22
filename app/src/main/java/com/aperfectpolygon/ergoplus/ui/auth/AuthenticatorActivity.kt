package com.aperfectpolygon.ergoplus.ui.auth

import android.app.Activity
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.Calendar.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.database.user.UserRepository
import com.aperfectpolygon.ergoplus.database.user.UserRepository.user
import com.aperfectpolygon.ergoplus.databinding.ActivityAutherticatorBinding
import com.aperfectpolygon.ergoplus.helper.ActivityHelper.moveTo
import com.aperfectpolygon.ergoplus.helper.RoundedCornersTransformation
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractActivity
import com.aperfectpolygon.ergoplus.ui.dashboard.DashboardActivity
import com.aperfectpolygon.ergoplus.utils.snackBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.RESULT_ERROR
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.getError
import com.orhanobut.logger.Logger
import java.io.File


class AuthenticatorActivity : AbstractActivity() {

	private lateinit var binding: ActivityAutherticatorBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAutherticatorBinding.inflate(layoutInflater).apply {
			setContentView(root)
			edtBirthDate
			hideKeyboard
			vBirthDate.setOnClickListener {
				Calendar.getInstance().apply {
					get(YEAR).also { year ->
						get(MONTH).also { month ->
							get(DAY_OF_MONTH).also { day ->
								DatePickerDialog(
									this@AuthenticatorActivity, { _, y, moy, dom ->
										"$dom, $moy, $y".also { edtBirthDate.setText(it) }
									}, year, month, day
								).show()
							}
						}
					}
				}
			}
			imgAvatar.setOnClickListener {
				try {
					ImagePicker.with(this@AuthenticatorActivity).cropSquare().compress(1024).galleryOnly()
						.galleryMimeTypes(arrayOf("image/jpg", "image/JPG", "image/jpeg", "image/png"))
						.maxResultSize(1080, 1080).createIntent { startForProfileImageResult.launch(it) }
				} catch (e: Exception) {
					Logger.e(e, e.message ?: "")
				}
			}
			btnSignup.setOnClickListener {
				if (edtUsername.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your username yet")
					return@setOnClickListener
				}
				if (edtMail.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your email yet")
					return@setOnClickListener
				}
				if (edtPassword.text.isNullOrEmpty()) {
					snackBar(root, "You have not entered your password yet")
					return@setOnClickListener
				}
				if (!checkbox.isChecked) {
					snackBar(root, "You have not accepted the term's of using (this App)")
					return@setOnClickListener
				}
				user = user.apply {
					username = edtUsername.text.toString()
					birthDate = edtBirthDate.text.toString()
					email = edtMail.text.toString()
				}
				binding.clPrimary.isGone = true
				binding.txtWelcome.isVisible = true
				"${getString(R.string.welcome)}\n${edtUsername.text.toString()}"
					.also { binding.txtWelcome.text = it }
				Handler(Looper.getMainLooper()).postDelayed({
					moveTo(
						this@AuthenticatorActivity,
						DashboardActivity()
					)
				}, 1000)
			}
		}
	}

	private var file: File? = null
	private var filePath: String? = null

	private val startForProfileImageResult =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
			val resultCode = result.resultCode
			val data = result.data

			when (resultCode) {
				Activity.RESULT_OK -> {
					//Image Uri will not be null for RESULT_OK
					val fileUri = data?.data
					Logger.w("fileUri --> $fileUri")
					if (fileUri == null) {
						file = null
						filePath = null
						return@registerForActivityResult
					}
					circularProgressDrawable = CircularProgressDrawable(this).apply {
						strokeWidth = 5f
						centerRadius = 30f
						start()
					}
					Glide.with(this).load(fileUri).placeholder(circularProgressDrawable).apply(
						bitmapTransform(
							RoundedCornersTransformation(
								context = this,
								radius = 250,
								margin = 0,
								color = "#00000000",
								border = 0
							)
						)
					).fallback(R.drawable.vtr_logo).into(binding.imgAvatar)
					// You can get File object from intent
					UserRepository.avatar = fileUri.toString()
					binding.txtAvatar.isGone = true
				}
				RESULT_ERROR -> Toast.makeText(this, getError(data), Toast.LENGTH_SHORT).show()
				else -> Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
			}
		}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}