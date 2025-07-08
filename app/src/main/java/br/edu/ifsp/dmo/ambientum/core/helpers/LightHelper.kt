package br.edu.ifsp.dmo.ambientum.core.helpers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class LightHelper(private val context: Context, private val callback: Callback) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    companion object {
        fun isSensorAvailable(context: Context): Boolean {
            val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            return sm.getDefaultSensor(Sensor.TYPE_LIGHT) != null
        }
    }

    fun start() {
        lightSensor?.let {
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
            val light = it.values[0]
            callback.onLightReading(light)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    interface Callback {
        fun onLightReading(light: Float)
    }
}