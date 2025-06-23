package br.edu.ifsp.dmo.ambientum.core.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class TemperatureHelper(private val context: Context, private val callback: Callback) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    val isTemperatureSensorAvailable: Boolean = temperatureSensor != null

    fun start() {
        temperatureSensor?.let {
            sensorManager.registerListener(
                this, it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val temperature = it.values[0]
            callback.onTemperatureReading(temperature)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    interface Callback {
        fun onTemperatureReading(temperature: Float)
    }
}