package com.example.porject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.databinding.FragmentMyPetBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_my_pet.*

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
    var items = mutableListOf<myPetType>()
    var adapter: myListAdapter? = null

    fun refresh(fragment: Fragment, fragmentManager: FragmentManager){
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.detach(fragment).attach(fragment).commitAllowingStateLoss()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    var check = false
    fun notifyob(){
        adapter?.notifyDataSetChanged()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        super.onStart()
        binding = FragmentMyPetBinding.inflate(layoutInflater)
        if(!MyApplication.checkAuth()) {
//            db = MyApplication.db
//            user = MyApplication.auth.currentUser?.uid.toString()
//            db.collection("pets")
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result){
//                        if (document["userUID"] as String? == user){
//                            val item = myPetType(document["petName"] as String, document["petType"] as Long, document["petWeight"] as Long, document["userUID"] as String?)
//                            items.add(item)
//                        }
//                    }
//                    adapter = context?.let { myListAdapter(it, items) }
//                    binding.listView.adapter = adapter
//                    binding.listView.layoutManager = LinearLayoutManager(context)
//                }
//        }
//        else{
            Toast.makeText(activity, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
    var user = "test"
    override fun onResume() {

        super.onResume()
        binding.addPetButton.setOnClickListener{
            activity?.let {
                val intent = Intent(activity, AddpetActivity::class.java)
                startActivity(intent)
            }
        }
        if(!MyApplication.checkAuth()){
            binding.listView.visibility = View.INVISIBLE
            add_pet_button.visibility = View.INVISIBLE
        }
        else{
            add_pet_button.visibility = View.VISIBLE
            binding.listView.visibility = View.VISIBLE
            db = MyApplication.db
            items.clear()
            db.collection("pets")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result){
                        if (document["userUID"] as String? == MyApplication.auth.currentUser?.uid){
                            val item = myPetType(document["petName"] as String, document["petType"] as Long, document["petWeight"] as Long, document["userUID"] as String?)
                            items.add(item)
                        }
                    }
                    adapter = context?.let { myListAdapter(it, items) }
                    binding.listView.adapter = adapter
                    binding.listView.layoutManager = LinearLayoutManager(context)
                }
        }
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