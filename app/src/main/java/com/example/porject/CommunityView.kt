package com.example.porject

import android.content.Intent
import android.os.Build.VERSION_CODES.S
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.porject.MyApplication.Companion.db
import com.example.porject.databinding.FragmentCommunityViewBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityView.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityView : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var binding: FragmentCommunityViewBinding
    lateinit var adapterOne : CommunityBriefAdapter
    lateinit var adapterTwo : CommunityBriefAdapter
    val datas = mutableListOf<CommunityData>()
    val datas2 = mutableListOf<CommunityData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommunityViewBinding.inflate(inflater, container, false)
        binding.go1.setOnClickListener {
            var intent = Intent(context, CommunityActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)

        }
        binding.go2.setOnClickListener {
            var intent = Intent(context, QuestionCommunity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var i = 0
        var j = 0
        datas.clear()
        datas2.clear()
        db = Firebase.firestore
        //adapter = context?.let { myListAdapter(it, items) }
        adapterOne = context?.let{CommunityBriefAdapter(it, datas)}!!
        adapterTwo = context?.let{CommunityBriefAdapter(it, datas2)}!!
        db.collection("Community")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    if(i > 3) break
                    i++
                    //val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"] as Int)
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString(), document["image"].toString())
                    //val item = CommunityData()
                    datas.add(item)
                }
                adapterOne.datas = datas
                adapterOne.notifyDataSetChanged()
                //val layoutManager = LinearLayoutManager(context)
                val layoutManagerOne = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                binding.freelistBrief.layoutManager = layoutManagerOne
                binding.freelistBrief.adapter = adapterOne
            }
        db.collection("Question")
            .get()
            .addOnSuccessListener { result ->
                for (document in result){
                    if(j > 3) break
                    j++
                    //val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"] as Int)
                    val item = CommunityData(document["title"] as String, document["contents"] as String, document["time"] as String, document["userUID"] as String, document["good"].toString(), document["noteNo"].toString(), document["image"].toString())
                    //val item = CommunityData()
                    datas2.add(item)
                }
                adapterTwo.datas = datas2
                adapterTwo.notifyDataSetChanged()
                //val layoutManager = LinearLayoutManager(context)
                val layoutManagerTwo = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean {
                        return false
                    }
                }
                binding.questionBrief.layoutManager = layoutManagerTwo
                binding.questionBrief.adapter = adapterTwo
    }
}}