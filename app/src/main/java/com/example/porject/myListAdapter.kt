package com.example.porject

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.porject.MyApplication.Companion.storage
import javax.xml.transform.URIResolver

class myListAdapter (val context: Context, val myPetList: MutableList<myPetType>) :
    RecyclerView.Adapter<myListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.pet_card_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return myPetList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nametxtView: TextView = view.findViewById(R.id.pet_Name_Text_View)
        val typetxtView: TextView = view.findViewById(R.id.pet_Type_Text_View)
        val imageView: ImageView = view.findViewById(R.id.pet_Image_View)
        val storageReference = storage.reference

        fun bind(position: Int){
            nametxtView.text = myPetList[position].petName
            typetxtView.text = myPetList[position].petType
            val docuUri = storageReference.child("images/" + myPetList[position].petName + ".jpg").downloadUrl.addOnSuccessListener{
                Uriesult ->
                Glide.with(context).load(Uriesult).into(imageView)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}