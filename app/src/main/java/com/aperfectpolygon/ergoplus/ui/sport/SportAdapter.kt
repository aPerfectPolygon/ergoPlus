package com.aperfectpolygon.ergoplus.ui.sport

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.databinding.RcvSportBinding
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractAdapter
import com.aperfectpolygon.ergoplus.utils.openUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


data class Sport(val title: String, val url: String)

class SportAdapter(context: Context, items: List<Sport>) :
	AbstractAdapter<SportAdapter.SportViewHolder, Sport>(context, items) {

	inner class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val binding = RcvSportBinding.bind(itemView)
		fun onFill(position: Int) {
			binding.apply {
				root.setOnClickListener { ctxAdapter.openUrl(items[position].url) }
				txtTitle.text = items[position].title
				circularProgressDrawable = CircularProgressDrawable(ctxAdapter).apply {
					strokeWidth = 5f
					centerRadius = 30f
					start()
				}
				Handler(Looper.myLooper() ?: Looper.getMainLooper()).post {
					MediaMetadataRetriever().apply {
						setDataSource(items[position].url, HashMap())
						getFrameAtTime(2000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)?.also {
							// items[position].cache = it
							Glide.with(image).asBitmap().load(it).placeholder(circularProgressDrawable)
								.diskCacheStrategy(DiskCacheStrategy.ALL).into(image)
						}
					}.release()
				}
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		SportViewHolder(inflater.inflate(R.layout.rcv_sport, parent, false))

	override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
		holder.onFill(holder.adapterPosition)
	}
}