package br.edu.ifsp.dmo.ambientum.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.ambientum.adapter.SettingsAdapter
import br.edu.ifsp.dmo.ambientum.databinding.FragmentSettingsBinding
import br.edu.ifsp.dmo.ambientum.data.model.SensorSetting

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mockSensores = listOf(
            SensorSetting("Sensor de Luz", true),
            SensorSetting("Sensor de Som", false),
            SensorSetting("Sensor de Umidade", true),
            SensorSetting("Sensor de Temperatura", false)
        )

        adapter = SettingsAdapter(mockSensores) { sensor ->
            Toast.makeText(
                requireContext(), "${sensor.name} -> ${sensor.isEnabled}", Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerSensors.adapter = adapter
        binding.recyclerSensors.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}