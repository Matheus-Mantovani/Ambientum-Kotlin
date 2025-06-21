package br.edu.ifsp.dmo.ambientum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.ambientum.core.ui.CarouselDetailsDialogFragment
import br.edu.ifsp.dmo.ambientum.databinding.ItemCarouselBinding
import br.edu.ifsp.dmo.ambientum.data.model.CarouselItem

class CarouselAdapter(private val items: List<CarouselItem>, private val context: Context) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {

    inner class CarouselViewHolder(val binding: ItemCarouselBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleCarousel.text = item.title
        holder.binding.imgCarousel.setImageResource(item.imageResId)
        holder.binding.btnCarousel.setOnClickListener {
            val dialog = CarouselDetailsDialogFragment(item)
            dialog.show((context as FragmentActivity).supportFragmentManager, "details_dialog")
        }
    }

    override fun getItemCount(): Int = items.size
}

