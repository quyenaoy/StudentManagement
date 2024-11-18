package vn.edu.hust.studentman

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )

  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(students, { position, updatedStudent ->
      // Xử lý cập nhật sinh viên nếu cần
    }, { removedStudent ->
      // Xử lý khi sinh viên bị xóa
    })

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddNewStudentDialog()
    }

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddNewStudentDialog()
    }
  }

  //Thêm sinh viên mới vào danh sách
  private fun showAddNewStudentDialog() {
    val dialog = Dialog(this)
    dialog.setContentView(R.layout.dialog_add_new_student)

    val editTextName: EditText = dialog.findViewById(R.id.editTextName)
    val editTextStudentId: EditText = dialog.findViewById(R.id.editTextStudentId)
    val buttonSubmit: Button = dialog.findViewById(R.id.buttonSubmit)

    buttonSubmit.setOnClickListener {
      val name = editTextName.text.toString()
      val studentId = editTextStudentId.text.toString()

      if (name.isNotEmpty() && studentId.isNotEmpty()) {
        // Thêm sinh viên mới vào danh sách
        students.add(StudentModel(name, studentId))
        studentAdapter.notifyItemInserted(students.size - 1)
        dialog.dismiss()
      }
    }

    dialog.show()
  }

  //Chỉnh sửa thông tin sinh viên
  private fun showEditStudentDialog(student: StudentModel) {
    val dialog = Dialog(this)
    dialog.setContentView(R.layout.dialog_add_new_student)

    val editTextName: EditText = dialog.findViewById(R.id.editTextName)
    val editTextStudentId: EditText = dialog.findViewById(R.id.editTextStudentId)
    val buttonSubmit: Button = dialog.findViewById(R.id.buttonSubmit)

    // Điền thông tin hiện tại vào dialog
    editTextName.setText(student.studentName)
    editTextStudentId.setText(student.studentId)

    buttonSubmit.setOnClickListener {
      val name = editTextName.text.toString()
      val studentId = editTextStudentId.text.toString()

      if (name.isNotEmpty() && studentId.isNotEmpty()) {
        // Cập nhật thông tin sinh viên
        student.studentName = name
        student.studentId = studentId

        // Thông báo cho adapter về sự thay đổi
        studentAdapter.notifyDataSetChanged()
        dialog.dismiss()
      }
    }

    dialog.show()
  }
}