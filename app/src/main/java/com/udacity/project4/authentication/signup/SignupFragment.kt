package com.udacity.project4.authentication.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
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
                    startActivity(Intent(this@SignupFragment.requireActivity(), RemindersActivity::class.java))

//                    findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToSaveReminderFragment())
                } else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(requireContext(), "Mail must not be empty", Toast.LENGTH_SHORT).show()
        if (passwordEmpty)
            Toast.makeText(requireContext(), "Password must not be empty", Toast.LENGTH_SHORT)
                .show()
        return mailEmpty && passwordEmpty
    }

}