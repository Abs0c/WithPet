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

        fun bind(position: Int){
            nametxtView.text = myPetList[position].petName
            typetxtView.text = myPetList[position].petType
            imageView.setImageURI(myPetList[position].petImageUri)
        }
    }

    /*
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.pet_card_view, parent, false)
        val petname = view.findViewById<TextView>(R.id.pet_Name_Text_View)
        val pettype = view.findViewById<TextView>(R.id.pet_Type_Text_View)
        val petimage = view.findViewById<ImageView>(R.id.pet_Image_View)

        val imgRef=MyApplication.storage
            .reference
            .child("images/${petname}.jpg")
        imgRef.downloadUrl.addOnCompleteListener{ task ->
            if (task.isSuccessful){
                 petimage.setImageURI(task.result)
            }
        }

        val user = myPetList[position]
        petname.text = user.PetName
        pettype.text = user.PetType

        return view
    }
     */

    override fun getItemId(position: Int): Long {
        return 0
    }
}