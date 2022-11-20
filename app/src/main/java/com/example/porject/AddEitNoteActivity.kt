package com.example.porject


import android.app.Activity
import android.app.job.JobInfo
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firestore.v1.Cursor
import kotlinx.android.synthetic.main.activity_add_eit_note.*
import kotlinx.android.synthetic.main.activity_loading.*
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
            startActivity(Intent(applicationContext, TestActivity::class.java))
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(NoteViewModel::class.java)
        val imgbtn = findViewById<ImageView>(R.id.imBtn2)
        imgbtn.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }
        val noteType = intent.getStringExtra("noteType")
        if(noteType.equals("Edit")){
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDesc = intent.getStringExtra("noteDescription")
            //val noteImg = intent.getByteArrayExtra("noteImg")
            noteID = intent.getIntExtra("noteID", -1)
            addUpdateBtn.setText("기록 변경")
            noteTitleEdt.setText(noteTitle)
            noteDescriptionEdt.setText(noteDesc)
            /*if (noteImg != null) {
                btmap = BitmapFactory.decodeByteArray(noteImg, 0, noteImg.size)
            }
            Img.setImageBitmap(btmap)*/
        }else{
            addUpdateBtn.setText("기록 저장")
        }

        addUpdateBtn.setOnClickListener {
            val noteTitle = noteTitleEdt.text.toString()
            val noteDescription = noteDescriptionEdt.text.toString()
            //val byte1: ByteArray = imageToBitmap(imgbtn)

            if(noteType.equals("Edit")){
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty()){
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    val updateNote = Note(noteTitle, noteDescription, currentDate)//, byte1)
                    updateNote.id = noteID
                    viewModel.updateNote(updateNote)
                    Toast.makeText(this, "기록 업데이트중..", Toast.LENGTH_LONG).show()
                }
            }else{
                if(noteTitle.isNotEmpty() && noteDescription.isNotEmpty())
                {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    viewModel.addNote(Note(noteTitle,noteDescription, currentDate))//, byte1))
                    Toast.makeText(this, "기록 추가중..", Toast.LENGTH_LONG).show()
                }
            }
            startActivity(Intent(applicationContext, TestActivity::class.java))
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
            startActivity(Intent(applicationContext, TestActivity::class.java))
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
}