package com.example.trainlivelocation.utli

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entity.*
import com.example.trainlivelocation.databinding.ChatMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.DoctorRcvItemLayoutBinding
import com.example.trainlivelocation.databinding.InboxMessageItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class DoctorCustomAdapter(
    private val doctorListener: DoctorListener
) :
    RecyclerView.Adapter<DoctorAdapterViewHolder>(), BindableAdapter<ArrayList<DoctorResponseItem>> {
    private val TAG: String? = "DoctorCustomAdapter"
    private var binding: DoctorRcvItemLayoutBinding? = null
    lateinit var doctorArrayList: ArrayList<DoctorResponseItem>
    var messageList = emptyList<DoctorResponseItem>()

    public fun updateData(postList: ArrayList<DoctorResponseItem>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAdapterViewHolder {
        binding = DoctorRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DoctorAdapterViewHolder(binding!!, doctorListener!!)
    }

    override fun getItemCount(): Int = doctorArrayList.size

    override fun onBindViewHolder(holder: DoctorAdapterViewHolder, position: Int) {
        val doctor = doctorArrayList.get(position)
        holder.bind(doctor)

    }

    override fun setData(data: ArrayList<DoctorResponseItem>) {
        Log.i(TAG, "data from adapter ---> ${data}")
        this.doctorArrayList = data!!
        notifyDataSetChanged()
    }
}
