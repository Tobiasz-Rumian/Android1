package com.example.project1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.project1.models.Shop
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private var shops = ArrayList<Shop>()
    private lateinit var mMap: GoogleMap
    private lateinit var databaseRef: DatabaseReference
    private var markers = ArrayList<Marker>()
    private val permissionId = 12
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13.0f))
        }
        setUpDatabase()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        prepareMap()
    }

    private fun prepareMap() {
        if (::mMap.isInitialized) {
            markers.onEach { marker -> marker.remove() }
            markers = ArrayList()
            shops.onEach { shop ->
                markers.add(
                    mMap.addMarker(
                        MarkerOptions().position(LatLng(shop.coordinateX, shop.coordinateY)).title(shop.title)
                    )
                )
            }
        }
    }



    private fun setUpDatabase() {
        databaseRef = FirebaseDatabase.getInstance()
            .getReference("users/" + FirebaseAuth.getInstance().currentUser!!.uid + "/shops")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val p = dataSnapshot.children.mapNotNull { child -> child.getValue(Shop::class.java) }
                Log.d("xxx", p.toString())
                shops.clear()
                shops.addAll(p)
                prepareMap()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("DATABASE", "Failed to read value.", error.toException())
            }
        })
    }
}
