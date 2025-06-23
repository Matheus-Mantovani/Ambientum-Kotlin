package br.edu.ifsp.dmo.ambientum.core.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class PressureHelper(private val context: Context, private val callback: Callback) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    val isPressureSensorAvailable: Boolean = pressureSensor != null

    fun start() {
        pressureSensor?.let {
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
            val pressure = it.values[0]
            callback.onPressureReading(pressure)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    interface Callback {
        fun onPressureReading(pressure: Float)
    }
}