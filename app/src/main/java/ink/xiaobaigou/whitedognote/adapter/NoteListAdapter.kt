package ink.xiaobaigou.whitedognote.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ink.xiaobaigou.whitedognote.database.entity.Note
import ink.xiaobaigou.whitedognote.databinding.NoteListItemBinding

class NoteListAdapter(
    val dataList: MutableList<Note>,
    private val onClickListener: (ViewHolder, Note, Int) -> Unit
) :
    RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    class ViewHolder(val binding: NoteListItemBinding) : RecyclerView.ViewHolder(binding.root)


    val selectedData = mutableListOf<Note>()

    private val selectedStatus = MutableList(dataList.size) { _ -> false }

    fun deleteSelectedFromView() {
        dataList.removeAll(selectedData)
        resetSelectedStatus()
        notifyDataSetChanged()
    }

    fun resetSelectedStatusAndNotifyChange() {
        resetSelectedStatus()
        notifyDataSetChanged()
    }

    private fun resetSelectedStatus() {
        selectedData.clear()
        selectedStatus.clear()
        selectedStatus.addAll(dataList.map { _ -> false })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NoteListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            noteListItemTitle.text = item.title
            noteListItemContent.text = item.content
            noteCard.isChecked = selectedStatus[position]

            noteCard.setOnClickListener {
                onClickListener(holder, item, position)
            }

            noteCard.setOnLongClickListener {
                selectedStatus[position] = !selectedStatus[position]
                noteCard.isChecked = selectedStatus[position]
                if (noteCard.isChecked) {
                    selectedData.add(item)
                } else {
                    val result = selectedData.find { it == item }
                    result?.let {
                        selectedData.remove(it)
                    }
                }
                true
            }
        }
    }

    override fun getItemCount(): Int = dataList.size
}