package com.aperfectpolygon.ergoplus.ui.sport

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.aperfectpolygon.ergoplus.R
import com.aperfectpolygon.ergoplus.databinding.RcvSportBinding
import com.aperfectpolygon.ergoplus.helper.abstracts.AbstractAdapter
import com.aperfectpolygon.ergoplus.model.Sport
import com.aperfectpolygon.ergoplus.utils.openUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL


class SportAdapter(context: Context, items: List<Sport>) :
	AbstractAdapter<SportAdapter.SportViewHolder, Sport>(context, items) {

	inner class SportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val binding = RcvSportBinding.bind(itemView)
		fun onFill(position: Int) {
			items[position].apply {
				binding.apply {
					itemView.setOnClickListener { ctxAdapter.openUrl(items[position].url) }
					txtTitle.text = items[position].title
					imgThumbnail.setImageResource(items[position].thumbnail)
				}
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
		SportViewHolder(RcvSportBinding.inflate(inflater, parent, false).root)

	override fun onBindViewHolder(holder: SportViewHolder, position: Int) {
		holder.onFill(holder.adapterPosition)
	}
}