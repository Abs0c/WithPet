package com.example.porject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.example.porject.MyApplication.Companion.storage
import com.example.porject.databinding.ActivityAddpetBinding
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.net.URL

class AddpetActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddpetBinding

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try{
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddpetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it!!.data!!.data!!, 16,16)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it!!.data!!.data!!)
                var bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                //bitmap에 이미지 저장완료
                bitmap?. let{
                    binding.addPetImageView.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("error", "bitmap null")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        binding.selectPetImageButton.setOnClickListener(){
            //권한 있음
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        binding.addInputPetDataButton.setOnClickListener() {
            if (binding.addPetImageView.drawToBitmap() != null) {
                val petimage = getBitmapFromView(binding.addPetImageView)
                val petname = binding.addPetNameEdittext.text.toString()
                val pettype = binding.addPetTypeEdittext.text.toString()
                saveStore(petimage, petname, pettype)
            }
            else {
                Toast.makeText(this,"사진을 골라주세요.",Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun saveStore(petimage: Bitmap, petname: String, pettype: String){
        val baos = ByteArrayOutputStream()
        petimage.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val imagedata = baos.toByteArray()
        val db = FirebaseFirestore.getInstance()
        val colRef: CollectionReference = db.collection("pets")
        uploadImage(petname, imagedata)
        var data = myPetType(petname, pettype)
        colRef.add(data)
            .addOnSuccessListener{
                Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT)
            }
            .addOnFailureListener() {
                Log.w("에러", it)
                Toast.makeText(this,"에러", Toast.LENGTH_SHORT)
            }
    }

    private fun uploadImage(docId: String, data: ByteArray){
        var storage = MyApplication.storage
        var storageRef: StorageReference = storage.reference
        var imgRef: StorageReference = storageRef.child("images/"+docId+".jpg")
        var uploadTask = imgRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Log.d("error", "upload fail")
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
        }
        uploadTask.addOnSuccessListener {
            Log.d("success!", "success!")
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getBitmapFromView(view: View): Bitmap{
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}
