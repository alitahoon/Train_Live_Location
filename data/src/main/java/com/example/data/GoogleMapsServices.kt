package com.example.data

import Resource
import android.util.Log
import com.example.domain.entity.Coordinates
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
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GoogleMapsServices {

    private val API_KEY = "AIzaSyDo_NcGDWugGSV5q7YC_kye_ekQwvKsg5k"
    private val OPEN_ROUTE_API_KEY = "5b3ce3597851110001cf62482eb69ee059f8402bb1e049b23b0e618d"
    private val Accept = "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8"
    private val Content_Type = "application/json; charset=utf-8"

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
            if (response.body() != null) {
                try {
                    val features = response.body()!!.features
                    Log.i(TAG, "${response.body()}")
                    Log.i(TAG, "${features}")
                    if (features.isNotEmpty()) {
                        Log.i(TAG, "${features}")
                        val firstFeature = features.first()
                        val properties = firstFeature.properties
                        val segments = properties.segments
                        Log.i(TAG, "${segments}")

                        if (segments.isNotEmpty()) {
                            Log.i(TAG, "${features}")

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
            } else {
                result.invoke(Resource.Failure("response body = null"))
            }

        } else {
            result.invoke(Resource.Failure("${response.errorBody()}"))
        }


    }

    suspend fun getDirectionFromOpenRouteForWaypoints(
        waypoints: List<LatLng>,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        getDirctionFromHttpRequset(waypoints,result)
        val baseUrl = "https://api.openrouteservice.org/"
        val apiKey = OPEN_ROUTE_API_KEY

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create a new instance of Coordinates
        val coordinates = Coordinates()

        // Create a list to store the coordinate pairs
        val coordinatePairs = mutableListOf<List<Double>>()

         // Iterate over the data and add coordinate pairs to the list
        for (point in waypoints) {
            val latitude =point.latitude // Get latitude value for current data item
            val longitude =point.latitude // Get longitude value for current data item
            val coordinatePair = listOf(latitude, longitude)
            coordinatePairs.add(coordinatePair)
        }

        // Assign the list of coordinate pairs to the coordinates property
        coordinates.coordinates = coordinatePairs
        Log.i(TAG, "${coordinates}")

        val service = retrofit.create(OpenRouteServiceApi::class.java)
        val waypointsString = waypoints.joinToString("|") { "${it.longitude},${it.latitude}" }
        Log.i(TAG, "${waypointsString}")
        val response = service.getDirectionsWayPoints(OPEN_ROUTE_API_KEY,Accept,Content_Type,coordinates!!)
        if (response.isSuccessful) {
            if (response.body() != null) {
                Log.i(TAG,"${response.body()}")
//                try {
//                    val features = response.body()!!.features
//                    Log.i(TAG, "${response.body()}")
//                    Log.i(TAG, "${features}")
//                    if (features.isNotEmpty()) {
//                        Log.i(TAG, "${features}")
//                        val firstFeature = features.first()
//                        val properties = firstFeature.properties
//                        val segments = properties.segments
//                        Log.i(TAG, "${segments}")
//
//                        if (segments.isNotEmpty()) {
//                            Log.i(TAG, "${features}")
//
//                            val firstSegment = segments.first()
//                            val distance = firstSegment.distance
//                            val duration = firstSegment.duration
//                            val geometry = firstFeature.geometry
//                            val coordinates = geometry.coordinates.flatten()
//                            val polylineCoordinates = mutableListOf<LatLng>()
//
//                            for (i in 0 until coordinates.size / 2) {
//                                val latitude = coordinates[i * 2 + 1]
//                                val longitude = coordinates[i * 2]
//                                val latLng = LatLng(latitude, longitude)
//                                polylineCoordinates.add(latLng)
//                            }
//
//                            val polyline = PolyUtil.encode(polylineCoordinates)
//
//                            val directionResult = OpenRouteDirectionResult(
//                                polyline = polyline,
//                                distance = distance,
//                                duration = duration
//                            )
//                            result.invoke(Resource.Success(directionResult))
//                        } else {
//                            result.invoke(Resource.Failure("No valid directions found"))
//                        }
//                    } else {
//                        result.invoke(Resource.Failure("No valid directions found"))
//                    }
//                } catch (e: Exception) {
//                    result.invoke(Resource.Failure("Failed to get direction from OpenRouteService"))
//                }
            } else {
                result.invoke(Resource.Failure("response body = null"))
            }

        } else {
            result.invoke(Resource.Failure("${response.raw()}"))
        }


    }

   suspend fun getDirctionFromHttpRequset(   waypoints: List<LatLng>,
                                      result: (Resource<OpenRouteDirectionResult>) -> Unit){
        val url = URL("https://api.openrouteservice.org/v2/directions/driving-car")
        val connection = url.openConnection() as HttpURLConnection

        // Set request method
        connection.requestMethod = "POST"

        // Set headers
        connection.setRequestProperty("Authorization", "5b3ce3597851110001cf62482eb69ee059f8402bb1e049b23b0e618d")
        connection.setRequestProperty("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")

        // Enable input and output streams
        connection.doInput = true
        connection.doOutput = true


        // Create the request body
        // Create a new instance of Coordinates
        val coordinates = Coordinates()

        // Create a list to store the coordinate pairs
        val coordinatePairs = mutableListOf<List<Double>>()

        // Iterate over the data and add coordinate pairs to the list
        for (point in waypoints) {
            val latitude =point.latitude // Get latitude value for current data item
            val longitude =point.latitude // Get longitude value for current data item
            val coordinatePair = listOf(latitude, longitude)
            coordinatePairs.add(coordinatePair)
        }

        // Assign the list of coordinate pairs to the coordinates property
        coordinates.coordinates = coordinatePairs
        Log.i(TAG, "${coordinates}")

        val requestBody=generateRequestBody(coordinatePairs)
        // Write the request body to the output stream
        val outputStream = connection.outputStream
        outputStream.write(requestBody.toByteArray())
        outputStream.flush()
        outputStream.close()

        // Get the response code
        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response body
            val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
            val responseBody = StringBuilder()
            var inputLine: String?
            while (inputStream.readLine().also { inputLine = it } != null) {
                responseBody.append(inputLine)
            }
            inputStream.close()

            // Print the response
            println("status: $responseCode")
            Log.i(TAG,"${responseBody}")
            println("body: $responseBody")
        } else {
            println("status: $responseCode")
        }

        // Disconnect the connection
        connection.disconnect()
    }

    fun generateRequestBody(coordinates: List<List<Double>>): String {
        val jsonCoordinates = coordinates.joinToString(",", "[", "]") {
            it.joinToString(",", "[", "]")
        }
        return """{"coordinates": $jsonCoordinates}"""
    }

}