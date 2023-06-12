package dev.tontech.job_finder_yt.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.tontech.job_finder_yt.R
import dev.tontech.job_finder_yt.data.model.Job
import dev.tontech.job_finder_yt.databinding.JobCardDescriptionBinding

class SearchJobAdapter(
    private val jobs: List<Job>,
    private val listener: (position: Int, isFav: Boolean) -> Unit) : RecyclerView.Adapter<SearchJobAdapter.ViewHolder>() {

        inner class ViewHolder(binding: JobCardDescriptionBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener {
            init {
                binding.favoriteIcon.setOnClickListener(this)
            }

            val companyName = binding.tvCompany
            val titleJob = binding.tvJobTitle

            val subTitle = binding.tvJobSubTitle
            val icon = binding.ivIconCompany
            val favoriteIcon = binding.favoriteIcon

            override fun onClick(view: View?) {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val job = jobs[position]

                    when(view?.id) {
                        R.id.favoriteIcon -> {
                            val isFav = !job.isFavorite
                            listener.invoke(position, isFav)
                            notifyItemChanged(adapterPosition)
                        }
                    }
                }

            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(JobCardDescriptionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]
        holder.companyName.text = job.company
        holder.titleJob.text = job.title
        holder.subTitle.text = job.location

        Picasso.get().load(job.iconUrl).into(holder.icon)

        when(job.isFavorite) {
            true -> holder.favoriteIcon.setImageResource(R.drawable.isfavorite_true_icon)
            false -> holder.favoriteIcon.setImageResource(R.drawable.isfavorite_false_icon)
        }
    }
}