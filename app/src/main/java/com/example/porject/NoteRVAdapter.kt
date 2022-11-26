package com.example.porject

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.walking_list.view.*
import org.w3c.dom.Text

class NoteRVAdapter(val context: Context,
                    val noteClickInterface: NoteClickInterface,
                    val noteCLickDeleteInterface: NoteCLickDeleteInterface,
                    val ImageClickInterface: ImageClickInterface
                    ) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>(){

    private val items = ArrayList<Note>()
    private var layoutType = 0
    // 내용 중심인지 사진 중심인지 판단하기 위한 변수

    override fun getItemCount() = items.size

    fun getItem(position: Int): Note? {
        return items[position]
    } // 아이템 반환

    fun addItem(item: Note?) {
        if (item != null) {
            items.add(item)
        }
    } // 아이템 추가
    fun onNoteClick(note: Note){
        //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.walking_list,parent,false)
        return ViewHolder(inflatedView, layoutType)
    } // 새로 만들어준 뷰홀더 생성
    override fun onBindViewHolder(holder: NoteRVAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.setLayoutType(layoutType)
        holder.date1.setText("작성 날짜 : " + item.timestamp )
        holder.date2.setText(item.timestamp)
        holder.detail.setOnClickListener {
            holder.setLayoutType(1)
        }
        holder.imag.setOnClickListener {
            ImageClickInterface.ImageClick(items.get(position), holder.imag)
        }
        holder.imag2.setOnClickListener {
            ImageClickInterface.ImageClick(items.get(position), holder.imag2)
        }
        holder.detail2.setOnClickListener {
            holder.setLayoutType(0)
        }
        holder.deletIV.setOnClickListener{
            noteCLickDeleteInterface.onDeleteIconClick(items.get(position))
        }
        holder.itemView.setOnClickListener{
            //Toast.makeText(context, "${items.get(position)}", Toast.LENGTH_SHORT)
            noteClickInterface.onNoteClick(items.get(position))
        }
    } // 데이터 뷰홀더에 바인딩
    fun switchLayout(position: Int){
        layoutType = position
    } // 내용,사진 레이아웃 변경 함수
    fun updateList(newList: List<Note>){
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
    class ViewHolder(itemView : View, layoutType : Int) : RecyclerView.ViewHolder(itemView){
        init {

            itemView.setOnClickListener {

            }
            itemView.setOnClickListener(View.OnClickListener {
                var position = adapterPosition
                setLayoutType(layoutType)
            })
        }
        val noteTV = itemView.findViewById<TextView>(R.id.idTVNoteTitle)
        val timeTV = itemView.findViewById<TextView>(R.id.idTVTimeStamp)
        val deletIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
        val detail = itemView.findViewById<ImageView>(R.id.idIVDetail)
        val detail2 = itemView.findViewById<ImageView>(R.id.idIVDetail2)
        val date1 = itemView.findViewById<TextView>(R.id.dateText)
        val date2 = itemView.findViewById<TextView>(R.id.dateTextView2)

        val imag = itemView.findViewById<ImageView>(R.id.moodImageView)
        val imag2 = itemView.findViewById<ImageView>(R.id.mapImage)



        fun bind(item: Note){
            /*var mood = item.mood
            var moodIndex = Integer.parseInt(mood)
            setMoodImage(moodIndex) // 기분 설정 */

            /*var picturePath = item.picture
            if(picturePath != null && !picturePath.equals("")){

                itemView.pictureExistsImageView.visibility = View.VISIBLE
                itemView.mapImage.visibility = View.VISIBLE
                itemView.mapImage.setImageURI(Uri.parse("file://$picturePath"))
            }
            else{
                itemView.pictureExistsImageView.visibility = View.GONE
                itemView.mapImage.visibility = View.GONE
                itemView.mapImage.setImageResource(R.drawable.noimagefound)
            }*/
            // 사진 설정

            /*var weather = item.weather
            var weatherIndex = Integer.parseInt(weather)
            setWeatherImage(weatherIndex) // 날씨 설정*/

            itemView.contentsTextView.text = item.noteTitle
            itemView.contentsTextView2.text = item.noteDescription
            var bitmap1 = BitmapFactory.decodeByteArray(item.noteImage, 0, item.noteImage.size)
            itemView.moodImageView.setImageBitmap(bitmap1)
            itemView.mapImage.setImageBitmap(bitmap1)
            // 텍스트 설정



        }

        /*fun setMoodImage( moodIndex : Int){
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
        } // moodIndex에 따른 기분 이미지 출력*/

        /*fun setWeatherImage(weatherIndex : Int){
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
        }*/
        fun setLayoutType(layoutType: Int){
            if( layoutType == 0){
                itemView.layout1.visibility = View.VISIBLE
                itemView.layout2.visibility = View.GONE
            }
            else if( layoutType == 1){
                itemView.layout1.visibility = View.GONE
                itemView.layout2.visibility = View.VISIBLE
            }
        }
    }
}
interface NoteCLickDeleteInterface{
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface{
    fun onNoteClick(note: Note)
}
interface ImageClickInterface{
    fun ImageClick(note: Note, view : View)
}