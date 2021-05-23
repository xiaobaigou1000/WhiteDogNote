package ink.xiaobaigou.whitedognote.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ink.xiaobaigou.whitedognote.R
import ink.xiaobaigou.whitedognote.adapter.NoteListAdapter
import ink.xiaobaigou.whitedognote.database.entity.Note
import ink.xiaobaigou.whitedognote.databinding.FragmentNoteListBinding
import ink.xiaobaigou.whitedognote.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class NoteListFragment : Fragment() {
    lateinit var binding: FragmentNoteListBinding
    val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (model.requestLogin) {
            val action = NoteListFragmentDirections.actionToLoginRegister()
            findNavController().navigate(action)
        } else {
            if (model.currentUser == null) {
                requireActivity().finish()
            }
        }

        binding.apply {
            val adapter = NoteListAdapter(mutableListOf()) { _, note, _ ->
                model.updateCurrentNote(note)
                val action = NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment()
                findNavController().navigate(action)
            }
            noteList.adapter = adapter
            noteList.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            createNewNoteFab.setOnClickListener {
                lifecycleScope.launch {
                    val note = model.currentUser?.let { user ->
                        model.createNote(Note(title = "", content = "", authorId = user.id))
                    }
                    model.updateCurrentNote(note)
                    val action =
                        NoteListFragmentDirections.actionNoteListFragmentToEditNoteFragment()
                    findNavController().navigate(action)
                }
            }

            bottomAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete_selected -> {
                        val noteList = noteList.adapter as NoteListAdapter
                        model.deleteNotesFromDatabase(noteList.selectedData)
                        true
                    }
                    else -> false
                }
            }
            bottomAppBar.setNavigationOnClickListener {
                model.requestLogin = true
                model.currentUser = null
                val action = NoteListFragmentDirections.actionToLoginRegister()
                findNavController().navigate(action)
            }
        }

        model.currentUser?.let { user ->
            model.getNoteListFlow(user).asLiveData(lifecycleScope.coroutineContext)
                .observe(viewLifecycleOwner) {
                    val noteListAdapter = binding.noteList.adapter as NoteListAdapter
                    noteListAdapter.dataList.clear()
                    noteListAdapter.dataList.addAll(it)
                    noteListAdapter.resetSelectedStatusAndNotifyChange()
                }
        }
    }
}