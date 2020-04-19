package com.example.covid19_android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.covid19_android.*
import com.example.covid19_android.viewmodel.MainViewModel
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.modal_bottom_sheet.*

// TODO: fix configuration issue of filter bottom sheet
class MapFragment: Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val modalBottomSheet = ModalBottomSheet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { mapboxMap ->

            val circleLayerFeatures: MutableList<Feature> = ArrayList()

            viewModel.getCountriesData().observe(viewLifecycleOwner, Observer { resource ->
                when(resource) {
                    is Resource.Success -> {

                        val list = resource.data.sortedBy { it.cases }
                        val min = list[0].cases
                        val max = list[list.lastIndex].cases

                        list.forEach {
                            it.countryInfo?.apply {
                                if(it.cases == 0) return@apply

                                val radiusCases = mapRange(
                                    it.cases.toDouble(),
                                    min.toDouble(),
                                    max.toDouble(),
                                    1.toDouble(),
                                    25.toDouble()
                                )

                                val radiusDeath = mapRange(
                                    it.deaths.toDouble(),
                                    0.toDouble(),
                                    it.cases.toDouble(),
                                    0.toDouble(),
                                    radiusCases
                                )

                                val radiusRecovered = radiusDeath + mapRange(
                                    it.recovered.toDouble(),
                                    0.toDouble(),
                                    it.cases.toDouble(),
                                    0.toDouble(),
                                    radiusCases
                                )

                                val feature = Feature.fromGeometry(
                                    Point.fromLngLat(long, lat)
                                )

                                feature.addNumberProperty(CASES_PROP_KEY, it.cases)

                                feature.addNumberProperty(RADIUS_CASES_PROP_KEY, radiusCases)
                                feature.addNumberProperty(RADIUS_DEATH_PROP_KEY, radiusDeath)
                                feature.addNumberProperty(RADIUS_RECOVERED_PROP_KEY, radiusRecovered)

                                circleLayerFeatures.add(feature)
                            }
                        }

                        val source = GeoJsonSource(
                            SOURCE_ID, FeatureCollection.fromFeatures(circleLayerFeatures)
                        )

                        val colorActive = ContextCompat.getColor(requireContext(), R.color.colorMapActive)
                        val layerCases = CircleLayer(LAYER_CASES, source.id)
                            .withProperties(
                                circleColor(colorActive),
                                circleRadius(get(RADIUS_CASES_PROP_KEY))
                            )
                            .withFilter(gt(get(CASES_PROP_KEY), 0))

                        val colorDeath = ContextCompat.getColor(requireContext(), R.color.colorMapDeaths)
                        val layerDeath = CircleLayer(LAYER_DEATH, source.id)
                            .withProperties(
                                circleColor(colorDeath),
                                circleRadius(get(RADIUS_DEATH_PROP_KEY))
                            )
                            .withFilter(gt(get(CASES_PROP_KEY), 0))

                        val colorRecovered = ContextCompat.getColor(requireContext(), R.color.colorMapRecovered)
                        val layerRecovered = CircleLayer(LAYER_RECOVERED, source.id)
                            .withProperties(
                                circleColor(colorRecovered),
                                circleRadius(get(RADIUS_RECOVERED_PROP_KEY))
                            )
                            .withFilter(gt(get(CASES_PROP_KEY), 0))

                        modalBottomSheet.setSeekBarListener(object :
                            SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(
                                seekBar: SeekBar?,
                                progress: Int,
                                fromUser: Boolean
                            ) {
                                val filter = gt(get(CASES_PROP_KEY), progress)
                                layerCases.setFilter(filter)
                                layerDeath.setFilter(filter)
                                layerRecovered.setFilter(filter)

                                modalBottomSheet.progressText.text = formatNumber(progress)
                            }

                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })

                        casesFilterButton.setOnClickListener {
                            activity?.apply {
                                modalBottomSheet.show(
                                    supportFragmentManager,
                                    ModalBottomSheet.TAG
                                )
                            }
                        }

                        mapboxMap.setStyle(
                            Style.Builder().fromUri(getString(R.string.mapbox_map_url))
                                .withSource(source)
                                .withLayers(layerCases, layerRecovered, layerDeath)
                        )
                    }
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    companion object {
        private const val SOURCE_ID = "source_id"
        private const val LAYER_CASES = "layer_cases"
        private const val LAYER_RECOVERED = "layer_recovered"
        private const val LAYER_DEATH = "layer_death"

        private const val CASES_PROP_KEY = "cases"
        private const val RADIUS_CASES_PROP_KEY = "radius_cases"
        private const val RADIUS_RECOVERED_PROP_KEY = "radius_recovered"
        private const val RADIUS_DEATH_PROP_KEY = "radius_death"
    }
}
