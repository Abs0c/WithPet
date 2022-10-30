package com.example.porject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.MyApplication.Companion.storage
import com.example.porject.databinding.FragmentMyPetBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [myPet.newInstance] factory method to
 * create an instance of this fragment.
 */
class myPet : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMyPetBinding
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onStart()
        binding = FragmentMyPetBinding.inflate(inflater, container, false)
        if(!MyApplication.checkAuth()){
            Toast.makeText(activity, "로그인이 필요합니다.", Toast.LENGTH_LONG).show()
        }
        else{
            binding = FragmentMyPetBinding.inflate(inflater, container, false)
            db = MyApplication.db
            val items = mutableListOf<myPetType>()
            val adapter = context?.let { myListAdapter(it, items) }

            db.collection("pets")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        val item = myPetType(document["petName"] as String, document["petType"] as String)
                        items.add(item)

                    }
                    binding.listView.adapter = adapter
                    binding.listView.layoutManager = LinearLayoutManager(context)
                }
            binding.addPetButton.setOnClickListener{
                activity?.let {
                    val intent = Intent(activity, AddpetActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment myPet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            myPet().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}