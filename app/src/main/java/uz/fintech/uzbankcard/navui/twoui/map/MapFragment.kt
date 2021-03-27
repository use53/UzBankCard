package uz.fintech.uzbankcard.navui.twoui.map


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.map_fragment.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import uz.fintech.uzbankcard.R
import uz.fintech.uzbankcard.adapter.MapAdapter
import uz.fintech.uzbankcard.common.lazyFast


@Suppress("DEPRECATION")
class MapFragment :Fragment(R.layout.map_fragment){

    private val mapViewModel:MapViewModel by activityViewModels()
    private var mapController: MapController?=null
    private val mapAdapter by lazyFast { MapAdapter() }
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        map_rec.layoutManager=GridLayoutManager(requireContext(),3)
        map_rec.adapter=mapAdapter
        mapViewModel.loadMapItemVM()
        mapViewModel.listItemVM().observe(viewLifecycleOwner, Observer {
            mapAdapter.submitList(it)
        })
       markerMaps()


    }

    private fun markerMaps() {
        Configuration.getInstance().load(requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext()))

        map_view.setTileSource(TileSourceFactory.MAPNIK)
        map_view.setBuiltInZoomControls(true)
        map_view.setMultiTouchControls(true)
        map_view.setUseDataConnection(true)
        map_view.invalidate()



        mapController = map_view.controller as MapController?

        mapController!!.setZoom(9)
        val list=ArrayList<GeoPoint>()
        val gPt = GeoPoint(41.32853 ,69.34484)
        val point =GeoPoint(41.36561,69.22562)
        val pointBank=GeoPoint(41.33671,69.27812)
        list.add(gPt)
        list.add(point)
        list.add(pointBank)
        mapController!!.setCenter(gPt)
        map_view.mapOrientation=4F
        val startmarker=Marker(map_view)
        val endmarker=Marker(map_view)
        val markers=Marker(map_view)


        startmarker.position=gPt
        endmarker.position=point
        markers.position=pointBank

        startmarker.title="Ipotika Bank"
        endmarker.title="Asaka Bank"
        markers.title="Milliy Bank"
        startmarker.isDraggable=true
        endmarker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM)


        map_view.overlays.add(startmarker)
        map_view.overlays.add(endmarker)
        map_view.overlays.add(markers)

        val livedate=MutableLiveData<Polyline>()
        val markma=MarkerTask(list,livedate,requireContext())
        markma.start()
        markma.livedate.observe(viewLifecycleOwner, Observer {
            map_view.overlays.add(it)
        })
    }


}




