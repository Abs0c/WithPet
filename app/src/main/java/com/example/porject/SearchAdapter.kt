package com.example.porject

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private val context: Context, private val noteList: MutableList<Note>, private val listener: ItemClickListener) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(),
    Filterable {

    private var SearchList: List<Note>? = null

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val info: TextView
        //val content: TextView

        init{
            info = view.findViewById(R.id.contentsTextView)
            //content = view.findViewById(R.id.contentsTextView2)


            view.setOnClickListener{

                listener.onItemClicked(SearchList!![adapterPosition])
            }
        }
    }

    init {
        this.SearchList = noteList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.walking_list, parent, false)
        return SearchViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int){
        val note = SearchList!![position]
        holder.info.text = "#"+note.noteTitle //+ "  #"+note.noteDescription+"("+excel.cate2+")" + "  #"+excel.source
        //holder.content.text = note.noteDescription.replace(".(?!$)".toRegex(), "$0\u200b")
    }


    override fun getItemCount(): Int = SearchList!!.size


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    SearchList = noteList
                } else {
                    val filteredList = ArrayList<Note>()

                    for (row in noteList) {
                        if (row.noteTitle.toLowerCase().contains(charString.toLowerCase()) || row.noteDescription.toLowerCase().contains(charString.toLowerCase()))
                            //|| row.cate2.toLowerCase().contains(charString.toLowerCase()) || row.source.toLowerCase().contains(charString.toLowerCase()))
                        {
                            filteredList.add(row)
                        }
                    }
                    SearchList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = SearchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                SearchList = filterResults.values as ArrayList<Note>
                notifyDataSetChanged()
            }
        }
    }

    interface ItemClickListener {
        fun onItemClicked(item : Note)
    }
}