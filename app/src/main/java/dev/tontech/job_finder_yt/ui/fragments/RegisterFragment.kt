package dev.tontech.job_finder_yt.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.tontech.job_finder_yt.LoginUiState
import dev.tontech.job_finder_yt.data.viewModels.AuthViewModel
import dev.tontech.job_finder_yt.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var binding: FragmentRegisterBinding? = null
    private val vm: AuthViewModel by viewModels { AuthViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.authUiState.collect {state ->
                    when(state) {
                        LoginUiState.LOADING -> Log.d(TAG, "LOADING")
                        LoginUiState.SUCCESS -> Log.d(TAG, "SUCCESS")
                        LoginUiState.ERROR -> Log.d(TAG, "ERROR")
                    }
                }
            }
        }

        binding?.btnLogin?.setOnClickListener {
            val name: String = binding?.etName?.text.toString()
            val email: String = binding?.etEmail?.text.toString()
            val password: String = binding?.etPassword?.text.toString()
            val phoneNumber: String = binding?.etPhoneNumber?.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && phoneNumber.isNotEmpty()) {
                lifecycleScope.launch {
                    vm.register(email, password)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        private const val TAG = "UiState"
    }
}