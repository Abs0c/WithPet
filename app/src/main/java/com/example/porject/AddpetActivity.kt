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
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.porject.databinding.ActivityAddPetBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class AddpetActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPetBinding
    var imagepicked = false
    val useruid = MyApplication.auth.currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    var storage = MyApplication.storage
    var storageRef: StorageReference = storage.reference
    val petList = arrayOf("", "강아지", "고양이")

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("")
        val toolbar3 = findViewById<Toolbar>(R.id.toolbar3)
        setSupportActionBar(toolbar3)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //강아지, 고양이 선택하는 스피너 값 설정
        val spinner = binding.addPetTypeSpinner
        ArrayAdapter.createFromResource(
            this,
            R.array.pet,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        val getextrapetname= intent.getStringExtra("petname")
        val getextrapettype= intent.getLongExtra("pettype", 1)

        val getextrapetweight= intent.getDoubleExtra("petweight", 0.0)// 바꿔보는 부분

        val getdocuname= intent.getStringExtra("docuname")
        if (getextrapetname != null && getextrapettype != null){
            binding.addPetNameEdittext.setText(getextrapetname)
            binding.addPetTypeSpinner.setSelection(getextrapettype.toInt())
            binding.addPetWeightEdittext.setText(getextrapetweight.toString())
            storageRef.child("images/" + useruid + "/" + getextrapetname+ ".jpg").downloadUrl.addOnSuccessListener{
                    Uriresult ->
                Glide.with(this).load(Uriresult).into(binding.addPetImageView)
                imagepicked = true
            }
        }

        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it!!.data!!.data!!, 64,64)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                val inputStream = contentResolver.openInputStream(it.data!!.data!!)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                //bitmap에 이미지 저장완료
                bitmap?. let{
                    binding.addPetImageView.setImageBitmap(bitmap)
                    imagepicked = true
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
            if (!imagepicked) {
                Toast.makeText(this,"사진을 골라주세요.",Toast.LENGTH_SHORT).show()
            }
            else if (binding.addPetNameEdittext.text.toString() == ""){
                Toast.makeText(this,"동물의 이름을 적어주세요.",Toast.LENGTH_SHORT).show()
            }
            else if (binding.addPetTypeSpinner.selectedItem == "종을 선택하세요"){
                Toast.makeText(this,"동물의 종류를 골라주세요.",Toast.LENGTH_SHORT).show()
            }
            else if (binding.addPetWeightEdittext.text.toString() == ""){
                Toast.makeText(this,"동물의 몸무게를 적어주세요.",Toast.LENGTH_SHORT).show()
            }
            else {
                val petimage = getBitmapFromView(binding.addPetImageView)
                val petname = binding.addPetNameEdittext.text.toString()
                val selectedpettype = binding.addPetTypeSpinner.selectedItem.toString()
                var pettype = "0".toLong()
                if (selectedpettype == "강아지"){
                    pettype = "1".toLong()
                }
                else{
                    pettype = "2".toLong()
                }
                val petweight = binding.addPetWeightEdittext.text.toString().toDouble()
                saveStore(petimage, petname, pettype, petweight, getextrapetname, getextrapetweight, getdocuname)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }
    }
    // 바꿔보는 부분
    private fun saveStore(petimage: Bitmap, petname: String, pettype: Long, petweight: Double, getpetname:String?, getpetweight: Double?, docuname: String?){
        val baos = ByteArrayOutputStream()
        petimage.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val imagedata = baos.toByteArray()
        val data = myPetType(petname, pettype, petweight, useruid.toString())
        uploadImage(petname, imagedata)
        val colRef = db.collection("pets")
        if (docuname == null || getpetname == null){
            colRef.add(data)
                .addOnSuccessListener{
                    Toast.makeText(this,"저장완료", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener() {
                    Toast.makeText(this,"에러", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            storageRef.child("images/"+useruid+"/"+ getpetname+".jpg").delete()
            db.document(docuname).update("petName", data.petName, "petType", data.petType, "petWeight", data.petWeight, "userUID", useruid).addOnSuccessListener {
                Toast.makeText(this,"정보 갱신 완료", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(docId: String, data: ByteArray){
        val useruid = MyApplication.auth.currentUser?.uid
        val imgRef: StorageReference = storageRef.child("images/"+useruid+"/"+docId+".jpg")
        val uploadTask = imgRef.putBytes(data)
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
