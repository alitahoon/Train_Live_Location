package com.example.trainlivelocation.utli

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.TrainResponseItem
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.TrainRcvItemLayoutBinding

class TrainCustomAdapter(private val trainListener: Train_Dialog_Listener) :
    RecyclerView.Adapter<TrainAdapterViewHolder>(), BindableAdapter<ArrayList<TrainResponseItem>> {
    private var TAG:String?="StationCustomAdapter"
    var selectedItemPos = -1
    var lastItemSelectedPos = -1
    private var binding: TrainRcvItemLayoutBinding? = null
    lateinit var trainArrayList: ArrayList<TrainResponseItem>
    var trainList = emptyList<TrainResponseItem>()

    public fun updateData(stationList: ArrayList<TrainResponseItem>) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainAdapterViewHolder {
        binding = TrainRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrainAdapterViewHolder(binding!!,trainListener)
    }

    override fun getItemCount(): Int = trainList.size

    override fun onBindViewHolder(holder: TrainAdapterViewHolder, position: Int) {
        val train = trainList.get(position)
        Log.i(TAG,"${trainList.get(position)}")
        holder.bind(train)
        holder.itemView.setOnClickListener{
            trainListener.onTrainSelected(train.id,train.degree)
        }
    }

    override fun setData(data: ArrayList<TrainResponseItem>) {
        this.trainList = data
        notifyDataSetChanged()
    }
    fun defaultBg() {
        binding!!.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_unselected)
    }

    fun selectedBg() {
        binding!!.stationRCVIremColor.setBackgroundResource(R.drawable.bg_capsule_selected)
    }
}
