package com.example.studentmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.model.Student;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStudentActivity extends AppCompatActivity {
    private EditText etStudentId, etName, etDate, etGender, etEmail, etAddress, etPhone, etMajorId;
    private Button btnSaveStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etStudentId = findViewById(R.id.etStudentId);
        etName = findViewById(R.id.etName);
        etDate = findViewById(R.id.etDate);
        etGender = findViewById(R.id.etGender);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etMajorId = findViewById(R.id.etMajorId);
        btnSaveStudent = findViewById(R.id.btnSaveStudent);

        btnSaveStudent.setOnClickListener(v -> {
            String studentId = etStudentId.getText().toString();
            String name = etName.getText().toString();
            String date = etDate.getText().toString();
            String gender = etGender.getText().toString();
            String email = etEmail.getText().toString();
            String address = etAddress.getText().toString();
            String phone = etPhone.getText().toString();
            String majorId = etMajorId.getText().toString();

            if (studentId.isEmpty() || name.isEmpty() || date.isEmpty() || gender.isEmpty() ||
                    email.isEmpty() || address.isEmpty() || phone.isEmpty() || majorId.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Student student = new Student();
            student.setID(studentId);
            student.setName(name);
            student.setDate(date);
            student.setGender(gender);
            student.setEmail(email);
            student.setAddress(address);
            student.setPhone(phone);
            student.setIdMajor(majorId);

            RetrofitClient.getApiService().addStudent(student).enqueue(new Callback<Student>() {
                @Override
                public void onResponse(Call<Student> call, Response<Student> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddStudentActivity.this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Student> call, Throwable t) {
                    Toast.makeText(AddStudentActivity.this, "Không thể thêm sinh viên", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
