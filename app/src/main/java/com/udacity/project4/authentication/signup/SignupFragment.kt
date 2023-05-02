package com.udacity.project4.authentication.signup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentSignupBinding
import com.udacity.project4.locationreminders.RemindersActivity

class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    lateinit var authentication: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        authentication = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupFragmentButton.setOnClickListener {

            launchSignUpFlow()
        }
    }

    private fun launchSignUpFlow() = with(binding) {
        if (!checkAndNotifyIfDataIsEmpty(
                signupFragmentMail,
                signupFragmentPassword
            )
        ) {
            authentication.createUserWithEmailAndPassword(
                signupFragmentMail.text.toString(),
                signupFragmentPassword.text.toString(),
            ).addOnCompleteListener(this@SignupFragment.requireActivity()) { task ->
                if (task.isSuccessful) {
                    startActivity(
                        Intent(
                            this@SignupFragment.requireActivity(),
                            RemindersActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.fragment_signup_error_authentication),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkAndNotifyIfDataIsEmpty(
        signupFragmentMail: EditText,
        signupFragmentPassword: EditText
    ): Boolean {
        val mailEmpty = signupFragmentMail.text.isEmpty()
        val passwordEmpty = signupFragmentPassword.text.isEmpty()

        if (mailEmpty)
            Toast.makeText(
                requireContext(),
                getString(R.string.fragment_signup_warning_mail),
                Toast.LENGTH_SHORT
            ).show()
        if (passwordEmpty)
            Toast.makeText(
                requireContext(),
                getString(R.string.fragment_signup_warning_password),
                Toast.LENGTH_SHORT
            )
                .show()
        return mailEmpty && passwordEmpty
    }

}