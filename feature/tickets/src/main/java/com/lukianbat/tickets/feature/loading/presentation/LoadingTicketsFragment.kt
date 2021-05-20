package com.lukianbat.tickets.feature.loading.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.ui.IconGenerator
import com.lukianbat.coreui.utils.color
import com.lukianbat.coreui.utils.drawable
import com.lukianbat.feature.map.R
import com.lukianbat.tickets.common.di.TicketsFlowComponentController
import com.lukianbat.tickets.feature.loading.utils.MovingMarkerAnimation
import javax.inject.Inject


class LoadingTicketsFragment : Fragment(R.layout.fragment_loading_tickets), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by navGraphViewModels<LoadingTicketViewModel>(R.id.navigation_tickets) { viewModelFactory }

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().applicationContext as TicketsFlowComponentController)
            .provideTicketsFlowComponent()
            .loadingTicketsComponent()
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(this) {
            requireActivity().findNavController(R.id.host_global).popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = super.onCreateView(inflater, container, savedInstanceState) as View

        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.routes().observeData(viewLifecycleOwner, ::handleRoutes)
    }

    override fun onStart() {
        super.onStart()
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        viewModel.onMapReady()
    }

    private fun handleRoutes(routeUiModel: RouteUiModel) {
        setupMap(routeUiModel)
    }

    private fun setupMap(routeUiModel: RouteUiModel) {
        val builder = LatLngBounds.Builder()
        builder.include(routeUiModel.firstPoint.latLng)
        builder.include(routeUiModel.secondPoint.latLng)
        val bounds = builder.build()

        googleMap.apply {
            addCityMarker(routeUiModel.firstPoint)
            addCityMarker(routeUiModel.secondPoint)
            moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, BOUND_PADDING))
        }

        showRoutePath(routeUiModel.routePoints)
        showPlaneMarkerAnimation(
            routeUiModel.routePoints,
            routeUiModel.lastPointIndex,
            BitmapFactory.decodeResource(resources, R.drawable.ic_plane)
        )
    }

    private fun showRoutePath(points: List<LatLng>) {
        val polyLineOptions = PolylineOptions().apply {
            width(POLYLINE_WIDTH)
            pattern(listOf(Gap(POLYLINE_GAP), Dot()))
            color(this@LoadingTicketsFragment.color(R.color.route_point_color))
            addAll(points)
        }
        googleMap.addPolyline(polyLineOptions)
    }

    private fun showPlaneMarkerAnimation(
        directionPoints: List<LatLng>,
        lastPointIndex: Int,
        bitmap: Bitmap
    ) {
        val planeMarker = googleMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoints[lastPointIndex])
                .flat(true)
                .anchor(MARKER_ANCHOR_CENTER, MARKER_ANCHOR_CENTER)
        ) as Marker

        val markerAnimation = MovingMarkerAnimation(lifecycle, planeMarker) {
            planeMarker.remove()
            viewModel.onStopAnimation(it)
        }

        markerAnimation.startAnimation(
            directionPoints,
            PLANE_MARKER_ANIMATION_STEP_DURATION,
            lastPointIndex
        )
    }

    private fun GoogleMap.addCityMarker(cityUiModel: RouteUiModel.CityUiModel) {
        val iconGen = IconGenerator(requireContext()).apply {
            setBackground(drawable(R.drawable.bg_marker))
            setTextAppearance(R.style.markerTextStyle)
        }

        addMarker(
            MarkerOptions()
                .anchor(MARKER_ANCHOR_CENTER, MARKER_ANCHOR_CENTER)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        iconGen.makeIcon(cityUiModel.name)
                    )
                )
                .position(cityUiModel.latLng)
        )
    }

    companion object {
        private const val POLYLINE_WIDTH = 15F
        private const val POLYLINE_GAP = 20F

        private const val PLANE_MARKER_ANIMATION_STEP_DURATION = 15L
        private const val BOUND_PADDING = 100
        private const val MARKER_ANCHOR_CENTER = 0.5F
    }
}
