package com.aperfectpolygon.ergoplus.helper

import android.content.Context
import android.graphics.*
import android.graphics.Color.BLACK
import android.graphics.Color.parseColor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import java.security.MessageDigest

class RoundedCornersTransformation @JvmOverloads constructor(
	private val mBitmapPool: BitmapPool, private val mRadius: Int, margin: Int,
	cornerType: CornerType = CornerType.ALL,
) : Transformation<Bitmap?> {
	enum class CornerType {
		ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT,
		OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT, BORDER
	}

	private val mDiameter: Int = mRadius * 2
	private val mMargin: Int = margin
	private val mCornerType: CornerType = cornerType
	private var mColor: String? = null
	private var mBorder = 0

	constructor(
		context: Context?, radius: Int, margin: Int, color: String?, border: Int
	) : this(context, radius, margin, CornerType.BORDER) {
		mColor = color
		mBorder = border
	}

	@JvmOverloads
	constructor(
		context: Context?, radius: Int, margin: Int, cornerType: CornerType = CornerType.ALL,
	) : this(Glide.get(context!!).bitmapPool, radius, margin, cornerType)

	override fun transform(
		context: Context,
		resource: Resource<Bitmap?>,
		outWidth: Int,
		outHeight: Int
	): Resource<Bitmap?> {
		resource.get().also {
			mBitmapPool[it.width, it.height, Bitmap.Config.ARGB_8888].also { bitmap ->
				Paint().apply {
					isAntiAlias = true
					shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
				}.also { paint ->
					drawRoundRect(Canvas(bitmap), paint, it.width.toFloat(), it.height.toFloat())
					return BitmapResource.obtain(bitmap, mBitmapPool)!!
				}
			}
		}
	}

	private fun drawRoundRect(canvas: Canvas, paint: Paint, width: Float, height: Float) =
		(width - mMargin).also { r ->
			(height - mMargin).also { b ->
				canvas.also { c ->
					paint.also { p ->
						when (mCornerType) {
							CornerType.ALL -> c.drawRoundRect(
								RectF(mMargin.toFloat(), mMargin.toFloat(), r, b),
								mRadius.toFloat(),
								mRadius.toFloat(),
								p
							)
							CornerType.TOP_LEFT -> drawTopLeftRoundRect(c, p, r, b)
							CornerType.TOP_RIGHT -> drawTopRightRoundRect(c, p, r, b)
							CornerType.BOTTOM_LEFT -> drawBottomLeftRoundRect(c, p, r, b)
							CornerType.BOTTOM_RIGHT -> drawBottomRightRoundRect(c, p, r, b)
							CornerType.TOP -> drawTopRoundRect(c, p, r, b)
							CornerType.BOTTOM -> drawBottomRoundRect(c, p, r, b)
							CornerType.LEFT -> drawLeftRoundRect(c, p, r, b)
							CornerType.RIGHT -> drawRightRoundRect(c, p, r, b)
							CornerType.OTHER_TOP_LEFT -> drawOtherTopLeftRoundRect(c, p, r, b)
							CornerType.OTHER_TOP_RIGHT -> drawOtherTopRightRoundRect(c, p, r, b)
							CornerType.OTHER_BOTTOM_LEFT -> drawOtherBottomLeftRoundRect(c, p, r, b)
							CornerType.OTHER_BOTTOM_RIGHT -> drawOtherBottomRightRoundRect(c, p, r, b)
							CornerType.DIAGONAL_FROM_TOP_LEFT -> drawDiagonalFromTopLeftRoundRect(c, p, r, b)
							CornerType.DIAGONAL_FROM_TOP_RIGHT -> drawDiagonalFromTopRightRoundRect(c, p, r, b)
							CornerType.BORDER -> drawBorder(c, p, r, b)
						}
					}
				}
			}
		}

	private fun drawTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(
					mMargin.toFloat(),
					mMargin.toFloat(),
					(mMargin + mDiameter).toFloat(),
					(mMargin + mDiameter).toFloat()
				),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(
				RectF(
					mMargin.toFloat(),
					(mMargin + mRadius).toFloat(),
					(mMargin + mRadius).toFloat(),
					bottom
				), paint
			)
			drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
		}


	private fun drawTopRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
			drawRect(RectF(right - mRadius, (mMargin + mRadius).toFloat(), right, bottom), paint)
		}

	private fun drawBottomLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(
				RectF(
					mMargin.toFloat(),
					mMargin.toFloat(),
					(mMargin + mDiameter).toFloat(),
					bottom - mRadius
				), paint
			)
			drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
		}

	private fun drawBottomRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(right - mDiameter, bottom - mDiameter, right, bottom),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
			drawRect(RectF(right - mRadius, mMargin.toFloat(), right, bottom - mRadius), paint)
		}

	private fun drawTopRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right, bottom), paint)
		}

	private fun drawBottomRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
				mRadius.toFloat(),
				mRadius.toFloat(),
				paint
			)
			drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom - mRadius), paint)
		}

	private fun drawLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom), paint)
		}

	private fun drawRightRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
				mRadius.toFloat(), mRadius.toFloat(), paint
			)
			drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom), paint)
		}

	private fun drawOtherTopLeftRoundRect(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		canvas.apply {
			drawRoundRect(
				RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
				mRadius.toFloat(),
				mRadius.toFloat(),
				paint
			)
			drawRoundRect(
				RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
				mRadius.toFloat(),
				mRadius.toFloat(),
				paint
			)
			drawRect(
				RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom - mRadius),
				paint
			)
		}

	private fun drawOtherTopRightRoundRect(
		canvas: Canvas,
		paint: Paint,
		right: Float,
		bottom: Float
	) = canvas.apply {
		drawRoundRect(
			RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRoundRect(
			RectF(mMargin.toFloat(), bottom - mDiameter, right, bottom),
			mRadius.toFloat(),
			mRadius.toFloat(),
			paint
		)
		drawRect(
			RectF((mMargin + mRadius).toFloat(), mMargin.toFloat(), right, bottom - mRadius),
			paint
		)
	}

	private fun drawOtherBottomLeftRoundRect(
		canvas: Canvas,
		paint: Paint,
		right: Float,
		bottom: Float
	) = canvas.apply {
		drawRoundRect(
			RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRoundRect(
			RectF(right - mDiameter, mMargin.toFloat(), right, bottom),
			mRadius.toFloat(),
			mRadius.toFloat(),
			paint
		)
		drawRect(
			RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right - mRadius, bottom),
			paint
		)
	}

	private fun drawOtherBottomRightRoundRect(
		canvas: Canvas,
		paint: Paint,
		right: Float,
		bottom: Float
	) = canvas.apply {
		drawRoundRect(
			RectF(mMargin.toFloat(), mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRoundRect(
			RectF(mMargin.toFloat(), mMargin.toFloat(), (mMargin + mDiameter).toFloat(), bottom),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRect(
			RectF((mMargin + mRadius).toFloat(), (mMargin + mRadius).toFloat(), right, bottom),
			paint
		)
	}

	private fun drawDiagonalFromTopLeftRoundRect(
		canvas: Canvas,
		paint: Paint,
		right: Float,
		bottom: Float
	) = canvas.apply {
		drawRoundRect(
			RectF(
				mMargin.toFloat(),
				mMargin.toFloat(),
				(mMargin + mDiameter).toFloat(),
				(mMargin + mDiameter).toFloat()
			),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRoundRect(
			RectF(right - mDiameter, bottom - mDiameter, right, bottom),
			mRadius.toFloat(),
			mRadius.toFloat(),
			paint
		)
		drawRect(
			RectF(mMargin.toFloat(), (mMargin + mRadius).toFloat(), right - mDiameter, bottom),
			paint
		)
		drawRect(
			RectF((mMargin + mDiameter).toFloat(), mMargin.toFloat(), right, bottom - mRadius),
			paint
		)
	}

	private fun drawDiagonalFromTopRightRoundRect(
		canvas: Canvas,
		paint: Paint,
		right: Float,
		bottom: Float
	) = canvas.apply {
		drawRoundRect(
			RectF(right - mDiameter, mMargin.toFloat(), right, (mMargin + mDiameter).toFloat()),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRoundRect(
			RectF(mMargin.toFloat(), bottom - mDiameter, (mMargin + mDiameter).toFloat(), bottom),
			mRadius.toFloat(), mRadius.toFloat(), paint
		)
		drawRect(RectF(mMargin.toFloat(), mMargin.toFloat(), right - mRadius, bottom - mRadius), paint)
		drawRect(
			RectF((mMargin + mRadius).toFloat(), (mMargin + mRadius).toFloat(), right, bottom),
			paint
		)
	}

	private fun drawBorder(canvas: Canvas, paint: Paint, right: Float, bottom: Float) =
		Paint().apply {
			style = Paint.Style.STROKE
			color = if (mColor != null) parseColor(mColor) else BLACK
			strokeWidth = mBorder.toFloat()
			canvas.drawRoundRect(
				RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom),
				mRadius.toFloat(),
				mRadius.toFloat(),
				paint
			)

			// stroke
			canvas.drawRoundRect(
				RectF(mMargin.toFloat(), mMargin.toFloat(), right, bottom),
				mRadius.toFloat(),
				mRadius.toFloat(),
				this
			)
		}

	override fun updateDiskCacheKey(messageDigest: MessageDigest) = Unit

	val id: String get() = ("RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter=" + mDiameter + ", cornerType=" + mCornerType.name + ")")
}