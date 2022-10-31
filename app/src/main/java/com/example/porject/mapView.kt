package com.example.porject

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.porject.databinding.FragmentMapViewBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions

val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
val PERMISSIONS_REQUEST_CODE = 100
class mapView : Fragment(), View.OnClickListener, OnMapReadyCallback, LocationListener {
    lateinit var binding: FragmentMapViewBinding
    lateinit var mView: MapView
    lateinit var mContext: Context
    lateinit var gMap : GoogleMap
    var initTime = 0L
    var pauseTime = 0L
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private var REQUEST_PERMISSTION_LOCATION = 10


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
        binding.appCompatButton.setOnClickListener{
            Toast.makeText(context, "산책을 시작합니다!!!", Toast.LENGTH_SHORT).show()
            binding.chronometer.visibility = View.VISIBLE
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            binding.chronometer.start()
        }
        binding.btn.setOnClickListener{
            Toast.makeText(context, "산책을 종료합니다!!!", Toast.LENGTH_SHORT).show()
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            val et = EditText(context)
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("오늘 하루를 작성하세요!!!")
            builder.setMessage("내용")
            builder.setMessage("산책 시간: " + pauseTime)
            builder.setView(et) //AlertDialog에 적용하기
            builder.setPositiveButton(
                "예"
            ) { dialog, which ->
                Toast.makeText(
                    context,
                    "" + et.getText().toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
            builder.setNegativeButton(
                "아니오"
            ) { dialog, which -> dialog.cancel() }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            pauseTime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()
        }
        binding.extendedFloatingActionButton.setOnClickListener {
            /*val intent = Intent (this@mapView.context, Diet::class.java)
            startActivity(intent)*/
            activity?.let {
                val intent = Intent(it, Diet::class.java)
                it.startActivity(intent)
            }
        }
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val myLocation = com.google.android.gms.maps.model.LatLng(37.554648, 126.972559)
        val marker = com.google.android.gms.maps.model.LatLng(37.554648, 126.972559)
        googleMap.addMarker(MarkerOptions().position(marker).title("test"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
    }
    override fun onStart() {
        super.onStart()
        mView.onStart()
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

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }
}

