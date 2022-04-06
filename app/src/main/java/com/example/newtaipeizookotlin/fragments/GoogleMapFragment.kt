package com.example.newtaipeizookotlin.fragments

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newtaipeizookotlin.R
import com.example.newtaipeizookotlin.adapter.GoogleMapItemAdapter
import com.example.newtaipeizookotlin.databinding.GoogleMapFragmentBinding
import com.example.newtaipeizookotlin.datalist.LocationPositionData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class GoogleMapFragment : BaseFragment<GoogleMapFragmentBinding>(), OnMapReadyCallback,
    GoogleMapItemAdapter.MapViewRecycleViewClickListener {
    override val mLayout: Int
        get() = R.layout.google_map_fragment

    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mLatLng: LatLng
    private var mLocationPositionListData: ArrayList<LocationPositionData> =
        ArrayList<LocationPositionData>()


    override fun initView() {

        mDataBinding.mGoogleMapRecycleView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val mGoogleMapItemAdapter = GoogleMapItemAdapter()
        mGoogleMapItemAdapter.setData(mLocationPositionListData, this)
        mDataBinding.mGoogleMapRecycleView.adapter = mGoogleMapItemAdapter
        val iSupportMapFragment =
            (childFragmentManager.findFragmentById(R.id.mGoogleMap) as SupportMapFragment?)
        iSupportMapFragment?.getMapAsync(this)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getBundle() {
        val iBundle = arguments
        if (iBundle != null) {
            mLocationPositionListData =
                iBundle.getSerializable("mLocationPositionListData") as ArrayList<LocationPositionData>
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mGoogleMap = googleMap
        }
        mLatLng = LatLng(
            mLocationPositionListData[0].getKeyYPosition()!!.toDouble(),
            mLocationPositionListData[0].getKeyXPosition()!!.toDouble()
        )
        mGoogleMap.addMarker(MarkerOptions().position(mLatLng))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 17f))


//        //放大地圖到15倍
//        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //設定 右下角的放大縮小功能
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        //設定 左上角的指南針，要兩指旋轉才會出現
        mGoogleMap.uiSettings.isCompassEnabled = true
        //設定 右下角的導覽及開啟 Google Map功能
        mGoogleMap.uiSettings.isMapToolbarEnabled = true
    }


    override fun onMapViewClicked(position: Int) {
        mGoogleMap.clear()
        mLatLng = LatLng(
            mLocationPositionListData[position].getKeyYPosition()!!.toDouble(),
            mLocationPositionListData[position].getKeyXPosition()!!.toDouble()
        )
        mGoogleMap.addMarker(MarkerOptions().position(mLatLng))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng))
    }
}