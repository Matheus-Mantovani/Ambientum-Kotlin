package br.edu.ifsp.dmo.ambientum.ui.monitor

import android.hardware.Sensor
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ifsp.dmo.ambientum.R
import br.edu.ifsp.dmo.ambientum.adapter.CarouselAdapter
import br.edu.ifsp.dmo.ambientum.core.exceptions.SensorNotAvailableException
import br.edu.ifsp.dmo.ambientum.core.helpers.PressaoHelper
import br.edu.ifsp.dmo.ambientum.databinding.FragmentMonitorBinding
import br.edu.ifsp.dmo.ambientum.data.model.CarouselItem
import br.edu.ifsp.dmo.ambientum.data.model.Quality
import br.edu.ifsp.dmo.ambientum.util.SensorConstants

class MonitorFragment : Fragment(), PressaoHelper.Callback {

    private var _binding: FragmentMonitorBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarouselAdapter
    private lateinit var pressaoHelper: PressaoHelper

    //TODO---------------------------------------------------------------REMOVER DEPOIS---------------------------------------------------------------
    private val mockItems = hashMapOf(
        "Montanha" to CarouselItem("Montanha", R.color.black, "Belas montanhas", Quality.EXCELLENT),
        "Lago" to CarouselItem("Lago", R.drawable.default_image, "O lago está ideal", Quality.GOOD),
        "Cidade" to CarouselItem("Cidade", R.drawable.default_image, "A cidade está moderada", Quality.MODERATE),
        SensorConstants.SENSOR_PRESSURE to CarouselItem("Pressure", R.color.black, "Pressão inicial", Quality.EXCELLENT)
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pressaoHelper = PressaoHelper(requireContext(), this)

        configCarousel()
    }

    override fun onResume() {
        super.onResume()
        if(pressaoHelper.isPressureSensorAvailable) {
            pressaoHelper.start()
        } else {
            simulatePressureReading()
        }
    }

    override fun onPause() {
        super.onPause()
        pressaoHelper.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configCarousel() {
        adapter = CarouselAdapter(mockItems.values.toList(), requireContext())
        binding.carouselViewPager.adapter = adapter
        binding.carouselViewPager.offscreenPageLimit = 3

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.page_margin)
        val pageOffset = resources.getDimensionPixelOffset(R.dimen.offset)

        binding.carouselViewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * pageOffset + pageMargin)
            page.translationX = offset
            page.scaleY = 0.95f + (1 - kotlin.math.abs(position)) * 0.05f
        }
    }

    override fun onPressureReading(pressure: Float) {
        val quality = getQuality(SensorConstants.SENSOR_PRESSURE, pressure)
        val desc = getDesc(SensorConstants.SENSOR_PRESSURE, quality)

        updateCarouselItem(SensorConstants.SENSOR_PRESSURE, desc, quality)
    }

    private fun simulatePressureReading() {
        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                val simulatedPressure = 1013.25f + (Math.random() * 10 - 5).toFloat()
                onPressureReading(simulatedPressure)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    private fun updateCarouselItem(nome: String, desc: String, quality: Quality) {
        mockItems[nome]?.let {
            it.desc = desc
            it.quality = quality
        }
    }

    private fun getQuality(sensor: String, value: Float): Quality {
        return when(sensor) {
            SensorConstants.SENSOR_PRESSURE -> when {
                value in 1010f..1020f -> Quality.EXCELLENT
                value in 1000f..1010f -> Quality.GOOD
                value in 990f..1000f -> Quality.MODERATE
                value in 980f..990f -> Quality.POOR
                value < 980f || value > 1035f -> Quality.VERY_POOR
                else -> Quality.MODERATE
            }

            else -> throw SensorNotAvailableException(sensor)
        }
    }

    private fun getDesc(sensor: String, quality: Quality): String {
        return when(sensor) {
            SensorConstants.SENSOR_PRESSURE -> when (quality) {
                Quality.EXCELLENT -> "Ideal conditions; stable and comfortable weather"
                Quality.GOOD -> "Slight atmospheric variation; still comfortable for most"
                Quality.POOR -> "Unstable pressure; possible discomfort for sensitive individuals"
                Quality.VERY_POOR -> "Extreme weather conditions; higher risk of headache and fatigue"
                else -> "Weather trend changing; may cause mild discomfort"
            }

            else -> throw SensorNotAvailableException(sensor)
        }
    }
}
