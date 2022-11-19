package com.example.porject

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.porject.MyApplication.Companion.db
import com.example.porject.MyApplication.Companion.storage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.pet_card_view.view.*
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
        val item = myPetList[position]

        holder.itemView.setOnClickListener{
            Toast.makeText(context, item.petName + " " + item.petType + " " + item.userUID, Toast.LENGTH_SHORT).show()
        }

        holder.petdelbtn.setOnClickListener{
            var docu = ""
            db.collection("pets").get().addOnSuccessListener {result ->
                for((i, document) in result.withIndex()){
                    val getpetName = item.petName
                    val getpetType = item.petType
                    val getuserUid = item.userUID
                    if ((document["petName"] as String) != getpetName) {
                        continue
                    }
                    if ((document["petType"] as String) != getpetType) {
                        continue
                    }
                    if ((document["userUID"] as String?) != getuserUid) {
                        continue
                    }
                    docu = document.reference.path
                    db.document(docu).delete()
                    storage.reference.child("images/" + getuserUid + "/" + getpetName + ".jpg").delete()
                    myPetList.removeAt(myPetList.indexOf(myPetType(getpetName, getpetType, getuserUid)))
                    notifyDataSetChanged()
                    break
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return myPetList.size
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nametxtView: TextView = view.findViewById(R.id.pet_Name_Text_View)
        val typetxtView: TextView = view.findViewById(R.id.pet_Type_Text_View)
        val imageView: ImageView = view.findViewById(R.id.pet_Image_View)
        val petdelbtn: Button = view.findViewById(R.id.pet_delete_btn)
        val storageReference = storage.reference

        fun bind(position: Int){
            nametxtView.text = myPetList[position].petName
            typetxtView.text = myPetList[position].petType
            storageReference.child("images/" + myPetList[position].userUID + "/" + myPetList[position].petName + ".jpg").downloadUrl.addOnSuccessListener{
                Uriresult ->
                Glide.with(context).load(Uriresult).into(imageView)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}