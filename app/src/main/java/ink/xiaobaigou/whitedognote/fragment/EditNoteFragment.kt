package ink.xiaobaigou.whitedognote.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ink.xiaobaigou.whitedognote.databinding.FragmentEditNoteBinding
import ink.xiaobaigou.whitedognote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {
    lateinit var binding: FragmentEditNoteBinding
    val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.currentNote.observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    noteTitle.text.append(it.title)
                    noteContent.text.append(it.content)

                    noteTitle.doAfterTextChanged {
                        updateNoteToDatabase()
                    }
                    noteContent.doAfterTextChanged {
                        updateNoteToDatabase()
                    }
                }
            } ?: run {
//                Toast.makeText(requireContext(),"empty current note",Toast.LENGTH_SHORT).show()
                val action=EditNoteFragmentDirections.actionEditNoteFragmentToNoteListFragment()
                findNavController().navigate(action)
            }
        }

        binding.apply {
            deleteCurrentNote.setOnClickListener {
                lifecycleScope.launch {
                    model.deleteCurrentNote()
                }
            }
        }
    }

    private fun updateNoteToDatabase() {
        val currentNote = model.currentNote.value
        currentNote?.let {
            val newNote = currentNote.copy(
                title = binding.noteTitle.text.toString(),
                content = binding.noteContent.text.toString()
            )

            model.updateNoteInDatabase(newNote)
        }
    }
}