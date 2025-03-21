package com.example.studentmanagement;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.studentmanagement.adapter.StudentAdapter;
import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentManagement extends AppCompatActivity implements StudentAdapter.OnMapClickListener,
        StudentAdapter.OnEditClickListener, StudentAdapter.OnDeleteClickListener {
    ListView lvStudent;
    ArrayList<Student> arrayStudent;
    ArrayList<Major> majorList; // Thêm danh sách ngành học
    StudentAdapter adapter;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvStudent = findViewById(R.id.listViewStudent);
        arrayStudent = new ArrayList<>();
        majorList = new ArrayList<>(); // Khởi tạo danh sách ngành học

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getApiService();

        adapter = new StudentAdapter(this, R.layout.dong_student, arrayStudent, majorList, this, this, this);
        lvStudent.setAdapter(adapter);

        GetDataStudent();
        GetDataMajors(); // Lấy dữ liệu ngành học
    }

    private void GetDataStudent() {
        apiService.getStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    arrayStudent.clear();
                    arrayStudent.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(StudentManagement.this, "Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetDataMajors() {
        apiService.getMajors().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majorList.clear();
                    majorList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                Toast.makeText(StudentManagement.this, "Lỗi kết nối API khi lấy danh sách ngành học!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd) {
            DialogThemStudent();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogThemStudent() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_them_student);
        EditText edtName = dialog.findViewById(R.id.editTextAddName);
        EditText edtDate = dialog.findViewById(R.id.editTextAddDate);
        EditText edtGender = dialog.findViewById(R.id.editTextAddGender);
        EditText edtEmail = dialog.findViewById(R.id.editTextAddEmail);
        EditText edtAddress = dialog.findViewById(R.id.editTextAddAddress);
        EditText edtPhone = dialog.findViewById(R.id.editTextAddPhone);
        EditText edtMajorId = dialog.findViewById(R.id.editTextAddMajorId);
        Button btnThem = dialog.findViewById(R.id.buttonAddStudent);
        Button btnClose = dialog.findViewById(R.id.buttonClose);

        btnThem.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String date = edtDate.getText().toString();
            String gender = edtGender.getText().toString();
            String email = edtEmail.getText().toString();
            String address = edtAddress.getText().toString();
            String phone = edtPhone.getText().toString();
            String majorId = edtMajorId.getText().toString();

            if (name.isEmpty() || date.isEmpty() || gender.isEmpty() || email.isEmpty() ||
                    address.isEmpty() || phone.isEmpty() || majorId.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Student newStudent = new Student();
            newStudent.setName(name);
            newStudent.setDate(date);
            newStudent.setGender(gender);
            newStudent.setEmail(email);
            newStudent.setAddress(address);
            newStudent.setPhone(phone);
            newStudent.setIdMajor(majorId);

            apiService.addStudent(newStudent).enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(StudentManagement.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        GetDataStudent();
                    } else {
                        Toast.makeText(StudentManagement.this, "Lỗi khi thêm: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Toast.makeText(StudentManagement.this, "Lỗi khi thêm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void DialogXoaStudent(String id) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa sinh viên này không?");
        dialogXoa.setPositiveButton("Yes", (dialog, which) -> {
            apiService.deleteStudent(id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(StudentManagement.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                        GetDataStudent();
                        GetDataMajors();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(StudentManagement.this, "Lỗi khi xóa!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialogXoa.setNegativeButton("No", null);
        dialogXoa.show();
    }

    @Override
    public void onMapClick(String address) {
        Intent intent = new Intent(StudentManagement.this, MapActivity.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Student student) {
        Intent intent = new Intent(StudentManagement.this, EditStudentActivity.class);
        intent.putExtra("student", student);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onDeleteClick(String studentId) {
        DialogXoaStudent(studentId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            GetDataStudent();
            GetDataMajors();
        }
    }
}