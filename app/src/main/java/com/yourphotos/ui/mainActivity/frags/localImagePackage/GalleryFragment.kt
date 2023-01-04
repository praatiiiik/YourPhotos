package com.yourphotos.ui.mainActivity.frags.localImagePackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qtalk.recyclerviewfastscroller.RecyclerViewFastScroller
import com.yourphotos.R
import com.yourphotos.getImages.sharedStorage.LocalImageViewModel
import com.yourphotos.model.ShowImage
import com.yourphotos.ui.imageShowing.AllImagesAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GalleryFragment : Fragment() {

    private val localImageViewModel : LocalImageViewModel by activityViewModels()
    private val list = ArrayList<ShowImage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

//    private lateinit var imageRecyclerViewFastScroller : RecyclerViewFastScroller
    private lateinit var imageRecyclerView : RecyclerView
    private lateinit var imageViewAdapter: AllImagesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        loadPhotosFromExternalStorage()
    }

    override fun onStart() {
        super.onStart()
        localImageViewModel.getLocalImage()
    }

    private fun initRecyclerView(view: View){
        imageRecyclerView = view.findViewById(R.id.imageRecyclerView)
        imageViewAdapter = AllImagesAdapter(requireContext(),this::openImagePager)
        val layoutManager = GridLayoutManager(requireContext(),3)
        imageRecyclerView.layoutManager = layoutManager
        imageRecyclerView.adapter = imageViewAdapter


        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (imageViewAdapter.getItemViewType(position)) {
                    R.layout.text_section -> 3
                    R.layout.image_rv -> 1
                    else -> 1
                }
            }
        }
    }

    private fun loadPhotosFromExternalStorage() {
        lifecycleScope.launch {
            localImageViewModel.showImage
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    if(it!=null){
                        list.clear()
                        list.addAll(it)
                        imageViewAdapter.submitList(it.toMutableList())
                    }
                }
        }
    }

    private fun openImagePager(imageId:Long){
        localImageViewModel.getClickedImagePos(imageId)
        openFrag(R.id.imageViewFragment)
    }

    private fun openFrag(id:Int){
        findNavController().navigate(id)
    }
}