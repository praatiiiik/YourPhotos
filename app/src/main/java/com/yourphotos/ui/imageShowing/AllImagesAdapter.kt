package com.yourphotos.ui.imageShowing

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yourphotos.R
import com.yourphotos.model.ImageData
import com.yourphotos.model.ShowImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllImagesAdapter(val context: Context, private val openImagePager: (Long) -> Unit) :
    ListAdapter<ShowImage, RecyclerView.ViewHolder>(DiffUtil()) {

    class ImageViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val image =  view.findViewById<ImageView>(R.id.image)
        fun bindView(imageId:Long, onItemClicked: (Long) -> Unit,callback: (itemView: View, adapterPosition: Int) -> Unit) {
            image.setOnClickListener { onItemClicked(imageId )}
            return callback(view, adapterPosition)
        }
    }

    class TextViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(callback: (itemView: View, adapterPosition: Int) -> Unit) {
            return callback(view, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.text_section) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.text_section, parent, false)
            TextViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_rv, parent, false)
            ImageViewHolder(view)
        }
    }


    override fun getItemViewType(position: Int): Int {
        if (getItem(position).imageData == null) return R.layout.text_section
        return R.layout.image_rv
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            holder.bindView(getItem(position).imageData!!.id,openImagePager) { itemView, _ ->
                setUpImage(itemView, getItem(position).imageData, context)
            }
        } else if (holder is TextViewHolder) {
            holder.bindView { itemView, _ ->
                setSection(itemView, getItem(position).date)
            }
        }
    }


    private fun setSection(view: View, data: String?) {
        val thumbnailSection = view.findViewById<TextView>(R.id.thumbnail_section)
        thumbnailSection.text = data ?: ""
    }

    private fun setUpImage(view: View, data: ImageData?, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val view1 = view.findViewById<ImageView>(R.id.image)
          //  view1.layout(0, 0, 0, 0)
            val builder = Glide.with(context)
                .load(Uri.parse(data?.contentUri))
                .transition(DrawableTransitionOptions.withCrossFade())
            launch(Dispatchers.Main) {
                builder.into(view1)
            }
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<ShowImage>() {
        override fun areItemsTheSame(oldItem: ShowImage, newItem: ShowImage): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ShowImage, newItem: ShowImage): Boolean {
            return oldItem == newItem
        }
    }
}