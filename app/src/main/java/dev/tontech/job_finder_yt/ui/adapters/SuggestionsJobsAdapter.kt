package dev.tontech.job_finder_yt.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tontech.job_finder_yt.databinding.SuggestionCardBinding

class SuggestionsJobsAdapter(private val items: List<String>):
    RecyclerView.Adapter<SuggestionsJobsAdapter.ViewHolder>() {
    inner class ViewHolder(binding: SuggestionCardBinding): RecyclerView.ViewHolder(binding.root) {
        val btn = binding.btnSuggestion
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(SuggestionCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sug = items[position]
        holder.btn.text = sug
    }
}