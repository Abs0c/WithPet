package com.example.porject

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.porject.databinding.ActivityDietBinding
import kotlinx.android.synthetic.main.activity_diet.*
import java.lang.reflect.Array


class   Diet : AppCompatActivity() {
    lateinit var binding: ActivityDietBinding
    private var shame:Double=0.0
    private var editdata:Double=0.0

    private fun setListenerToEditText() {
        binding.weightWrite.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.weightWrite.windowToken, 0)
                // Toast Message
                true
            }
            false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setTitle("")

        val toolbar2 = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val spinner = findViewById<Spinner>(R.id.dog_situation)


        ArrayAdapter.createFromResource(
            this,
            R.array.my_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener= object :AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(parent: AdapterView<*>?){
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){
                    //선택하지 않은경우
                    0 -> {
                        shame = 0.0
                    }
                    //성장기(4~12개월)
                    1 -> {
                        //수치3
                        shame = 3.0
                    }
                    //성장기(4~12개월)
                    2 -> {
                        //수치2
                        shame = 2.0
                    }
                    //미중성 성견
                    3 -> {
                        //수치1.8
                        shame = 1.8
                    }
                    //중성화 완료 성견
                    4 -> {
                        //수치1.6
                        shame = 1.6
                    }
                    //과체중 성견
                    5 -> {
                        //수치1.4
                        shame = 1.4
                    }
                    //비만인 성견
                    6 -> {
                        //수치 1
                        shame = 1.0
                    }
                    //임산견 (임신전반 42일간)
                    7 -> {
                        //수치1.8
                        shame = 1.8
                    }

                    //임산견 (임신후반 21일간)
                    8 -> {
                        //3

                        shame = 3.0
                    }
                    //노령견
                    9 -> {
                        //1.4
                        shame = 1.4

                    }

                    //일치하는게 없는 경우
                    else -> {

                    }

                }
            }
        }

        val sumBtn : Button= findViewById(R.id.sumBtn)

        sumBtn.setOnClickListener {
            var pet_kcal:Double = 0.0

            var ses=binding.weightWrite.text.toString()
            editdata=ses.toDouble()
            //강아지 몸무게가 2kg이상일시
            if (editdata>=2.0) {
                pet_kcal = (30*editdata+70) * shame
                binding.calculation.text=pet_kcal.toString()
            }
            //강아지 몸무게가 2kg미만일시
            else if(editdata<2.0){
                pet_kcal = (70 * editdata * (0.75)) * shame
                binding.calculation.text=pet_kcal.toString()
            }
            else{

            }
        }
        val items = ArrayList<String>()
        val weightitems = ArrayList<String>()
        MyApplication.db.collection("pets").get().addOnSuccessListener { result ->
            for (document in result){
                if (MyApplication.auth.currentUser?.uid == document["userUID"]){
                    items.add(document["petName"].toString())
                    weightitems.add(document["petWeight"].toString())
                }
            }
            val adapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.dietPetSelectSpinner.adapter = adapter
        }
        binding.dietPetSelectSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                weight_write.text = weightitems[p2]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}