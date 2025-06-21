package br.edu.ifsp.dmo.ambientum.ui.monitor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.edu.ifsp.dmo.ambientum.R
import br.edu.ifsp.dmo.ambientum.adapter.CarouselAdapter
import br.edu.ifsp.dmo.ambientum.databinding.FragmentMonitorBinding
import br.edu.ifsp.dmo.ambientum.data.model.CarouselItem
import br.edu.ifsp.dmo.ambientum.data.model.Quality

class MonitorFragment : Fragment() {

    private var _binding: FragmentMonitorBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CarouselAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonitorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configCarousel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun configCarousel() {
        val mockItems = listOf(
            CarouselItem("Montanha", R.color.black, "Belas montanhas", Quality.EXCELLENT),
            CarouselItem("Lago", R.drawable.default_image, "O lago está ideal", Quality.GOOD),
            CarouselItem("Cidade", R.drawable.default_image, "A cidade está moderada", Quality.MODERATE)
        )

        adapter = CarouselAdapter(mockItems, requireContext())
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
}
