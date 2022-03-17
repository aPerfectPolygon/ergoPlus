package com.aperfectpolygon.smartknee.ui.auth

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.icu.util.Calendar.*
import android.os.Bundle
import androidx.core.view.isGone
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.smartknee.database.user.UserRepository
import com.aperfectpolygon.smartknee.database.user.UserRepository.user
import com.aperfectpolygon.smartknee.databinding.ActivityAutherticatorBinding
import com.aperfectpolygon.smartknee.helper.ActivityHelper.moveTo
import com.aperfectpolygon.smartknee.helper.RoundedCornersTransformation
import com.aperfectpolygon.smartknee.helper.abstracts.AbstractActivity
import com.aperfectpolygon.smartknee.ui.dashboard.DashboardActivity
import com.aperfectpolygon.smartknee.utils.snackBar
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
						.maxResultSize(1080, 1080).cropSquare().start()
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
				moveTo(this@AuthenticatorActivity, DashboardActivity())
			}
		}
	}

	private var file: File? = null
	private var filePath: String? = null

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		Logger.w("requestCode --> $requestCode, resultCode --> $resultCode")
		when (resultCode) {
			Activity.RESULT_OK -> {
				//Image Uri will not be null for RESULT_OK
				val fileUri = data?.data
				Logger.w("fileUri --> $fileUri")
				if (fileUri == null) {
					file = null
					filePath = null
					return
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
							radius = 200,
							margin = 0,
							color = "#00000000",
							border = 0
						)
					)
				).into(binding.imgAvatar)
				// You can get File object from intent
				file = ImagePicker.getFile(data)!!
				// You can also get File Path from intent
				filePath = ImagePicker.getFilePath(data)!!
				UserRepository.avatar = fileUri.toString()
				Logger.w("filePath --> $filePath")
				binding.txtAvatar.isGone = true
			}
			RESULT_ERROR -> snackBar(binding.root, getError(data)).show()
			else -> snackBar(binding.root, "Task Cancelled").show()
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	override lateinit var circularProgressDrawable: CircularProgressDrawable
}