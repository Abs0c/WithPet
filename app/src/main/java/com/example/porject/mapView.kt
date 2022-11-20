package com.example.porject

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.porject.databinding.FragmentMapViewBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.text.SimpleDateFormat
import java.util.*

val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
val PERMISSIONS_REQUEST_CODE = 100
lateinit var bitmap : Bitmap
class mapView : Fragment(), View.OnClickListener, OnMapReadyCallback, LocationListener {
    lateinit var binding: FragmentMapViewBinding
    lateinit var mView: MapView
    lateinit var mContext: Context
    lateinit var gMap : GoogleMap
    var initTime = 0L
    var pauseTime = 0L
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var REQUEST_PERMISSTION_LOCATION = 10
    /////////////////////////
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    /////////
    fun checkPermissionForLocation(context: Context): Boolean {
        Log.d(ContentValues.TAG, "checkPermissionForLocation()")
        // Android 6.0 Marshmallow 이상에서는 지리 확보(위치) 권한에 추가 런타임 권한이 필요합니다.
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(ContentValues.TAG, "checkPermissionForLocation() 권한 상태 : O")
                true
            } else {
                val activity = context as Activity
                // 권한이 없으므로 권한 요청 알림 보내기
                Log.d(ContentValues.TAG, "checkPermissionForLocation() 권한 상태 : X")
                ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_LOCATION)
                false
            }
        } else {
            true
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d(ContentValues.TAG, "onRequestPermissionsResult()")
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(ContentValues.TAG, "onRequestPermissionsResult() _ 권한 허용 클릭")
                startLocationUpdates()
                // View Button 활성화 상태 변경
            } else {
                Log.d(ContentValues.TAG, "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(activity, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    ///////
    protected fun startLocationUpdates() {
        Log.d(ContentValues.TAG, "startLocationUpdates()")
        val activity = context as Activity
        //FusedLocationProviderClient의 인스턴스를 생성.

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(ContentValues.TAG, "startLocationUpdates() 두 위치 권한중 하나라도 없는 경우 ")
            return
        }
        Log.d(ContentValues.TAG, "startLocationUpdates() 위치 권한이 하나라도 존재하는 경우")
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청합니다.
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }


    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            Log.d(ContentValues.TAG, "onLocationResult()")
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation)
        }
    }
    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    var polyLineOptions = PolylineOptions().width(5f).color(Color.RED)
    var startCheck = false
    override fun onLocationChanged(location: Location) {
        Log.d(ContentValues.TAG, "onLocationChanged()")
        mLastLocation = location
        val date: Date = Calendar.getInstance().time
        val simpleDateFormat = SimpleDateFormat("hh:mm:ss a")
        val latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        polyLineOptions.add(latLng)
        gMap.addPolyline(polyLineOptions)
        if(startCheck == false){
            val marker = com.google.android.gms.maps.model.LatLng(mLastLocation.latitude, mLastLocation.longitude)
            gMap.addMarker(MarkerOptions().position(marker).title("시작 지점"))
            binding.locationLoading.visibility = View.INVISIBLE
            binding.chronometer.visibility = View.VISIBLE
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            binding.chronometer.start()
            binding.btnStart.visibility = View.INVISIBLE
            binding.btnStop.visibility = View.VISIBLE
            binding.onStop.visibility = View.INVISIBLE
            binding.onGoing.visibility = View.VISIBLE
            binding.onGoing.playAnimation()
            startCheck = true
        }
        binding.txtTime.text = "Updated at : " + simpleDateFormat.format(date) // 갱신된 날짜
        binding.txtLat.text = "LATITUDE : " + mLastLocation.latitude // 갱신 된 위도
        binding.txtLong.text = "LONGITUDE : " + mLastLocation.longitude // 갱신 된 경도
    }
    // 위치 업데이터를 제거 하는 메서드
    private fun stoplocationUpdates() {
        if(startCheck){
            Log.d(ContentValues.TAG, "stoplocationUpdates()")
            // 지정된 위치 결과 리스너에 대한 모든 위치 업데이트를 제거
            mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
            startCheck = false
        }
    }
    ///////////

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapViewBinding.inflate(inflater, container, false)
        mView = binding.root.findViewById(R.id.viewMap) as MapView
        mView.onCreate(savedInstanceState)
        mView.getMapAsync(this)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLocationRequest =  LocationRequest.create().apply {
            interval = 2000 // 업데이트 간격 단위(밀리초)
            fastestInterval = 1000 // 가장 빠른 업데이트 간격 단위(밀리초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 정확성
            maxWaitTime= 2000 // 위치 갱신 요청 최대 대기 시간 (밀리초)
        }

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        val myLocation = com.google.android.gms.maps.model.LatLng(37.554648, 126.972559)
        val marker = com.google.android.gms.maps.model.LatLng(37.554648, 126.972559)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        
    }
    override fun onStart() {
        super.onStart()
        mView.onStart()
        binding.btnStart.setOnClickListener{
            if (checkPermissionForLocation(requireActivity())){
                polyLineOptions = PolylineOptions().width(5f).color(Color.RED)
                gMap.clear()
                startLocationUpdates()
                binding.locationLoading.visibility = View.VISIBLE
                binding.locationLoading.playAnimation()
                /*binding.chronometer.visibility = View.VISIBLE
                binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
                binding.chronometer.start()
                binding.btnStart.visibility = View.INVISIBLE
                binding.btnStop.visibility = View.VISIBLE
                binding.onStop.visibility = View.INVISIBLE
                binding.onGoing.visibility = View.VISIBLE
                binding.onGoing.playAnimation()*/
            }
            else Toast.makeText(context, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.btnStop.setOnClickListener{
            if(startCheck){
                Toast.makeText(context, "산책을 종료합니다!!!", Toast.LENGTH_SHORT).show()
                pauseTime = SystemClock.elapsedRealtime() - binding.chronometer.base
                stoplocationUpdates()
                binding.chronometer.stop()
                val et = EditText(context)
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle("오늘 하루를 작성하세요!!!")
                builder.setMessage("내용")
                builder.setMessage("산책 시간: " + (pauseTime/1000)/60 + "분 " + (pauseTime/1000)%60 +"초")
                builder.setView(et) //AlertDialog에 적용하기
                builder.setPositiveButton(
                    "예"
                ) { dialog, which ->
                    Toast.makeText(
                        context,
                        "" + et.getText().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    gMap.snapshot {
                        it?.let {
                            var intent = Intent(context, AddEitNoteActivity::class.java)
                            bitmap = it
                            intent.putExtra("check", true)
                            startActivity(intent)
                        }
                    }
                }
                builder.setNegativeButton(
                    "아니오"
                ) { dialog, which -> dialog.cancel() }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                pauseTime = 0L
                binding.chronometer.visibility = View.INVISIBLE
                binding.chronometer.base = SystemClock.elapsedRealtime()
                val marker = com.google.android.gms.maps.model.LatLng(mLastLocation.latitude, mLastLocation.longitude)
                gMap.addMarker(MarkerOptions().position(marker).title("종료 지점"))
                binding.btnStart.visibility = View.VISIBLE
                binding.btnStop.visibility = View.INVISIBLE
                binding.onGoing.visibility = View.INVISIBLE
                binding.onStop.visibility = View.VISIBLE
                binding.onStop.playAnimation()
            }


        }
        binding.extendedFloatingActionButton.setOnClickListener {
            /*val intent = Intent (this@mapView.context, Diet::class.java)
            startActivity(intent)*/
            //val intent = Intent(context, walking::class.java)
            //startActivity(intent)
            /*activity?.let {
                val intent = Intent(it, walking::class.java)
                it.startActivity(intent)
            }*/
        }
    }
    override fun onStop() {
        super.onStop()
        mView.onStop()
    }
    override fun onResume() {
        super.onResume()
        mView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mView.onPause()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mView.onLowMemory()
    }
    override fun onDestroy() {
        mView.onDestroy()
        super.onDestroy()
    }
}



