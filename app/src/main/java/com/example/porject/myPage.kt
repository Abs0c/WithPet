package com.example.porject

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.porject.databinding.FragmentMyPageBinding


class myPage : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentMyPageBinding
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.btnDiet.setOnClickListener {
            val intent = Intent(activity, Diet::class.java)
            startActivity(intent)
        }
        binding.add.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }
        binding.needLogin.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_right_enter, R.anim.slide_right_exit)
        }
        binding.btnSettingFrag.setOnClickListener{
            val permissionsupporter = PermissionSupport(requireActivity(), requireContext())
            if (permissionsupporter.checkPermission()){
                Toast.makeText(context, "권한 설정이 이미 완료되어 있습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context, "권한 설정중입니다.", Toast.LENGTH_SHORT).show()
                permissionsupporter.requestPermission()
            }
        }
        //binding.btnInfo.setOnClickListener{
            //Toast.makeText(activity, "미구현된 기능입니다.", Toast.LENGTH_SHORT).show()
        //}

        binding.btnHelp.setOnClickListener{
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/gLN28fPe"))
            startActivity(intent)

        }
        binding.btnCommunity.setOnClickListener {
            if(MyApplication.checkAuth()){
                //var intent = Intent(activity, CommunityActivity::class.java)
                var intent = Intent(activity, activity_mypet()::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            }
            else{
                Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }
    override fun onStart(){
        super.onStart()
        if(!MyApplication.checkAuth()){
            binding.needLogin.setText("로그인이 필요합니다.")
            binding.add.visibility = View.VISIBLE
            binding.btnCommunity.visibility = View.INVISIBLE
        }
        else{
            binding.needLogin.setText("${MyApplication.email}로 로그인하셨습니다.")
            binding.add.visibility = View.INVISIBLE
            binding.btnCommunity.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }
    private fun setOnClickListener(){
    }

    override fun onClick (v: View){
    }
}