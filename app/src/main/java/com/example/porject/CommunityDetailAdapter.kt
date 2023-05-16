package com.example.porject

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.porject.MyApplication.Companion.db
import com.example.porject.MyApplication.Companion.storage

class CommunityDetailAdapter(private val context : Context) : RecyclerView.Adapter<CommunityDetailAdapter.ViewHolder>(){
    var datas = mutableListOf<CommunityData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityDetailAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.comment_list, parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return datas.size
    }
    override fun onBindViewHolder(holder: CommunityDetailAdapter.ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class ViewHolder(view : View) :RecyclerView.ViewHolder(view){
        var db = MyApplication.db
        var userid = MyApplication.email.toString()
        val Title = ""
        val Comment : TextView = itemView.findViewById(R.id.commentcontents)
        val Time : TextView = itemView.findViewById(R.id.commenttime)
        val ID : TextView = itemView.findViewById(R.id.commentID)
        val delete = itemView.findViewById<ImageView>(R.id.commentsdelete)
        fun bind(item : CommunityData){
            var commentNo = item.noteNo
            var noteNo = item.Title
            userid = userid.substring(0, userid.indexOf("@"))
            Comment.text = item.Contents
            Time.text = item.Time
            ID.text = item.userUID
            if(item.userUID.equals(userid) || item.userUID.equals(MyApplication.email.toString())){
                delete.visibility = View.VISIBLE
            }
            delete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("").setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("네",
                        DialogInterface.OnClickListener { dialog, which ->
                            db.collection("Community").document("$noteNo").collection("Comments").document("$commentNo").delete()
                            db.collection("Question").document("$noteNo").collection("Comments").document("$commentNo").delete()
                            Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            datas.remove(item)
                        }).setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->  })
                builder.show()
                /*db.collection("Community").document("$noteNo").collection("Comments").document("$commentNo").delete()
                db.collection("Question").document("$noteNo").collection("Comments").document("$commentNo").delete()
                Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()*/
            }
        }
    }
}