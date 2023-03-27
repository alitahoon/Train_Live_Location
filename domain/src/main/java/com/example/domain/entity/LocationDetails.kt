package com.example.domain.entity

import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationDetails(val longitude:Float,val latitude:Float) :Parcelable{
}