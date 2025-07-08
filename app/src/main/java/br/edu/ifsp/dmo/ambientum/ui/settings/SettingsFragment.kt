package br.edu.ifsp.dmo.ambientum.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.ambientum.adapter.SettingsAdapter
import br.edu.ifsp.dmo.ambientum.core.helpers.HumidityHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.LightHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.PressureHelper
import br.edu.ifsp.dmo.ambientum.core.helpers.TemperatureHelper
import br.edu.ifsp.dmo.ambientum.databinding.FragmentSettingsBinding
import br.edu.ifsp.dmo.ambientum.data.model.SensorSetting
import br.edu.ifsp.dmo.ambientum.ui.start.StartActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SettingsAdapter
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsItems = listOf(
            SensorSetting("Light Sensor", true, LightHelper.isSensorAvailable(requireContext())),
            SensorSetting("Humidity Sensor", true, HumidityHelper.isSensorAvailable(requireContext())),
            SensorSetting("Temperature Sensor", true, TemperatureHelper.isSensorAvailable(requireContext())),
            SensorSetting("Pressure Sensor", true, PressureHelper.isSensorAvailable(requireContext()))
        )

        configRecyclerView(settingsItems)
        configListeners()
    }

    private fun configListeners() {
        binding.btnLogout.setOnClickListener {
            if(firebaseAuth.currentUser != null) {
                firebaseAuth.signOut()
                startActivity(Intent(requireContext(), StartActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configRecyclerView(settingsItems: List<SensorSetting>) {
        adapter = SettingsAdapter(settingsItems) { sensor ->
            Toast.makeText(
                requireContext(), "${sensor.name} -> ${sensor.isEnabled}", Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerSensors.adapter = adapter
        binding.recyclerSensors.layoutManager = LinearLayoutManager(requireContext())
    }
}