package com.example.data

import Resource
import com.example.domain.entity.OpenRouteDirectionResult
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class GoogleMapsServices {

    private val API_KEY = "AIzaSyDo_NcGDWugGSV5q7YC_kye_ekQwvKsg5k"
    private val OPEN_ROUTE_API_KEY = "5b3ce3597851110001cf6248659904d13c424881b9f1ceb34e8288b6"

    fun getDirections(
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

    fun getDirctionFromOpenRoute(
        origin: LatLng,
        destination: LatLng,
        result: (Resource<OpenRouteDirectionResult>) -> Unit
    ) {
        val url =
            "https://api.openrouteservice.org/v2/directions/driving-car?api_key=$OPEN_ROUTE_API_KEY&start=${origin.longitude},${origin.latitude}&end=${destination.longitude},${destination.latitude}"

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                // Handle the response here
                val jsonResponse = JSONObject(responseBody)

                val routes = jsonResponse.getJSONArray("routes")
                if (routes.length() > 0) {
                    val route = routes.getJSONObject(0)
                    val geometry = route.getJSONObject("geometry")
                    val polyline = geometry.getString("coordinates")

                    val summary = route.getJSONObject("summary")
                    val distance = summary.getDouble("distance")
                    val duration = summary.getDouble("duration")

                    val directionResult = OpenRouteDirectionResult(
                        polyline = polyline,
                        distance = distance,
                        duration = duration
                    )
                    result.invoke(Resource.Success(directionResult))
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle the failure here
                result.invoke(Resource.Failure("Failed to get direction from open route${e.message}"))
            }
        })
    }
}