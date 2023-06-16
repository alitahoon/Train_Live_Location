package com.example.trainlivelocation.ui

import Resource
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.domain.entity.StationResponseItem
import com.example.domain.entity.TrainConverterDistanceModel
import com.example.domain.entity.TrainConverterModel
import com.example.trainlivelocation.R
import com.example.trainlivelocation.databinding.FragmentAddPostCommentBinding
import com.example.trainlivelocation.databinding.FragmentTrainConvertersBinding
import com.example.trainlivelocation.utli.Arrival_Station_Listener
import com.example.trainlivelocation.utli.Station_Dialog_Listener
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainConverters : Fragment(), Station_Dialog_Listener, Arrival_Station_Listener {

    private val TAG: String? = "TrainConverters"
    private lateinit var binding: FragmentTrainConvertersBinding
    private val trainConvertersViewModel: TrainConvertersViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrainConvertersBinding.inflate(inflater, container, false)
            .apply {
                this.viewmodel = trainConvertersViewModel
            }
        setObservers()
        return binding.root
    }

    private fun setObservers() {
        trainConvertersViewModel.arrivalStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get arrival inforamtion
                var dialog = ChooseArrivalStationFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseArrivalStationDialogFragment")
            }
        })

        trainConvertersViewModel.takeoffStationTxtClicked.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                //get takeoff inforamtion
                var dialog = ChooseStationDialogFragment(this)
                var childFragmentManager = getChildFragmentManager()
                dialog.show(childFragmentManager, "ChooseTakeOffStationDialogFragment")
            }
        })


    }

    companion object {
        private var arrivalStationLocation: LatLng? = null
        private var arrivalStationId: Int? = null
        private var takeoffStationLocation: LatLng? = null
        private var takeoffStationId: Int? = null
        private var trainLocations = arrayListOf<TrainConverterModel>()
        private var trainLocationsDitanceTakeOff = arrayListOf<TrainConverterDistanceModel>()
        private var trainLocationsDitanceArrival = arrayListOf<TrainConverterDistanceModel>()
        private var trainLocationsAfter30Seconds = arrayListOf<TrainConverterModel>()
        private var trainLocationsDistanceAfter30SecondsTakeOff =
            arrayListOf<TrainConverterDistanceModel>()
        private var trainLocationsDistanceAfter30SecondsArrival =
            arrayListOf<TrainConverterDistanceModel>()
    }

    override fun onArrivalStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.converterTxtArrival.setText("${StationName}")
        arrivalStationLocation = LatLng(longitude!!, latitude!!)
        arrivalStationId = StationId
    }

    override fun onStationSelected(
        StationId: Int?,
        StationName: String?,
        longitude: Double?,
        latitude: Double?
    ) {
        binding.converterTxtTakeoff.setText("${StationName}")
        takeoffStationLocation = LatLng(longitude!!, latitude!!)
        takeoffStationId = StationId

    }

    fun gettingTrainLocation(time: String?) {
        //getting all trains
        trainConvertersViewModel.getTrains()
        trainConvertersViewModel.trains.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.i(TAG, "getting trains....")
                }
                is Resource.Success -> {
                    Log.i(TAG, "${it.data}")
                    //getting train locations
                    for (train in it.data) {
                        trainConvertersViewModel.getTrainLocation(train.id)
                        trainConvertersViewModel.trainLocation.observe(
                            viewLifecycleOwner,
                            Observer {
                                when (it) {
                                    is Resource.Loading -> {
                                        Log.i(TAG, "getting train ${train.id} location....")
                                    }
                                    is Resource.Success -> {
                                        Log.i(
                                            TAG,
                                            "Success getting train ${train.id} location ${it.data}"
                                        )
                                        when (time) {
                                            "first" -> {
                                                trainLocations.add(
                                                    TrainConverterModel(
                                                        train.id,
                                                        LatLng(it.data.latitude, it.data.longitude)
                                                    )
                                                )
                                                computeDistance(trainLocations, time)
                                            }
                                            "after30Seconds" -> {
                                                trainLocationsAfter30Seconds.add(
                                                    TrainConverterModel(
                                                        train.id,
                                                        LatLng(it.data.latitude, it.data.longitude)
                                                    )
                                                )
                                                computeDistance(trainLocationsAfter30Seconds, time)
                                            }
                                        }

                                    }
                                    is Resource.Failure -> {
                                        Log.i(
                                            TAG,
                                            "Success getting train ${train.id} location ${it.error}"
                                        )
                                    }
                                    else -> {}
                                }
                            })
                    }
                }
                is Resource.Failure -> {
                    Log.e(TAG, "${it.error}")
                }
                else -> {

                }
            }
        })

    }

    fun computeDistance(trainLocationList: ArrayList<TrainConverterModel>, time: String?) {
        for (train in trainLocationList) {
            when (time) {
                "first" -> {
                    trainLocationsDitanceTakeOff.add(
                        TrainConverterDistanceModel(
                            train.trainID,
                            getDistanceInKM(
                                train.trainLocation.latitude,
                                train.trainLocation.longitude,
                                takeoffStationLocation!!.latitude,
                                takeoffStationLocation!!.longitude
                            )
                        )
                    )

                    trainLocationsDitanceArrival.add(
                        TrainConverterDistanceModel(
                            train.trainID,
                            getDistanceInKM(
                                train.trainLocation.latitude, train.trainLocation.longitude,
                                arrivalStationLocation!!.latitude, arrivalStationLocation!!.latitude
                            )
                        )
                    )
                }
                "after30Seconds" -> {
                    trainLocationsDistanceAfter30SecondsTakeOff.add(
                        TrainConverterDistanceModel(
                            train.trainID,
                            getDistanceInKM(
                                train.trainLocation.latitude,
                                train.trainLocation.longitude,
                                takeoffStationLocation!!.latitude,
                                takeoffStationLocation!!.longitude
                            )
                        )
                    )

                    trainLocationsDistanceAfter30SecondsArrival.add(
                        TrainConverterDistanceModel(
                            train.trainID,
                            getDistanceInKM(
                                train.trainLocation.latitude, train.trainLocation.longitude,
                                arrivalStationLocation!!.latitude, arrivalStationLocation!!.latitude
                            )
                        )
                    )
                }
            }
        }
    }


    private fun getDistanceInKM(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ): Double {
        var results = FloatArray(1)
        Log.e(TAG, "startLat ${startLat}")
        Log.e(TAG, "startLon ${startLon}")
        Log.e(TAG, "endLat ${endLat}")
        Log.e(TAG, "endLon ${endLon}")

        Location.distanceBetween(startLat, startLon, endLat, endLon, results)
        Log.e(TAG, "distance In Kilo Meter ${results[0].toDouble() / 1000}")
        return results[0].toDouble() / 1000
    }


    private fun chooseBestTrainAfter30S():Int? {
        var trainsGoingToUserStationAndThenGoingConverterStation =
            arrayListOf<TrainConverterDistanceModel>()
        var bestTrainID: Int? = null
        for (i in 0..trainLocationsDistanceAfter30SecondsArrival.size - 1) {
            Log.i(TAG,"trainLocationsDitanceArrival")
            if (trainLocationsDitanceArrival.get(i).distance < trainLocationsDistanceAfter30SecondsArrival.get(
                    i
                ).distance && trainLocationsDitanceArrival.get(i).trainId == trainLocationsDistanceAfter30SecondsArrival.get(
                    i
                ).trainId
            )
                if (trainLocationsDitanceTakeOff.get(i).distance< trainLocationsDistanceAfter30SecondsTakeOff.get(i).distance &&
                        trainLocationsDitanceTakeOff.get(i).trainId == trainLocationsDistanceAfter30SecondsTakeOff.get(i).trainId){
                    trainsGoingToUserStationAndThenGoingConverterStation.add(
                        trainLocationsDitanceArrival.get(i))
                }
        }

        //small ditance is the best one
        bestTrainID =trainsGoingToUserStationAndThenGoingConverterStation.minByOrNull { it.distance }!!.trainId

        return bestTrainID
    }
}