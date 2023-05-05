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
import com.example.trainlivelocation.databinding.PassengersRcvItemLayoutBinding
import com.example.trainlivelocation.databinding.PostCommentItemLayoutBinding
import com.example.trainlivelocation.databinding.UserPostsRcvItemLayoutBinding

class PassengersCustomAdapter(
    private val passengersListener: PassengersListener
) :
    RecyclerView.Adapter<PassengersAdapterViewHolder>(), BindableAdapter<ArrayList<UserInTrainResponseItem>> {
    private val TAG: String? = "PassengersCustomAdapter"
    private var binding: PassengersRcvItemLayoutBinding? = null
    lateinit var passengersArrayList: ArrayList<UserInTrainResponseItem>
    var passengersList = emptyList<UserInTrainResponseItem>()

    public fun updateData(postList: ArrayList<UserInTrainResponseItem>) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengersAdapterViewHolder {
        binding = PassengersRcvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PassengersAdapterViewHolder(binding!!, passengersListener!!)
    }

    override fun getItemCount(): Int = passengersArrayList.size

    override fun onBindViewHolder(holder: PassengersAdapterViewHolder, position: Int) {
        val passengers = passengersArrayList.get(position)
        holder.bind(passengers)

    }

    override fun setData(data: ArrayList<UserInTrainResponseItem>) {
        Log.i(TAG, "data from adapter ---> ${data}")
        this.passengersArrayList = data!!
        notifyDataSetChanged()
    }
}
