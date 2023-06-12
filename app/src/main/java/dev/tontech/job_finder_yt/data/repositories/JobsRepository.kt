package dev.tontech.job_finder_yt.data.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.tontech.job_finder_yt.data.model.Job

class JobsRepository(firebaseDatabase: FirebaseDatabase, private val auth: AuthenticationFirebaseRepository) {
    private val db = firebaseDatabase.reference

    fun createNewJob(jobs: List<Job>): Task<Void> {
        return db.child("jobs").setValue(jobs)
    }

    fun readJobs(query: String, jobsLiveData: MutableLiveData<List<Job>>) {
        val queryRef = db.child("jobs")

        queryRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobs = mutableListOf<Job>()
                for (childSnapshot in snapshot.children) {
                    val job = childSnapshot.getValue(Job::class.java)
                    job?.let {
                        if (it.title.contains(query, ignoreCase = true)) {
                            jobs.add(it)
                        }
                    }
                }
                jobsLiveData.postValue(jobs)
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

    fun updateJobFavoriteStatus(
        job: Job,
        isFav: Boolean,
        jobsLiveData: MutableLiveData<List<Job>>
    ) {
        val jobRef = db.child("jobs")

        jobRef.orderByChild("id").equalTo(job.id).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jobId = snapshot.key
                val favRef = jobRef.child(jobId!!).child("favorite")
                favRef.setValue(isFav).addOnSuccessListener {
                    val updateJob = jobsLiveData.value?.map {if (it.id == job.id) it.copy(isFavorite = isFav) else it }
                    jobsLiveData.value = updateJob
                }.addOnFailureListener {
                    Log.e(TAG, "Error to update the favorite status")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.details)
            }
        })
    }

    fun signOut() {
        auth.signOut()
    }

    companion object {
        private const val TAG = "Job_Repository"
    }
}