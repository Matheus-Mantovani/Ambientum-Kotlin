package br.edu.ifsp.dmo.ambientum.ui.monitor

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ifsp.dmo.ambientum.R
import br.edu.ifsp.dmo.ambientum.adapter.CarouselAdapter
import br.edu.ifsp.dmo.ambientum.core.exceptions.SensorNotAvailableException
import br.edu.ifsp.dmo.ambientum.core.helpers.HumidityHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.LightHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.PressureHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.TemperatureHelper
import br.edu.ifsp.dmo.ambientum.databinding.FragmentMonitorBinding
import br.edu.ifsp.dmo.ambientum.data.model.CarouselItem
import br.edu.ifsp.dmo.ambientum.data.model.Quality
import br.edu.ifsp.dmo.ambientum.util.SensorConstants

class MonitorFragment : Fragment(), PressureHelper.Callback, LightHelper.Callback, HumidityHelper.Callback, TemperatureHelper.Callback {

    private var _binding: FragmentMonitorBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarouselAdapter
    private lateinit var pressureHelper: PressureHelper
    private lateinit var lightHelper: LightHelper
    private lateinit var humidityHelper: HumidityHelper
    private lateinit var temperatureHelper: TemperatureHelper

    private val carouselItems = hashMapOf(
        SensorConstants.SENSOR_PRESSURE to CarouselItem("Pressure", R.drawable.pressure_image, "Initial pressure", Quality.EXCELLENT),
        SensorConstants.SENSOR_LIGHT to CarouselItem("Light", R.drawable.light_image, "Initial light", Quality.EXCELLENT),
        SensorConstants.SENSOR_HUMIDITY to CarouselItem("Humidity", R.drawable.humidity_image, "Initial humidity", Quality.EXCELLENT),
        SensorConstants.SENSOR_TEMPERATURE to CarouselItem("Temperature", R.drawable.temperature_image, "Initial temperature", Quality.EXCELLENT)
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pressureHelper = PressureHelper(requireContext(), this)
        lightHelper = LightHelper(requireContext(), this)
        humidityHelper = HumidityHelper(requireContext(), this)
        temperatureHelper = TemperatureHelper(requireContext(), this)

        configCarousel(carouselItems)
    }

    override fun onResume() {
        super.onResume()

        if(PressureHelper.isSensorAvailable(requireContext())) {
            pressureHelper.start()
        } else {
            simulatePressureReading()
        }

        if(HumidityHelper.isSensorAvailable(requireContext())) {
            humidityHelper.start()
        } else {
            simulateHumidityReading()
        }

        if(TemperatureHelper.isSensorAvailable(requireContext())) {
            temperatureHelper.start()
        } else {
            simulateTemperatureReading()
        }

        if(LightHelper.isSensorAvailable(requireContext())) {
            lightHelper.start()
        } else {
            simulateLightReading()
        }

    }

    override fun onPause() {
        super.onPause()
        pressureHelper.stop()
        humidityHelper.stop()
        temperatureHelper.stop()
        lightHelper.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configCarousel(mockItems: HashMap<String, CarouselItem>) {
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

        updateCarouselItem(SensorConstants.SENSOR_PRESSURE, desc, quality, pressure)
    }

    override fun onLightReading(light: Float) {
        val quality = getQuality(SensorConstants.SENSOR_LIGHT, light)
        val desc = getDesc(SensorConstants.SENSOR_LIGHT, quality)

        updateCarouselItem(SensorConstants.SENSOR_LIGHT, desc, quality, light)
    }

    override fun onHumidityReading(humidity: Float) {
        val quality = getQuality(SensorConstants.SENSOR_HUMIDITY, humidity)
        val desc = getDesc(SensorConstants.SENSOR_HUMIDITY, quality)

        updateCarouselItem(SensorConstants.SENSOR_HUMIDITY, desc, quality, humidity)
    }

    override fun onTemperatureReading(temperature: Float) {
        val quality = getQuality(SensorConstants.SENSOR_TEMPERATURE, temperature)
        val desc = getDesc(SensorConstants.SENSOR_TEMPERATURE, quality)

        updateCarouselItem(SensorConstants.SENSOR_TEMPERATURE, desc, quality, temperature)
    }

    private fun updateCarouselItem(nome: String, desc: String, quality: Quality, value: Float) {
        carouselItems[nome]?.let {
            it.desc = desc
            it.quality = quality
            it.value = value
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

            SensorConstants.SENSOR_LIGHT -> when {
                value in 300f..500f -> Quality.EXCELLENT
                value in 200f..300f || value in 501f..1000f -> Quality.GOOD
                value in 100f..200f -> Quality.MODERATE
                value in 50f..100f -> Quality.POOR
                value < 50f || value > 10000f -> Quality.VERY_POOR
                else -> Quality.MODERATE
            }

            SensorConstants.SENSOR_HUMIDITY -> when {
                value in 40f..60f -> Quality.EXCELLENT
                value in 30f..40f || value in 61f..70f -> Quality.GOOD
                value in 20f..29f || value in 71f..80f -> Quality.MODERATE
                value in 10f..19f || value in 81f..90f -> Quality.POOR
                value < 10f || value > 90f -> Quality.VERY_POOR
                else -> Quality.MODERATE
            }

            SensorConstants.SENSOR_TEMPERATURE -> when {
                value in 20f..24f -> Quality.EXCELLENT
                value in 18f..20f || value in 25f..26f -> Quality.GOOD
                value in 16f..18f || value in 26f..28f -> Quality.MODERATE
                value in 14f..16f || value in 28f..30f -> Quality.POOR
                value < 14f || value > 30f -> Quality.VERY_POOR
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

            SensorConstants.SENSOR_LIGHT -> when (quality) {
                Quality.EXCELLENT -> "Ideal for reading and visual tasks"
                Quality.GOOD -> "Comfortable, but may cause fatigue over time"
                Quality.POOR -> "Too dark or excessively bright"
                Quality.VERY_POOR -> "Insufficient for visual work"
                else -> "Dimly lit"
            }

            SensorConstants.SENSOR_HUMIDITY -> when (quality) {
                Quality.EXCELLENT -> "Ideal for human comfort and health"
                Quality.GOOD -> "Acceptable for human comfort and health"
                Quality.POOR -> "Not ideal for human comfort and health"
                Quality.VERY_POOR -> "Extremely uncomfortable and harmful"
                else -> "May cause dryness or mold"
            }

            SensorConstants.SENSOR_TEMPERATURE -> when (quality) {
                Quality.EXCELLENT -> "Ideal for human comfort"
                Quality.GOOD -> "Slightly outside ideal range"
                Quality.POOR -> "Uncomfortable for most people"
                Quality.VERY_POOR -> "Severe discomfort"
                else -> "Mild discomfort"
            }

            else -> throw SensorNotAvailableException(sensor)
        }
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

    private fun simulateHumidityReading() {
        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                val simulatedHumidity = 40f + (Math.random() * 10.5 - 5).toFloat()
                onHumidityReading(simulatedHumidity)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    private fun simulateTemperatureReading() {
        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                val simulatedTemperature = 19f + (Math.random() * 10.5 - 5).toFloat()
                onTemperatureReading(simulatedTemperature)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    private fun simulateLightReading() {
        val handler = android.os.Handler()
        val runnable = object : Runnable {
            override fun run() {
                val simulatedLight = 300f + (Math.random() * 10.5 - 5).toFloat()
                onLightReading(simulatedLight)
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }
}
