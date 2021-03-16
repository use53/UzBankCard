package uz.fintech.uzbankcard.navui.twoui.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.map_fragment.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import uz.fintech.uzbankcard.R

class MapFragment :Fragment(R.layout.map_fragment){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()

    }

    private fun initMap() {
        map_view.setUseDataConnection(true)
        map_view.setTileSource(TileSourceFactory.MAPNIK)
        map_view.zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
    }
}