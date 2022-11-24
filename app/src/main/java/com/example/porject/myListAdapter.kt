package com.example.porject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.porject.MyApplication.Companion.db
import com.example.porject.MyApplication.Companion.storage

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
        val getpetName = item.petName
        val getpetType = item.petType
        val getpetWeight = item.petWeight
        val getuserUid = item.userUID
        var docu = ""

        holder.itemView.setOnClickListener{
            db.collection("pets").get().addOnSuccessListener { result ->
                for (document in result) {
                    if ((document["petName"] as String) != getpetName) {
                        continue
                    }
                    if ((document["petType"] as String) != getpetType) {
                        continue
                    }
                    if ((document["petWeight"] as Long) != getpetWeight){
                        continue
                    }
                    if ((document["userUID"] as String?) != getuserUid) {
                        continue
                    }
                    docu = document.reference.path
                    val intent = Intent(context, AddpetActivity::class.java)
                    intent.putExtra("petname", getpetName)
                    intent.putExtra("pettype", getpetType)
                    intent.putExtra("petweight", getpetWeight)
                    intent.putExtra("docuname", docu)
                    context.startActivity(intent)
                    notifyDataSetChanged()
                    break
                }
            }
        }

        holder.petdelbtn.setOnClickListener{
            db.collection("pets").get().addOnSuccessListener {result ->
                for(document in result){
                    if ((document["petName"] as String) != getpetName) {
                        continue
                    }
                    if ((document["petType"] as String) != getpetType) {
                        continue
                    }
                    if ((document["petWeight"] as Long) != getpetWeight){
                        continue
                    }
                    if ((document["userUID"] as String?) != getuserUid) {
                        continue
                    }
                    docu = document.reference.path
                    db.document(docu).delete()
                    storage.reference.child("images/" + getuserUid + "/" + getpetName + ".jpg").delete()
                    myPetList.removeAt(myPetList.indexOf(myPetType(getpetName, getpetType, getpetWeight, getuserUid)))
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