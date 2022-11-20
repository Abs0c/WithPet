package com.example.porject

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_walking_write.view.*

import kotlinx.android.synthetic.main.walking_list.view.*


class walkingAdapter(val db: walkingDatabase, var items: List<walkingData>?):RecyclerView.Adapter<walkingAdapter.ViewHolder>(){
    lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): walkingAdapter.ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.walking_list,parent,false)
        mContext = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: walkingAdapter.ViewHolder, position: Int) {
        holder.bind(items!!.get(position), position)
    }

    override fun getItemCount(): Int {
        return items!!.size
    }
    fun getItem() :List<walkingData>?{
        return items
    }
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var index: Int? = null
        fun bind(walkingData: walkingData, position: Int){
            index = position
            walkingData._id?.let { itemView.contentsTextView.setText(it) }
        }
        fun editData(id : Int){
            Thread{
                index?.let{items!!.get(it)._id = id};
                index?.let{items!!.get(it)}?.let { db.walkingDao().update(it) };
            }.start()
            Toast.makeText(mContext, "OK", Toast.LENGTH_SHORT).show()
        }
    }

}
