package com.example.trainlivelocation.utli

import android.view.View
import com.example.domain.entity.*

interface StationAlarmListener {
//     val onSwitchChanged: CompoundButton.OnCheckedChangeListener


     fun OnButtonEdtitClicked(stationAlarmEntity: StationAlarmEntity)
     fun OnSwitchButtonChecked(isChecked: Boolean,stationAlarmEntity: StationAlarmEntity)
}