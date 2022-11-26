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
import kotlin.math.*

// 고양이랑 강아지의 필요 요구량만 다르고 계산식은 같습니다.
class   Diet : AppCompatActivity() {
    lateinit var binding: ActivityDietBinding
    private var shame:Double=0.0
    private var editdata:Double=0.0
    val petList = arrayOf("", "강아지", "고양이")

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
        dogDietSpinner()

        val sumBtn : Button= findViewById(R.id.sumBtn)
        // 해당 계산 부분은 강아지 고양이 계산 방법은 같습니다.
        sumBtn.setOnClickListener {
            var pet_kcal:Double = 0.0

            var ses=binding.weightWrite.text.toString()
            editdata=ses.toDouble()
            //강아지 몸무게가 2kg이상일시
            if (editdata>=2.0) {
                pet_kcal = ((30*editdata+70) * shame)
                pet_kcal = round(pet_kcal)
                binding.calculation.text=pet_kcal.toString()
            }
            //강아지 몸무게가 2kg미만일시
            else if(editdata<2.0){
                pet_kcal = ((70 * editdata * (0.75)) * shame)
                pet_kcal = round(pet_kcal)
                binding.calculation.text=pet_kcal.toString()
            }
            else{

            }
        }
        val items = ArrayList<String>()
        val pettypeitems = ArrayList<String>()
        val weightitems = ArrayList<String>()
        MyApplication.db.collection("pets").get().addOnSuccessListener { result ->
            for (document in result){
                if (MyApplication.auth.currentUser?.uid == document["userUID"]){
                    items.add(document["petName"].toString())
                    pettypeitems.add(petList[document["petType"].toString().toInt()])
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
                changeTextByPetSelected(pettypeitems[p2])
                setDietSpinner(pettypeitems[p2])
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
    fun changeTextByPetSelected(petType: String){
        binding.DietTitle.text = petType +" 식단 계산기"
        binding.dietPetNameText.text = petType +"의 이름"
        binding.dogWeight.text = petType + "의 몸무게"
        binding.dogText.text = petType + "의 상태"
    }

    fun setDietSpinner(petType: String){
        if (petType == "강아지"){
            dogDietSpinner()
        }
        else{
            catDietSpinner()
        }
    }

    fun dogDietSpinner(){
        val spinner = binding.petSituation

        //강아지 상태 리스너를 가져옴
        ArrayAdapter.createFromResource(
            this,
            R.array.dog_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //강아지 상태 리스너 각선택의 계산값들 지정
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
    }

    fun catDietSpinner(){
        val spinner = binding.petSituation

        //고양이 상태리스트 호출
        ArrayAdapter.createFromResource(
            this,
            R.array.cat_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

          //고양이 상태 각 값
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            //고양이 상태별 상황
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    //선택하지 않은경우
                    0 -> {
                        shame = 0.0
                    }
                    //성장기(4개월 미만)
                    1 -> {
                        shame = 3.0
                    }
                    //성장기(4~6개월)
                    2 -> {
                        shame = 2.5
                    }
                    //성장기 (7~12개월)
                    3 -> {
                        shame = 2.0
                    }
                    //중성화 묘
                    4 -> {
                        //수치1.6
                        shame = 1.2
                    }
                    //중성화 안한 묘
                    5 -> {
                        //수치1.4
                        shame = 1.4
                    }
                    //노인묘
                    6 -> {
                        shame = 0.7
                    }
                    //임신묘
                    7 -> {
                        shame = 3.0
                    }
                    //수유묘 (이 경우는 낳은아기묘 마다 다르긴함 2~6이라는데 이부분은 빼도 될것 같습니다. )
                    8 -> {
                        //3
                        shame = 3.0
                    }
                    //다이어트묘
                    9 -> {
                        //0.8
                        shame = 0.8
                    }
                    //일치하는게 없는 경우
                    else -> {
                    }
                }
            }
        }
    }
}