package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentmanagement.model.Student;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStudentActivity extends AppCompatActivity {
    private EditText edtName, edtDate, edtGender, edtEmail, edtAddress, edtPhone, edtMajorId;
    private Button btnSave, btnCancel;
    private Student student;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        // Khởi tạo các view
        edtName = findViewById(R.id.editTextEditName);
        edtDate = findViewById(R.id.editTextEditDate);
        edtGender = findViewById(R.id.editTextEditGender);
        edtEmail = findViewById(R.id.editTextEditEmail);
        edtAddress = findViewById(R.id.editTextEditAddress);
        edtPhone = findViewById(R.id.editTextEditPhone);
        edtMajorId = findViewById(R.id.editTextEditMajorId);
        btnSave = findViewById(R.id.buttonSaveStudent);
        btnCancel = findViewById(R.id.buttonCancel);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getApiService();

        // Lấy dữ liệu sinh viên từ Intent
        student = (Student) getIntent().getSerializableExtra("student");
        if (student == null || student.getID() == null) {
            Toast.makeText(this, "Không tìm thấy thông tin sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin sinh viên lên các EditText
        edtName.setText(student.getName());
        edtDate.setText(student.getDate());
        edtGender.setText(student.getGender());
        edtEmail.setText(student.getEmail());
        edtAddress.setText(student.getAddress());
        edtPhone.setText(student.getPhone());
        edtMajorId.setText(student.getIdMajor());

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> {
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

            // Cập nhật thông tin sinh viên
            student.setName(name);
            student.setDate(date);
            student.setGender(gender);
            student.setEmail(email);
            student.setAddress(address);
            student.setPhone(phone);
            student.setIdMajor(majorId);

            // Gọi API để cập nhật sinh viên
            Toast.makeText(this, "Cập nhật sinh viên với ID: " + student.getID(), Toast.LENGTH_SHORT).show();
            apiService.updateStudent(student.getID(), student).enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditStudentActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(EditStudentActivity.this, "Lỗi khi cập nhật: " + response.message() + " (Code: " + response.code() + ")", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Toast.makeText(EditStudentActivity.this, "Lỗi khi cập nhật: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        // Xử lý nút Hủy
        btnCancel.setOnClickListener(v -> finish());
    }
}