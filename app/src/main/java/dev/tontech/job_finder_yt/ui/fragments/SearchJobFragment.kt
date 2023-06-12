package dev.tontech.job_finder_yt.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dev.tontech.job_finder_yt.R
import dev.tontech.job_finder_yt.data.model.Job
import dev.tontech.job_finder_yt.data.viewModels.HomeViewModel
import dev.tontech.job_finder_yt.databinding.FragmentSearchJobBinding
import dev.tontech.job_finder_yt.ui.adapters.SearchJobAdapter
import dev.tontech.job_finder_yt.ui.adapters.SuggestionsJobsAdapter
import dev.tontech.job_finder_yt.ui.adapters.decorations.LinearSpacingItemDecoration
import dev.tontech.job_finder_yt.ui.adapters.decorations.VerticalSpaceItemDecoration
import dev.tontech.job_finder_yt.util.SuggestionsList
import java.util.Locale

class SearchJobFragment : Fragment() {
    private var binding: FragmentSearchJobBinding? = null
    private var suggestions: List<String>? = null
    private var suggestionsJobAdapter: SuggestionsJobsAdapter? = null

    private var jobs: List<Job>? = null
    private var jobAdapter: SearchJobAdapter? = null

    private val vm: HomeViewModel by viewModels { HomeViewModel.Factory }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchJobBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchBar = binding?.searchbar

        searchBar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                vm.readJobsFromRepo(query!!)
                binding?.tvTitleSearchJob?.text = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        suggestions = SuggestionsList.list
        setupSuggestionsRecyclerView()
        setupDropDownMenu()


        vm.jobs.observe(viewLifecycleOwner) {updateJobs ->
            updateJobs?.let {
                jobs = it
                val query = searchBar?.query.toString()
                val newText = jobs?.size.toString() + " trabalhos como ${query.lowercase(Locale.ROOT)}\n foram encontrados."
                binding?.tvJobsCountFound?.text = newText
                setupJobsRecyclerView()
            }
        }
    }

    private fun setupSuggestionsRecyclerView() {
        binding?.rvSuggestions?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        suggestionsJobAdapter = suggestions?.let { SuggestionsJobsAdapter(it) }
        binding?.rvSuggestions?.adapter = suggestionsJobAdapter

        val spacing = resources.getDimensionPixelOffset(R.dimen.item_spacing)
        val includeEdge = true
        val headerNum = 0

        val itemDecoration = LinearSpacingItemDecoration(spacing, includeEdge, headerNum)
        binding?.rvSuggestions?.addItemDecoration(itemDecoration)
    }

    private fun setupJobsRecyclerView() {
        binding?.rvJobList?.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
        if ((binding?.rvJobList?.itemDecorationCount ?: 0) > 0) {
            binding?.rvJobList?.removeItemDecorationAt(0)
        }
        val spaceItemDecoration = VerticalSpaceItemDecoration(resources.getDimensionPixelSize(R.dimen.item_spacing))
        binding?.rvJobList?.addItemDecoration(spaceItemDecoration)

        jobAdapter = jobs?.let { SearchJobAdapter(it) { position, isFavorite ->
            val job = it[position]
            vm.favoriteJob(job, isFavorite)
            }
        }
        binding?.rvJobList?.adapter = jobAdapter
    }

    private fun setupDropDownMenu() {
        val items = listOf("Tempo Integral", "Meio Periodo")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding?.autoCompleteText?.setAdapter(adapter)
        binding?.autoCompleteText?.setText(items[0], false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}