package br.edu.ifsp.dmo.ambientum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.ambientum.databinding.ItemCarouselBinding
import br.edu.ifsp.dmo.ambientum.model.CarouselItem

class CarouselAdapter(private val items: List<CarouselItem>) :
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
    }

    override fun getItemCount(): Int = items.size
}

