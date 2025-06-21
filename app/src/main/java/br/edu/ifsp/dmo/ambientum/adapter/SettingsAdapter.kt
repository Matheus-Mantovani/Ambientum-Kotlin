package br.edu.ifsp.dmo.ambientum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.ambientum.R
import br.edu.ifsp.dmo.ambientum.data.model.SensorSetting
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsAdapter(
    private val sensores: List<SensorSetting>,
    private val onToggleChanged: (SensorSetting) -> Unit
) : RecyclerView.Adapter<SettingsAdapter.SensorViewHolder>() {

    inner class SensorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.txt_sensor_name)
        val switch: MaterialSwitch = itemView.findViewById(R.id.switch_status)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SensorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_settings, parent, false)
        return SensorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SensorViewHolder, position: Int) {
        val sensor = sensores[position]
        holder.nameText.text = sensor.name
        holder.switch.isChecked = sensor.isEnabled

        holder.switch.setOnCheckedChangeListener { _, isChecked ->
            sensor.isEnabled = isChecked
            onToggleChanged(sensor)
        }
    }

    override fun getItemCount(): Int = sensores.size
}