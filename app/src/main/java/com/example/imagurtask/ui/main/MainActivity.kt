package com.example.imagurtask.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.imagurtask.GALLERY_ITEM_ID_EXTRA
import com.example.imagurtask.GALLERY_ITEM_MEDIA_URL_EXTRA
import com.example.imagurtask.R
import com.example.imagurtask.models.GalleryData
import com.example.imagurtask.models.Section
import com.example.imagurtask.ui.AboutActivity
import com.example.imagurtask.ui.BaseActivity
import com.example.imagurtask.ui.details.DetailsActivity
import com.example.imagurtask.util.extensions.launchActivity
import com.zplesac.connectionbuddy.models.ConnectivityEvent
import com.zplesac.connectionbuddy.models.ConnectivityState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModel()
    private var galleryAdapter: GalleryAdapter? = null
    private var currentLayoutManager: RecyclerView.LayoutManager? = null
    private lateinit var galleryDataList: ArrayList<GalleryData>
    private var currentSelectedSection: Section = Section.hot
    private var showViral = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel.galleryUIModel.observe(this, Observer {
            when {
                it.isLoading -> {
                    progress_bar.visibility = View.VISIBLE
                    gallery_recycler_view.visibility = View.GONE
                }
                it.isSuccess -> {
                    progress_bar.visibility = View.GONE
                    gallery_recycler_view.visibility = View.VISIBLE
                    galleryDataList = it.galleryList!!
                    setGallery(galleryDataList)
                }
                it.error != null -> {

                }
            }
        })
    }

    private fun setGallery(galleryDataList: ArrayList<GalleryData>) {
        if (gallery_recycler_view.adapter == null) {
            galleryAdapter = GalleryAdapter(galleryDataList, onItemClick = { itemId, itemMediaUrl ->
                this.launchActivity(DetailsActivity(), Bundle().apply {
                    putString(GALLERY_ITEM_ID_EXTRA, itemId)
                    putString(GALLERY_ITEM_MEDIA_URL_EXTRA, itemMediaUrl)
                })
            })
            gallery_recycler_view.adapter = galleryAdapter
            if (currentLayoutManager == null)
                currentLayoutManager = LinearLayoutManager(this)
            gallery_recycler_view.layoutManager = currentLayoutManager
        }
    }

    override fun onConnectionChange(event: ConnectivityEvent) {
        super.onConnectionChange(event)
        if (event.state.value == ConnectivityState.CONNECTED)
            viewModel.getGallery(section = currentSelectedSection)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.change_view_settings -> {
                when {
                    compareDrawables(
                        item.icon,
                        resources.getDrawable(R.drawable.ic_list)
                    ) -> setLinearLayout(item)
                    compareDrawables(
                        item.icon,
                        resources.getDrawable(R.drawable.ic_grid)
                    ) -> setGridLayout(item)
                    compareDrawables(
                        item.icon,
                        resources.getDrawable(R.drawable.ic_staggered_grid)
                    ) -> setStaggeredGrid(item)
                }
                true
            }
            R.id.hot_section_settings -> {
                if (currentSelectedSection != Section.hot) {
                    currentSelectedSection = Section.hot
                    viewModel.getGallery(showViral = showViral, section = Section.hot)
                }
                true
            }
            R.id.top_section_settings -> {
                if (currentSelectedSection != Section.top) {
                    currentSelectedSection = Section.top
                    viewModel.getGallery(showViral = showViral, section = Section.top)
                }
                true
            }
            R.id.user_section_settings -> {
                if (currentSelectedSection != Section.user) {
                    currentSelectedSection = Section.user
                    viewModel.getGallery(showViral = showViral, section = Section.user)
                }
                true
            }
            R.id.show_viral_settings -> {
                item.isChecked = !item.isChecked
                showViral = item.isChecked
                viewModel.getGallery(showViral = showViral, section = currentSelectedSection)
                true
            }
            R.id.about_settings -> {
                this.launchActivity(AboutActivity())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setLinearLayout(menuItem: MenuItem) {
        menuItem.setIcon(R.drawable.ic_grid)
        currentLayoutManager = LinearLayoutManager(this)
        gallery_recycler_view.layoutManager = currentLayoutManager
        galleryAdapter?.notifyDataSetChanged()
    }

    private fun setGridLayout(menuItem: MenuItem) {
        menuItem.setIcon(R.drawable.ic_staggered_grid)
        currentLayoutManager = GridLayoutManager(this, 2)
        gallery_recycler_view.layoutManager = currentLayoutManager
        galleryAdapter?.notifyDataSetChanged()
    }

    private fun setStaggeredGrid(menuItem: MenuItem) {
        menuItem.setIcon(R.drawable.ic_list)
        currentLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        gallery_recycler_view.layoutManager = currentLayoutManager
        galleryAdapter?.notifyDataSetChanged()
    }

    private fun compareDrawables(drawable1: Drawable, drawable2: Drawable) =
        drawable1.constantState?.equals(drawable2.constantState)!!

}
