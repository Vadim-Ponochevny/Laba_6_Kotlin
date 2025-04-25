package com.example.laba_6_kotlin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class Adapter(
    private val context: Context,
    private val list: List<String>
) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    // ViewHolder — держит ссылки на элементы одной строки списка
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewItem: View = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    // onBindViewHolder используется для инициализации и манипуляций с ячейками.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val UrlOfCurrentItem = list[position]

        Glide.with(holder.itemView.context)
            .load(UrlOfCurrentItem)
            .into(holder.viewItem as ImageView)


        holder.viewItem.setOnClickListener {
            val intent = Intent(context, PicViewerActivity::class.java)
            intent.putExtra("IMAGE_URL", UrlOfCurrentItem)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = list.size
}