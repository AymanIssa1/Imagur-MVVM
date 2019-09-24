package com.example.imagurtask.ui.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.imagurtask.GALLERY_ITEM_ID_EXTRA
import com.example.imagurtask.GALLERY_ITEM_MEDIA_URL_EXTRA
import com.example.imagurtask.R
import com.example.imagurtask.ui.BaseActivity
import com.example.imagurtask.util.extensions.argument
import com.example.imagurtask.util.extensions.initializePlayer
import com.example.imagurtask.util.extensions.loadUrl
import com.example.imagurtask.util.extensions.releasePlayer
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailsActivity : BaseActivity() {

    private val itemId by argument<String>(GALLERY_ITEM_ID_EXTRA)
    private val itemMediaUrl by argument<String>(GALLERY_ITEM_MEDIA_URL_EXTRA)
    private val viewModel: DetailsViewModel by viewModel { parametersOf(itemId) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }


        if (itemMediaUrl.endsWith(".mp4")) {
            image_view.visibility = View.GONE
            video_player.visibility = View.VISIBLE
            video_player.initializePlayer(itemMediaUrl, true)
        } else {
            video_player.visibility = View.GONE
            image_view.visibility = View.VISIBLE
            image_view.loadUrl(itemMediaUrl)
        }

        viewModel.galleryUIModel.observe(this, Observer {
            when {
                it.isLoading -> {
                    // TODO: To be handled
                }
                it.isSuccess -> {
                    upVotesTextView.text = it.galleryItem?.ups.toString()
                    downVotesTextView.text = it.galleryItem?.downs.toString()
                    titleTextView.text = it.galleryItem?.title
                    descriptionTextView.text = it.galleryItem?.description
                    scoreTextView.text = it.galleryItem?.score.toString()
                }
                it.error != null -> {
                    // TODO: To be handled
                }
            }
        })

        viewModel.getGalleryItem()
    }

    override fun onStop() {
        super.onStop()
        video_player.releasePlayer()
    }
}
