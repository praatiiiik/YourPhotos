package com.yourphotos.ui.imageShowing

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yourphotos.R
import com.yourphotos.getImages.sharedStorage.LocalImageViewModel
import com.yourphotos.model.ImageData
import kotlinx.android.synthetic.main.image_viewpager_item.view.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryFullScreenFragment : DialogFragment() {

    private var imageList = ArrayList<ImageData>()
    private var selectedPosition: Int = 0

    lateinit var viewPager: ViewPager

    lateinit var galleryPagerAdapter: GalleryPagerAdapter
    private val localImageViewModel : LocalImageViewModel by activityViewModels()

    private lateinit var scaleGestureDetector: ScaleGestureDetector

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_full_screen, container, false)
        viewPager = view.findViewById(R.id.fullScreenImageVP)

        galleryPagerAdapter = GalleryPagerAdapter()
        loadPhotosFromExternalStorage()
        localImageViewModel.getImageForViewPager()
        return view
    }


    private fun loadPhotosFromExternalStorage() {
        lifecycleScope.launch {
            localImageViewModel.imageData
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    if(it!=null){
                        imageList.addAll(it)
                        selectedPosition = getImagePos(it,localImageViewModel.clickedData)
                        viewPager.adapter = galleryPagerAdapter
                        viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
                        viewPager.setPageTransformer(true, ZoomOutPageTransformer())
                        setCurrentItem(selectedPosition)
                    }
                }
        }
    }

    private fun getImagePos(imageList:List<ImageData>, id:Long):Int{
        var pos=0;
        while (pos<imageList.size){
            if(imageList[pos].id==id)return pos
            pos++;
        }
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    private fun setCurrentItem(position: Int) {
        viewPager.setCurrentItem(position, false)
    }



    // viewpager page change listener
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
            }

            override fun onPageScrollStateChanged(arg0: Int) {
            }
        }

    // gallery adapter
    inner class GalleryPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.image_viewpager_item, container, false)

            val image = imageList[position]

            // load image
            Glide.with(context!!)
                .load(image.contentUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view.ivFullscreenImage)
            container.addView(view)

            view.frameLayout.onPinchZoomListener(object : ZoomLayout.PinchZoomListener {
                override fun onPinchZoom(zoom: Float) {
                    /**
                     * [closeBtn] will be fade-in and fade-out when the Image View zoom-in and zoom-out accordingly
                     * */
                }

            })

            return view
        }

        override fun getCount(): Int {
            return imageList.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj as View
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }

    }
}