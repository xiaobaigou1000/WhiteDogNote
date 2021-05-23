package ink.xiaobaigou.whitedognote.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ink.xiaobaigou.whitedognote.R
import ink.xiaobaigou.whitedognote.database.entity.User
import ink.xiaobaigou.whitedognote.databinding.FragmentRegisterBinding
import ink.xiaobaigou.whitedognote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            registerUserName.editText?.doAfterTextChanged { _ ->
                registerUserName.error = null
            }
            registerUserPassword.editText?.doAfterTextChanged { _ ->
                registerUserPassword.error = null
            }

            registerButton.setOnClickListener {
                val userName = registerUserName.editText?.text.toString()
                val password = registerUserPassword.editText?.text.toString()

                var fieldsValid = true
                if (userName.isBlank()) {
                    registerUserName.error = getString(R.string.invalid_user_name)
                    fieldsValid = false
                }
                if (password.isBlank()) {
                    registerUserPassword.error = getString(R.string.invalid_password)
                    fieldsValid = false
                }
                if (!fieldsValid) {
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val queriedUser = model.queryUserByName(userName)
                    if (queriedUser != null) {
                        registerUserName.error = getString(R.string.user_already_existed)
                    } else {
                        val insertedUser =
                            model.register(User(username = userName, password = password))
                        if (insertedUser == null) {
                            registerUserName.error = getString(R.string.register_failed)
                        } else {
                            model.currentUser = insertedUser
                            val action = RegisterFragmentDirections.actionToNoteListFragment()
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }
}