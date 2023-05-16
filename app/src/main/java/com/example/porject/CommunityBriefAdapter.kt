package com.example.porject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityBriefAdapter (private val context : Context, val CommunityList: MutableList<CommunityData>) : RecyclerView.Adapter<CommunityBriefAdapter.ViewHolder>(){
    var datas = mutableListOf<CommunityData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityBriefAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.community_list_brief, parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return datas.size
    }
    override fun onBindViewHolder(holder: CommunityBriefAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
        val item = datas[position]
        var title = item.Title
        var contents = item.Contents
        var time = item.Time
        var userID = item.userUID
        var good = item.Good
        var noteNo = item.noteNo
        var imagecheck = item.Image
    }
    inner class ViewHolder(view : View) :RecyclerView.ViewHolder(view){
        val Title : TextView = itemView.findViewById(R.id.community_title_brief)
        val Contents : TextView = itemView.findViewById(R.id.community_contents_brief)
        var image : ImageView = itemView.findViewById(R.id.imagecheck_biref)
        fun bind(item : CommunityData){
            Title.text = item.Title
            Contents.text = item.Contents
            if(item.Image.equals("1")) image.visibility = View.VISIBLE
        }
    }
}
