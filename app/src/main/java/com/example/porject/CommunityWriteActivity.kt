package com.example.porject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.example.porject.databinding.ActivityCommunityWriteBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CommunityWriteActivity : AppCompatActivity() {
    lateinit var binding : ActivityCommunityWriteBinding
    val db = FirebaseFirestore.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    var imagepicked = "0"
    var storage = MyApplication.storage
    var storageRef: StorageReference = storage.reference
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar4 = findViewById<Toolbar>(R.id.toolbar9)
        setSupportActionBar(toolbar4)
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")
        val formatted = current.format(formatter)
        var mode = intent.getStringExtra("mode")
        binding.communitywriteSave.setOnClickListener {
            if(binding.communitywriteTitle.equals("")) Toast.makeText(this, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
            else{
                var Title = binding.communitywriteTitle.text.toString()
                var Contents = binding.communitywriteContents.text.toString()
                var Time = formatted
                var Userid = "비회원"
                if(MyApplication.checkAuth()){
                    Userid = MyApplication.email.toString()
                    Userid = Userid.substring(0, Userid.indexOf("@"))
                }
                var Good = "0"
                var noteNo = (10000000000000 - System.currentTimeMillis()).toString()
                if (mode != null) {
                    writeCommunity(Title, Contents, Time, Userid, Good, noteNo, mode, imagepicked)
                    if(imagepicked == "1"){
                        var image = getBitmapFromView(binding.communitywriteImage)
                        val baos = ByteArrayOutputStream()
                        image.compress(Bitmap.CompressFormat.JPEG, 70, baos)
                        val imagedata = baos.toByteArray()
                        uploadImage(noteNo, imagedata)
                    }
                }
            }
        }
        binding.communitywriteCancel.setOnClickListener {
            finish()
        }
        binding.communitywriteImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }
    }
    private fun uploadImage(noteNo: String, data: ByteArray){
        val imgRef: StorageReference = storageRef.child("community/"+noteNo+".jpg")
        val uploadTask = imgRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Log.d("error", "upload fail")
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnSuccessListener {
            Log.d("success!", "success!")

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        try{
            val calRatio = calculateInSampleSize(it!!.data!!.data!!, 200,200)
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val inputStream = contentResolver.openInputStream(it.data!!.data!!)
            val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
            inputStream!!.close()
            //bitmap에 이미지 저장완료
            bitmap?. let{
                binding.communitywriteImage.setImageBitmap(bitmap)
                imagepicked = "1"
            } ?: let{
                Log.d("error", "bitmap null")
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try{
            val inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
        val (height: Int, width: Int) = options.run {outHeight to outWidth}
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth){
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_down_enter, R.anim.slide_down_exit)
    }
    fun writeCommunity(title : String, contents : String, time : String, userid : String, good : String, noteNo : String, mode : String, image : String){
        val data = CommunityData(title, contents, time, userid, good, noteNo, image)
        val colRef = db.collection("Community")
        db.collection("$mode").document(noteNo).set(data)
        //db.collection("Community").document(noteNo).collection("Comment").document(noteNo).set(data)
        finish()
        /*colRef.add(data).addOnSuccessListener {
            Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT).show()
            finish()
        }
            .addOnFailureListener{
                Toast.makeText(this,"에러", Toast.LENGTH_SHORT).show()
            }*/

    }
    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}