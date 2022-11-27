package com.example.porject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityAdapter(private val context : Context, val CommunityList: MutableList<CommunityData>) : RecyclerView.Adapter<CommunityAdapter.ViewHolder>()  {
    var datas = mutableListOf<CommunityData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.community_list, parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: CommunityAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
        val item = datas[position]
        var title = item.Title
        var contents = item.Contents
        var time = item.Time
        var userID = item.userUID
        var good = item.Good
        var noteNo = item.noteNo
        var imageCheck = item.Image
        holder.itemView.setOnClickListener{
            val intent = Intent(context, CommunityDetail::class.java)
            intent.putExtra("title", title)
            intent.putExtra("noteNo", noteNo)
            intent.putExtra("contents", contents)
            intent.putExtra("time", time)
            intent.putExtra("userID", userID)
            intent.putExtra("good", good)
            intent.putExtra("image", imageCheck)
            context.startActivity(intent)
        }
    }
    inner class ViewHolder(view : View) :RecyclerView.ViewHolder(view){
        val Title : TextView = itemView.findViewById(R.id.community_title_brief)
        val Contents : TextView = itemView.findViewById(R.id.community_contents_brief)
        val Time : TextView = itemView.findViewById(R.id.community_time)
        val ID : TextView = itemView.findViewById(R.id.community_id)
        val image : ImageView = itemView.findViewById(R.id.imagecheck)
        fun bind(item : CommunityData){
            Title.text = item.Title
            Contents.text = item.Contents
            Time.text = item.Time
            ID.text = item.userUID
            if(item.Image.equals("1")) image.visibility = View.VISIBLE
        }
    }

}