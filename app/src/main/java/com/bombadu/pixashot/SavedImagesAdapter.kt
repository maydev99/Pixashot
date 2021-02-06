package com.bombadu.pixashot

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.pixashot.local.LocalData
import com.squareup.picasso.Picasso

class SavedImagesAdapter(
    val mItemClickListener: ItemClickListener,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        //Constants, if any go here

    }

    private val diffCallBack = object : DiffUtil.ItemCallback<LocalData>() {

        override fun areItemsTheSame(oldItem: LocalData, newItem: LocalData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocalData, newItem: LocalData): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return SavedImageHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.saved_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        when (holder) {
            is SavedImageHolder -> {
                holder.bind(differ.currentList[position])

            }
        }


    }

    fun getItemAt(position: Int): LocalData {
        return differ.currentList[position]
        //Use this for getting selected item data from Activity/ Fragment
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<LocalData>) {
        differ.submitList(list)
    }

    inner class SavedImageHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                mItemClickListener.onItemClick(adapterPosition)

            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: LocalData) = with(itemView) {

            val imageView = itemView.findViewById<ImageView>(R.id.saved_item_image_view)
            val commentTextView = itemView.findViewById<TextView>(R.id.saved_item_comments_text_view)
            val nameTextView = itemView.findViewById<TextView>(R.id.saved_item_name_text_view)

            itemView.setOnClickListener {

                interaction?.onItemSelected(adapterPosition, item)
                mItemClickListener.onItemClick(adapterPosition)


            }

            val url = item.url
            val name = item.name
            Picasso.get().load(url).into(imageView)
            commentTextView.text = item.comments
            nameTextView.text = "${context.getString(R.string.by)} $name"


        }


    }

    interface Interaction {
        fun onItemSelected(position: Int, item: LocalData)
    }


}



