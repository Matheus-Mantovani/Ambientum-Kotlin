package br.edu.ifsp.dmo.ambientum.core.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class HumidityHelper(private val context: Context, private val callback: Callback) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

    companion object {
        fun isSensorAvailable(context: Context): Boolean {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            return sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null
        }
    }

    fun start() {
        humiditySensor?.let {
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
            val humidity = it.values[0]
            callback.onHumidityReading(humidity)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    interface Callback {
        fun onHumidityReading(humidity: Float)
    }
}