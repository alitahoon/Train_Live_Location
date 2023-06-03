package com.example.data

import Resource
import android.util.Log
import com.google.maps.android.PolyUtil
import com.example.domain.entity.OpenRouteDirectionResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class GoogleMapsServices {

    private val API_KEY = "AIzaSyDo_NcGDWugGSV5q7YC_kye_ekQwvKsg5k"
    private val OPEN_ROUTE_API_KEY = "5b3ce3597851110001cf6248659904d13c424881b9f1ceb34e8288b6"

    val TAG = "GoogleMapsServices"
    suspend fun getDirections(
        origin: LatLng, destination: LatLng, result: (Resource<DirectionsResult>) -> Unit
    ) {
        val geoApiContext = GeoApiContext.Builder()
            .apiKey(API_KEY)
            .build()

        try {
            val request: DirectionsApiRequest = DirectionsApi.newRequest(geoApiContext)
                .mode(TravelMode.TRANSIT)
                .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(
                    com.google.maps.model.LatLng(
                        destination.latitude,
                        destination.longitude
                    )
                )

            val response: DirectionsResult = request.await()
            result.invoke(Resource.Success(response))
        } catch (e: Exception) {
            result.invoke(Resource.Failure("Failed getting LocationDirection -->${e.message}"))
        }

    }


// ...

    suspend fun getDirectionFromOpenRoute(
        origin: LatLng,
        destination: LatLng,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        val baseUrl = "https://api.openrouteservice.org/"
        val apiKey = OPEN_ROUTE_API_KEY

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenRouteServiceApi::class.java)

        val start = "${origin.longitude},${origin.latitude}"
        val end = "${destination.longitude},${destination.latitude}"
        val response = service.getDirections(apiKey, start, end)
        if (response.isSuccessful) {
            if (response.body() != null){
                try {
                    val features = response.body()!!.features
                    Log.i(TAG,"${response.body()}")
                    Log.i(TAG,"${features}")
                    if (features.isNotEmpty()) {
                        Log.i(TAG,"${features}")
                        val firstFeature = features.first()
                        val properties = firstFeature.properties
                        val segments = properties.segments
                        Log.i(TAG,"${segments}")

                        if (segments.isNotEmpty()) {
                            Log.i(TAG,"${features}")

                            val firstSegment = segments.first()
                            val distance = firstSegment.distance
                            val duration = firstSegment.duration
                            val geometry = firstFeature.geometry
                            val coordinates = geometry.coordinates.flatten()
                            val polylineCoordinates = mutableListOf<LatLng>()

                            for (i in 0 until coordinates.size / 2) {
                                val latitude = coordinates[i * 2 + 1]
                                val longitude = coordinates[i * 2]
                                val latLng = LatLng(latitude, longitude)
                                polylineCoordinates.add(latLng)
                            }

                            val polyline = PolyUtil.encode(polylineCoordinates)

                            val directionResult = OpenRouteDirectionResult(
                                polyline = polyline,
                                distance = distance,
                                duration = duration
                            )
                            result.invoke(Resource.Success(directionResult))
                        } else {
                            result.invoke(Resource.Failure("No valid directions found"))
                        }
                    } else {
                        result.invoke(Resource.Failure("No valid directions found"))
                    }
                } catch (e: Exception) {
                    result.invoke(Resource.Failure("Failed to get direction from OpenRouteService"))
                }
            }else{
                result.invoke(Resource.Failure("response body = null"))
            }

        }else{
            result.invoke(Resource.Failure("${response.errorBody()}"))
        }


    }
    suspend fun getDirectionFromOpenRouteForWaypoints(
        waypoints: List<LatLng>,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        val baseUrl = "https://api.openrouteservice.org/"
        val apiKey = OPEN_ROUTE_API_KEY

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenRouteServiceApi::class.java)
        val waypointsString = waypoints.joinToString("|") { "${it.longitude},${it.latitude}" }
        val response = service.getDirectionsWayPoints(apiKey,waypointsString)
        if (response.isSuccessful) {
            if (response.body() != null){
                try {
                    val features = response.body()!!.features
                    Log.i(TAG,"${response.body()}")
                    Log.i(TAG,"${features}")
                    if (features.isNotEmpty()) {
                        Log.i(TAG,"${features}")
                        val firstFeature = features.first()
                        val properties = firstFeature.properties
                        val segments = properties.segments
                        Log.i(TAG,"${segments}")

                        if (segments.isNotEmpty()) {
                            Log.i(TAG,"${features}")

                            val firstSegment = segments.first()
                            val distance = firstSegment.distance
                            val duration = firstSegment.duration
                            val geometry = firstFeature.geometry
                            val coordinates = geometry.coordinates.flatten()
                            val polylineCoordinates = mutableListOf<LatLng>()

                            for (i in 0 until coordinates.size / 2) {
                                val latitude = coordinates[i * 2 + 1]
                                val longitude = coordinates[i * 2]
                                val latLng = LatLng(latitude, longitude)
                                polylineCoordinates.add(latLng)
                            }

                            val polyline = PolyUtil.encode(polylineCoordinates)

                            val directionResult = OpenRouteDirectionResult(
                                polyline = polyline,
                                distance = distance,
                                duration = duration
                            )
                            result.invoke(Resource.Success(directionResult))
                        } else {
                            result.invoke(Resource.Failure("No valid directions found"))
                        }
                    } else {
                        result.invoke(Resource.Failure("No valid directions found"))
                    }
                } catch (e: Exception) {
                    result.invoke(Resource.Failure("Failed to get direction from OpenRouteService"))
                }
            }else{
                result.invoke(Resource.Failure("response body = null"))
            }

        }else{
            result.invoke(Resource.Failure("${response.errorBody()}"))
        }


    }

}