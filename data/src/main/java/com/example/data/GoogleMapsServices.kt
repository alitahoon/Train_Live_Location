package com.example.data

import Resource
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode

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
        result: (Resource<DirectionsResult>) -> Unit
    ){
        
    }


}