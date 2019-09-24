package com.example.imagurtask.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagurtask.GALLERY_ITEM_VIDEO_TYPE
import com.example.imagurtask.R
import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.util.extensions.initializePlayer
import com.example.imagurtask.util.extensions.loadUrl
import com.example.imagurtask.util.extensions.releasePlayer
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.image_item.view.*
import kotlinx.android.synthetic.main.image_item.view.description_text_view
import kotlinx.android.synthetic.main.video_item.view.*

class GalleryAdapter(
    private val galleryDataList: ArrayList<GalleryData>,
    private val onItemClick: (itemId: String, itemMediaUrl: String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_IMAGE) {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            ImageViewHolder(itemView)
        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
            VideoViewHolder(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!galleryDataList[position].type.isNullOrBlank())
            if (galleryDataList[position].type == GALLERY_ITEM_VIDEO_TYPE)
                TYPE_VIDEO
            else
                TYPE_IMAGE
        else if (galleryDataList[position].images[0].type == GALLERY_ITEM_VIDEO_TYPE)
            TYPE_VIDEO
        else
            TYPE_IMAGE
    }

    override fun getItemCount(): Int {
        return galleryDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val galleryData = galleryDataList[position]

        val title = galleryData.title
        val media = getMediaUrl(galleryData)


        if (getItemViewType(position) == TYPE_VIDEO) {
            (holder as VideoViewHolder).descriptionTextView.text = title
            holder.playerView.initializePlayer(media)
        } else if (getItemViewType(position) == TYPE_IMAGE) {
            (holder as ImageViewHolder).descriptionTextView.text = title
            (holder as ImageViewHolder).imageView.loadUrl(media)
        }

        holder.itemView.setOnClickListener { onItemClick(galleryData.id, media) }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is VideoViewHolder) {
            holder.playerView.releasePlayer()
        }
        super.onViewRecycled(holder)
    }

    private fun getMediaUrl(galleryData: GalleryData): String {
        return if (!galleryData.type.isNullOrBlank())
            if (galleryData.type == GALLERY_ITEM_VIDEO_TYPE)
                galleryData.mp4
            else
                galleryData.link
        else if (galleryData.images[0].type == GALLERY_ITEM_VIDEO_TYPE)
            galleryData.images[0].mp4
        else
            galleryData.images[0].link
    }

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }

    inner class ImageViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.image_view
        val descriptionTextView: TextView = itemView.description_text_view
    }

    inner class VideoViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val playerView: PlayerView = itemView.video_player
        val descriptionTextView: TextView = itemView.description_text_view
    }

}