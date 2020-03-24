package id.rizmaulana.covid19.ui.maps

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.ui.adapter.viewholders.LocationItem
import id.rizmaulana.covid19.ui.base.BaseFragment
import id.rizmaulana.covid19.ui.base.BaseViewItem
import id.rizmaulana.covid19.ui.detail.DetailViewModel
import id.rizmaulana.covid19.util.CaseType
import id.rizmaulana.covid19.util.ext.observe
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.math.pow


class VisualMapsFragment : BaseFragment(), OnMapReadyCallback {

    private val markers = mutableListOf<Marker>()
    private var googleMap: GoogleMap? = null
    private var pulseCircle: Circle? = null

    private val viewModel by sharedViewModel<DetailViewModel>()

    private val caseType by lazy {
        arguments?.getInt(TYPE) ?: CaseType.FULL
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_visual_maps,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_fr) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun observeChange() {
        observe(viewModel.detailListViewItems, ::updateMarkers)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.googleMap = map

        try {
            googleMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.style_json
                )
            )
        } catch (e: NotFoundException) {
            e.printStackTrace()
        }

        moveCamera(LatLng(LAT_DEFAULT, LON_DEFAULT))
    }

    fun selectItem(data: LocationItem) {
        googleMap?.let {
            moveCamera(LatLng(data.lat, data.long))
            startPulseAnimation(LatLng(data.lat, data.long))
        }
    }

    private val valueAnimator by lazy {
        ValueAnimator.ofFloat(
            0f,
            calculatePulseRadius(googleMap?.cameraPosition?.zoom ?: 4f).apply { }
        ).apply {
            startDelay = 100
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f))
    }

    private fun updateMarkers(data: List<BaseViewItem>) {
        googleMap?.clear()
        markers.clear()
        data.filterIsInstance<LocationItem>().forEach {
            val marker = googleMap?.addMarker(
                MarkerOptions().position(LatLng(it.lat, it.long))
                    .anchor(0.5f, 0.5f)
                    .title(it.locationName)
                    .icon(
                        BitmapDescriptorFactory.fromResource(
                            when (caseType) {
                                CaseType.DEATHS -> R.drawable.img_deaths_marker
                                CaseType.RECOVERED -> R.drawable.img_recovered_marker
                                else -> R.drawable.img_confirmed_marker
                            }
                        )
                    )
            )
            marker?.let { m ->
                markers.add(m)
            }
        }
    }

    private fun calculatePulseRadius(zoomLevel: Float): Float {
        return 2.0.pow(16 - zoomLevel.toDouble()).toFloat() * 160
    }

    private fun startPulseAnimation(latLng: LatLng) {
        valueAnimator?.apply {
            removeAllUpdateListeners()
            removeAllListeners()
            end()
        }

        pulseCircle?.remove()
        pulseCircle = googleMap?.addCircle(
            CircleOptions().center(
                latLng
            ).radius(0.0).strokeWidth(0f)
        )

        valueAnimator.addUpdateListener {
            pulseCircle?.fillColor = when (caseType) {
                CaseType.RECOVERED -> RECOVERED_COLOR
                CaseType.DEATHS -> DEATH_COLOR
                else -> CONFIRMED_COLOR

            }
            pulseCircle?.radius = (valueAnimator.animatedValue as Float).toDouble()
        }

        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                valueAnimator.startDelay = 100
                valueAnimator.start()
            }
        })

        valueAnimator.start()
    }

    companion object {
        private val RECOVERED_COLOR = Color.argb(70, 0, 204, 153)
        private val CONFIRMED_COLOR = Color.argb(70, 242, 185, 0)
        private val DEATH_COLOR = Color.argb(70, 226, 108, 90)

        private const val LAT_DEFAULT = 30.360227
        private const val LON_DEFAULT = 114.8260094
        private const val TYPE = "type"

        @JvmStatic
        fun newInstance(caseType: Int) = VisualMapsFragment().apply {
            arguments = Bundle().apply { putInt(TYPE, caseType) }
        }
    }

}
