package com.example.porject

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.walking_list.view.*

/*class walkingAdapter(private val items: ArrayList<walkingData>) :
    RecyclerView.Adapter<walkingAdapter.ViewHolder>(), OnNoteItemClickListener{

    lateinit var listener : OnNoteItemClickListener

    private var layoutType = 0
    // 내용 중심인지 사진 중심인지 판단하기 위한 변수

    override fun getItemCount() = items.size

    fun getItem(position: Int): walkingData? {
        return items[position]
    } // 아이템 반환

    fun addItem(item: walkingData?) {
        if (item != null) {
            items.add(item)
        }
    } // 아이템 추가

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.walking_list,parent,false)
        return ViewHolder(inflatedView,this,layoutType)
    } // 새로 만들어준 뷰홀더 생성

    override fun onBindViewHolder(holder: walkingAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.setLayoutType(layoutType)
    } // 데이터 뷰홀더에 바인딩

    fun setOnItemClickListener(listener: OnNoteItemClickListener){
        this.listener = listener
    }

    fun switchLayout(position: Int){
        layoutType = position
    } // 내용,사진 레이아웃 변경 함수

    class ViewHolder(itemView : View, listener: OnNoteItemClickListener, layoutType : Int) : RecyclerView.ViewHolder(itemView){

        fun bind(item: walkingData){
            var mood = item.mood
            var moodIndex = Integer.parseInt(mood)
            setMoodImage(moodIndex) // 기분 설정

            var picturePath = item.picture
            if(picturePath != null && !picturePath.equals("")){

                itemView.pictureExistsImageView.visibility = View.VISIBLE
                itemView.mapImage.visibility = View.VISIBLE
                itemView.mapImage.setImageURI(Uri.parse("file://$picturePath"))
            }
            else{
                itemView.pictureExistsImageView.visibility = View.GONE
                itemView.mapImage.visibility = View.GONE
                itemView.mapImage.setImageResource(R.drawable.noimagefound)
            }
            // 사진 설정

            var weather = item.weather
            var weatherIndex = Integer.parseInt(weather)
            setWeatherImage(weatherIndex) // 날씨 설정

            itemView.contentsTextView.text = item.contents
            itemView.contentsTextView2.text = item.contents
            // 텍스트 설정

            itemView.locationTextView.text = item.address
            itemView.locationTextView2.text = item.address
            // 주소 설정

            itemView.dateTextView.text = item.createDataStr
            itemView.dateTextView2.text = item.createDataStr
            // 날짜 설정
        }

        fun setMoodImage( moodIndex : Int){
            when(moodIndex){
                0 -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile1_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile1_48)
                }
                1 -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile2_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile2_48)
                }
                2 -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile3_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile3_48)
                }
                3 -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile4_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile4_48)
                }
                4 -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile5_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile5_48)
                }
                else -> {
                    itemView.moodImageView.setImageResource(R.drawable.smile3_48)
                    itemView.moodImageView2.setImageResource(R.drawable.smile3_48)
                }
            }
        } // moodIndex에 따른 기분 이미지 출력

        fun setWeatherImage(weatherIndex : Int){
            when(weatherIndex){
                0 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_1)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_1)
                }
                1 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_2)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_2)
                }
                2 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_3)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_3)
                }
                3 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_4)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_4)
                }
                4 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_5)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_5)
                }
                5 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_6)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_6)
                }
                6 -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_7)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_7)
                }
                else -> {
                    itemView.weatherImageView.setImageResource(R.drawable.weather_icon_1)
                    itemView.weatherImageView2.setImageResource(R.drawable.weather_icon_1)
                }
            }
        } // weatherIndex에 따른 날씨 이미지 출력

        fun setLayoutType(layoutType: Int){
            if( layoutType == 0){
                itemView.layout1.visibility = View.VISIBLE
                itemView.layout2.visibility = View.GONE
            }
            else if( layoutType == 1){
                itemView.layout1.visibility = View.GONE
                itemView.layout2.visibility = View.VISIBLE
            }
        } // 레이아웃 타입에 따라 내용,사진 레이아웃 하나는 비활성화, 하나는 활성화
    }

}*/
