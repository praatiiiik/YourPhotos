package com.yourphotos.ui.mainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yourphotos.R
import com.yourphotos.ui.mainActivity.frags.cloudImage.CloudImageFragment
import com.yourphotos.ui.mainActivity.frags.localImagePackage.GalleryFragment


class LocalCloudFragment : Fragment() {

    private lateinit var imagesViewPager : ViewPager2
    private lateinit var imageTypeTabs : TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_local_cloud, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View){
        imagesViewPager = view.findViewById(R.id.imagesViewPager)
        imageTypeTabs = view.findViewById(R.id.imageTypeTabs)
        clickListeners(view)
        val adapter = ViewPagerAdapter(requireActivity())
        adapter.addFragment(GalleryFragment(), "Gallery")
        adapter.addFragment(CloudImageFragment(), "Cloud")
        imagesViewPager.adapter = adapter
        imagesViewPager.currentItem = 0
        TabLayoutMediator(imageTypeTabs, imagesViewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    private fun clickListeners(view: View){
        val profileButton = view.findViewById<ImageView>(R.id.profileButton)
        profileButton.setOnClickListener {
            findNavController().navigate(R.id.action_localCloudFragment_to_profileFragment)
        }
    }
}