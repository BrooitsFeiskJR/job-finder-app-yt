package dev.tontech.job_finder_yt.data.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dev.tontech.job_finder_yt.data.model.Job
import dev.tontech.job_finder_yt.data.model.enums.HomeUiState
import dev.tontech.job_finder_yt.data.repositories.AuthenticationFirebaseRepository
import dev.tontech.job_finder_yt.data.repositories.JobsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repository: JobsRepository) : ViewModel() {
    private val _jobs = MutableLiveData<List<Job>>()
    val jobs: LiveData<List<Job>>
        get() = _jobs

    private val _homeUIState = MutableStateFlow(HomeUiState.LOADING)
    val homeUiState: StateFlow<HomeUiState>
        get() = _homeUIState.asStateFlow()

    fun createJob(jobs: List<Job>) {
        viewModelScope.launch {
            try {
                _homeUIState.value = HomeUiState.LOADING
                repository.createNewJob(jobs)
                _homeUIState.value = HomeUiState.SUCCESS
            } catch (e: Exception) {
                _homeUIState.value = HomeUiState.ERROR
            }
        }
    }

    fun readJobsFromRepo(query: String) {
        viewModelScope.launch {
            repository.readJobs(query, _jobs)
        }
    }

    fun favoriteJob(job: Job, favStatusUpdate: Boolean) {
        viewModelScope.launch {
            repository.updateJobFavoriteStatus(job, favStatusUpdate, _jobs)
        }
    }

    fun signOutCurrentUser() {
        repository.signOut()
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
       val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
           override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
               val repo = JobsRepository(FirebaseDatabase.getInstance(), AuthenticationFirebaseRepository(Firebase.auth))
               return HomeViewModel(repo) as T
           }
       }
    }
}