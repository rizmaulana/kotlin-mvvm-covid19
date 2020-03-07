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
import id.rizmaulana.covid19.data.model.CovidDetail
import id.rizmaulana.covid19.ui.base.BaseFragment
import id.rizmaulana.covid19.util.CaseType


class VisualMapsFragment : BaseFragment(), OnMapReadyCallback {
    private var googleMap: GoogleMap? = null
    private val detailData by lazy {
        arguments?.getParcelableArrayList<CovidDetail>(DATA).orEmpty()
    }
    private val caseType by lazy {
        arguments?.getInt(TYPE) ?: CaseType.CONFIRMED
    }
    private val markers = mutableListOf<Marker>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_visual_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_fr) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun observeChange() {

    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0
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
        initMarker()
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f))
    }

    private fun initMarker() {
        googleMap?.clear()
        markers.clear()
        detailData.forEach {
            val marker = googleMap?.addMarker(
                MarkerOptions().position(LatLng(it.lat, it.long))
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

    fun selectItem(data: CovidDetail) {
        googleMap?.let {
            moveCamera(LatLng(data.lat, data.long))
            startPulsAnimation(LatLng(data.lat, data.long))
        }
    }

    /*Related to Animation */
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
    private var pulseCircle: Circle? = null

    private fun calculatePulseRadius(zoomLevel: Float): Float {
        return Math.pow(2.0, 16 - zoomLevel.toDouble()).toFloat() * 160
    }

    private fun startPulsAnimation(latLng: LatLng) {
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
                CaseType.DEATHS -> Color.argb(70, 226, 108, 90)
                CaseType.RECOVERED -> Color.argb(70, 0, 204, 153)
                else -> Color.argb(70, 242, 185, 0)

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
        const val LAT_DEFAULT = 30.360227
        const val LON_DEFAULT = 114.8260094
        const val DATA = "data"
        const val TYPE = "type"

        @JvmStatic
        fun newInstance(data: ArrayList<CovidDetail>, caseType: Int) =
            VisualMapsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(DATA, data)
                    putInt(TYPE, caseType)
                }
            }
    }


}
