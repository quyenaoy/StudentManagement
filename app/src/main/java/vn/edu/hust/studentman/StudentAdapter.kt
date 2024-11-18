package vn.edu.hust.studentman

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(private val studentList: MutableList<StudentModel>, private val onStudentUpdated: (Int, StudentModel) -> Unit, private val onStudentRemoved: (StudentModel) -> Unit) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(view)
  }

  private var removedStudent: StudentModel? = null
  private var removedStudentPosition: Int = -1

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = studentList[position]
    holder.nameTextView.text = student.studentName
    holder.studentIdTextView.text = student.studentId

    // Sự kiện click cho nút chỉnh sửa
    holder.editButton.setOnClickListener {
      showEditDialog(holder.itemView.context, position)
    }

    // Sự kiện click cho nút xóa
    holder.removeButton.setOnClickListener {
      showDeleteConfirmationDialog(holder.itemView.context, position)
    }
  }

  private fun showEditDialog(context: Context, position: Int) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.dialog_edit_student)

    val editTextName: EditText = dialog.findViewById(R.id.editTextName)
    val editTextStudentId: EditText = dialog.findViewById(R.id.editTextStudentId)
    val buttonUpdate: Button = dialog.findViewById(R.id.buttonUpdate)

    // Điền thông tin hiện tại vào dialog
    editTextName.setText(studentList[position].studentName)
    editTextStudentId.setText(studentList[position].studentId)

    buttonUpdate.setOnClickListener {
      val updatedName = editTextName.text.toString()
      val updatedStudentId = editTextStudentId.text.toString()

      if (updatedName.isNotEmpty() && updatedStudentId.isNotEmpty()) {
        // Cập nhật thông tin sinh viên
        val updatedStudent = StudentModel(updatedName, updatedStudentId)
        studentList[position] = updatedStudent
        notifyItemChanged(position) // Cập nhật item trong RecyclerView
        onStudentUpdated(position, updatedStudent)
        dialog.dismiss()
      }
    }

    dialog.show()
  }
  //sự kiện xóa
  private fun showDeleteConfirmationDialog(context: Context, position: Int) {
    val dialog = Dialog(context)
    dialog.setContentView(R.layout.dialog_delete_confirmation)

    val buttonConfirm: Button = dialog.findViewById(R.id.buttonConfirm)
    val buttonCancel: Button = dialog.findViewById(R.id.buttonCancel)

    buttonConfirm.setOnClickListener {
      removedStudent = studentList[position]
      removedStudentPosition = position
      studentList.removeAt(position)
      notifyItemRemoved(position)

      showUndoSnackbar(context)

      dialog.dismiss()
    }

    buttonCancel.setOnClickListener {
      dialog.dismiss()
    }

    dialog.show()
  }

  private fun showUndoSnackbar(context: Context) {
    val snackbar = Snackbar.make((context as MainActivity).findViewById(R.id.recycler_view_students), "Sinh viên đã bị xóa", Snackbar.LENGTH_LONG)
    snackbar.setAction("Hoàn tác") {
      removedStudent?.let {
        studentList.add(removedStudentPosition, it)
        notifyItemInserted(removedStudentPosition)
        removedStudent = null // Reset removed student
      }
    }
    snackbar.show()
  }

  override fun getItemCount(): Int = studentList.size

  class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val nameTextView: TextView = itemView.findViewById(R.id.text_student_name)
    val studentIdTextView: TextView = itemView.findViewById(R.id.text_student_id)
    val editButton: ImageButton = itemView.findViewById(R.id.image_edit)
    val removeButton: ImageButton = itemView.findViewById(R.id.image_remove)
  }
}