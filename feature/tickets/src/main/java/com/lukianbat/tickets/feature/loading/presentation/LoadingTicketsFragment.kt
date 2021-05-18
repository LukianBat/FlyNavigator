package com.lukianbat.tickets.feature.loading.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
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
import com.lukianbat.coreui.utils.viewBinding
import com.lukianbat.feature.map.R
import com.lukianbat.feature.map.databinding.FragmentLoadingTicketsBinding
import com.lukianbat.tickets.common.di.TicketsFlowComponentController
import com.lukianbat.tickets.feature.loading.utils.AnimationUtils.getBezierLinePoints
import com.lukianbat.tickets.feature.loading.utils.AnimationUtils.getMarkerBearing
import javax.inject.Inject


class LoadingTicketsFragment : Fragment(R.layout.fragment_loading_tickets), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by navGraphViewModels<LoadingTicketViewModel>(R.id.navigation_tickets) { viewModelFactory }

    private val binding by viewBinding(FragmentLoadingTicketsBinding::bind)

    private val flyAnimationView get() = binding.flyAnimationView

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
        mapView.getMapAsync(this)
        viewModel.routes().observeData(viewLifecycleOwner, ::handleRoutes)
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
        val iconGen = IconGenerator(requireContext()).apply {
            setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_marker))
            setTextAppearance(R.style.markerTextStyle)
        }

        googleMap.addMarker(
            MarkerOptions()
                .anchor(MARKER_ANCHOR_CENTER, MARKER_ANCHOR_CENTER)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        iconGen.makeIcon(
                            routeUiModel.firstPoint.name
                        )
                    )
                )
                .position(routeUiModel.firstPoint.latLng)
        )

        googleMap.addMarker(
            MarkerOptions()
                .anchor(MARKER_ANCHOR_CENTER, MARKER_ANCHOR_CENTER)
                .icon(
                    BitmapDescriptorFactory.fromBitmap(
                        iconGen.makeIcon(
                            routeUiModel.secondPoint.name
                        )
                    )
                )
                .position(routeUiModel.secondPoint.latLng)
        )

        val builder = LatLngBounds.Builder()
        builder.include(routeUiModel.firstPoint.latLng)
        builder.include(routeUiModel.secondPoint.latLng)

        val bounds = builder.build()
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, BOUND_PADDING)

        googleMap.uiSettings.setAllGesturesEnabled(false)
        googleMap.moveCamera(cu)

        flyAnimationView.startFlyAnimation(
            googleMap.projection.toScreenLocation(routeUiModel.firstPoint.latLng),
            googleMap.projection.toScreenLocation(routeUiModel.secondPoint.latLng),
            R.drawable.ic_plane
        )

        drawPolylineRoute(routeUiModel.firstPoint, routeUiModel.secondPoint)
    }

    private fun drawPolylineRoute(
        fromCity: RouteUiModel.CityUiModel,
        toCity: RouteUiModel.CityUiModel
    ) {
        val points = ArrayList<LatLng>(getBezierLinePoints(fromCity.latLng, toCity.latLng))

        val polyLineOptions = PolylineOptions().apply {
            width(POLYLINE_WIDTH)
            pattern(listOf(Gap(POLYLINE_GAP), Dot()))
            color(
                ContextCompat.getColor(requireContext(), R.color.route_point_color)
            )
            addAll(points)
        }

        googleMap.apply {
            addPolyline(polyLineOptions)
            startPlaneMarkerAnimation(
                points,
                BitmapFactory.decodeResource(resources, R.drawable.ic_plane)
            )
        }

    }

    private fun GoogleMap.startPlaneMarkerAnimation(
        directionPoints: List<LatLng>,
        bitmap: Bitmap
    ) {
        val marker = addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoints.first())
                .flat(true)
                .anchor(MARKER_ANCHOR_CENTER, MARKER_ANCHOR_CENTER)
        ) as Marker

        val handler = Handler(Looper.getMainLooper())
        val start = SystemClock.uptimeMillis()
        val duration: Long = PLANE_MARKER_ANIMATION_DURATION
        val interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            var i = 0
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t: Float = interpolator.getInterpolation(
                    elapsed.toFloat() / duration
                )
                if (i < directionPoints.size) {
                    if (i != 0 && i != directionPoints.size - 1) {
                        marker.rotation =
                            getMarkerBearing(directionPoints[i - 1], directionPoints[i])
                    }
                    marker.position = directionPoints[i]
                }
                i++
                if (t < 1.0) {
                    handler.postDelayed(this, PLANE_MARKER_ANIMATION_DELAYED)
                } else {
                    marker.isVisible = false
                }
            }
        })
    }

    companion object {
        private const val POLYLINE_WIDTH = 15F
        private const val POLYLINE_GAP = 20F

        private const val PLANE_MARKER_ANIMATION_DELAYED = 16L
        private const val BOUND_PADDING = 100
        private const val MARKER_ANCHOR_CENTER = 0.5F
        private const val PLANE_MARKER_ANIMATION_DURATION = 30000L
    }
}
