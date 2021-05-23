package ink.xiaobaigou.whitedognote.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ink.xiaobaigou.whitedognote.R
import ink.xiaobaigou.whitedognote.databinding.FragmentLoginBinding
import ink.xiaobaigou.whitedognote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.loginRequired = false

        binding.apply {
            navToRegisterButton.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }

            loginUserName.editText?.doOnTextChanged { _, _, _, _ ->
                loginUserName.error = null
            }
            loginUserPassword.editText?.doOnTextChanged { _, _, _, _ ->
                loginUserPassword.error = null
            }

            loginButton.setOnClickListener {
                val userName = loginUserName.editText?.text.toString()
                val password = loginUserPassword.editText?.text.toString()

                var fieldsValid = true
                if (userName.isBlank()) {
                    binding.loginUserName.error = getString(R.string.invalid_user_name)
                    fieldsValid = false
                }
                if (password.isBlank()) {
                    binding.loginUserPassword.error = getString(R.string.invalid_password)
                    fieldsValid = false
                }
                if (!fieldsValid) {
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val queriedUser = model.queryUserByName(userName)
                    if (queriedUser == null) {
                        loginUserName.error = getString(R.string.user_does_not_exist)
                    } else if (queriedUser.password != password) {
                        loginUserPassword.error = getString(R.string.incorrect_password)
                    } else {
                        model.currentUser = queriedUser
                        val action = LoginFragmentDirections.actionToNoteListFragment()
                        findNavController().navigate(action)
                    }
                }
            }

            navToRegisterButton.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            }
        }
    }
}