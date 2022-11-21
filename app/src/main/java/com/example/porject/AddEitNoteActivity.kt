package com.example.porject


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddEitNoteActivity : AppCompatActivity() {
    lateinit var noteTitleEdt : EditText
    lateinit var noteDescriptionEdt : EditText
    lateinit var addUpdateBtn : Button
    lateinit var viewModel: NoteViewModel
    lateinit var cancleButton : Button
    private val GALLERY = 1
    lateinit var Img : ImageView
    lateinit var btmap : Bitmap
    var noteID = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_eit_note)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        noteTitleEdt = findViewById<EditText>(R.id.idEdtNoteTitle)
        noteDescriptionEdt = findViewById<EditText>(R.id.idEdtNoteDescription)
        addUpdateBtn = findViewById(R.id.idBtnAddupdate)
        cancleButton = findViewById(R.id.closeButton)
        //Img = findViewById(R.id.imBtn2)
        cancleButton.setOnClickListener {
            val intent = Intent(applicationContext, TestActivity::class.java)
            intent.putExtra("selectedDate", "")
            startActivity(intent)
            this.finish()
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(NoteViewModel::class.java)
        val imgbtn = findViewById<ImageView>(R.id.imBtn2)

        val requestGalleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            try{
                val calRatio = calculateInSampleSize(it!!.data!!.data!!, 160,160)
                val option = BitmapFactory.Options()
                option.inSampleSize = calRatio

                var inputStream = contentResolver.openInputStream(it!!.data!!.data!!)
                var bitmap = BitmapFactory.decodeStream(inputStream, null, option)
                inputStream!!.close()
                //bitmap에 이미지 저장완료
                bitmap?. let{
                    imgbtn.setImageBitmap(bitmap)
                } ?: let{
                    Log.d("error", "bitmap null")
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        var bitmapcheck = false
        bitmapcheck = intent.getBooleanExtra("check", false)
        if(bitmapcheck){
            imgbtn.setImageBitmap(bitmap)
            noteTitleEdt.setText(intent.getStringExtra("et"))
            noteDescriptionEdt.setText(intent.getStringExtra("info"))
        }
        imgbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        val noteType = intent.getStringExtra("noteType")
        if(noteType.equals("Edit")){
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            val noteImg= intent.getByteArrayExtra("image")
            noteID = intent.getIntExtra("noteID", -1)
            addUpdateBtn.setText("기록 변경")
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
            val option = BitmapFactory.Options()
            var bitmap = noteImg?.let { BitmapFactory.decodeByteArray(noteImg, 0, it.size) }
            imgbtn.setImageBitmap(bitmap)
            //if (noteImg != null) {
            //    btmap = BitmapFactory.decodeByteArray(noteImg, 0, noteImg.size)
            //}
            //Img.setImageBitmap(btmap)
        }else{
            addUpdateBtn.setText("기록 저장")
        }

        addUpdateBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()
            //val byte1: ByteArray = imageToBitmap(imgbtn)
            val bytebitmap: Bitmap = getBitmapFromView(imgbtn)
            val baos = ByteArrayOutputStream()
            bytebitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos)
            val bytearrayfrombitmap: ByteArray = baos.toByteArray()
            val sdf = SimpleDateFormat("yyyy MMM dd", Locale.KOREA)
            val currentDate:String = sdf.format(Date())

            if(noteType.equals("Edit")){
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){

                    val updateNote = Note(noteTitle, noteDescription, currentDate, bytearrayfrombitmap)//, byte1)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "기록 업데이트중..", Toast.LENGTH_LONG).show()
                }
            }else{
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty())
                {
                    viewModel.addNote(Note(noteTitle,noteDescription, currentDate, bytearrayfrombitmap))//, byte1))
                    Toast.makeText(this, "기록 추가중..", Toast.LENGTH_LONG).show()
                }
            }
            Toast.makeText(this, currentDate, Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, TestActivity::class.java)
            intent.putExtra("selectedDate", currentDate)
            startActivity(intent)
            this.finish()
        }

    }
    /*fun absolutelyPath(path: Uri): String {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: android.database.Cursor = contentResolver.query(path, proj, null, null, null)!!
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)

        return result
    }

     fun imageToBitmap(image: ImageView): ByteArray {
         val bitmap = (image.getDrawable() as BitmapDrawable).getBitmap()
         val stream = ByteArrayOutputStream()
         bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){

            val intent = Intent(applicationContext, TestActivity::class.java)
            intent.putExtra("selectedDate", "")
            startActivity(intent)
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val imBtn: ImageView = findViewById(R.id.imBtn2)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                var ImnageData: Uri? = data?.data
                //Toast.makeText(this, ImnageData.toString(), Toast.LENGTH_SHORT).show()
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData)
                    imBtn.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int{
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try{
            var inputStream = contentResolver.openInputStream(fileUri)
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

    private fun getBitmapFromView(view: View): Bitmap{
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}