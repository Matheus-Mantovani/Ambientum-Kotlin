package br.edu.ifsp.dmo.ambientum.core.ui

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import br.edu.ifsp.dmo.ambientum.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import br.edu.ifsp.dmo.ambientum.databinding.DialogCarouselDetailsBinding
import br.edu.ifsp.dmo.ambientum.data.model.CarouselItem
import br.edu.ifsp.dmo.ambientum.util.SensorConstants

class CarouselDetailsDialogFragment(
    private val item: CarouselItem
) : BottomSheetDialogFragment() {

    private var _binding: DialogCarouselDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogCarouselDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container = view.findViewById<LinearLayout>(R.id.dialog_container)
        val background = container.background.mutate() as GradientDrawable
        val borderColor = ContextCompat.getColor(requireContext(), item.quality.colorResId)

        val unit = when (item.title) {
            SensorConstants.SENSOR_TEMPERATURE -> "°C"
            SensorConstants.SENSOR_HUMIDITY -> "%"
            SensorConstants.SENSOR_LIGHT -> "lux"
            SensorConstants.SENSOR_PRESSURE -> "hPa"
            else -> ""
        }

        val valueText = item.value?.let { String.format("%.1f %s", it, unit) } ?: "N/A"
        binding.txtTitle.text = "${item.title} ($valueText)"
        binding.txtDesc.text = item.desc
        binding.txtQuality.text = "Quality: ${item.quality.label}"
        binding.imgItem.setImageResource(item.imageResId)
        background.setStroke(12, borderColor)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}