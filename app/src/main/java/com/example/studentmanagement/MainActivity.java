package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentmanagement.adapter.StudentAdapter;
import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements StudentAdapter.OnMapClickListener,
        StudentAdapter.OnEditClickListener, StudentAdapter.OnDeleteClickListener {
    private ListView listViewStudents;
    private Button btnAddStudent, btnAddMajor, btnSignOut;
    private List<Student> studentList = new ArrayList<>();
    private List<Major> majorList = new ArrayList<>();
    private StudentAdapter adapter;
    private GoogleSignInClient googleSignInClient;
    private boolean studentsFetched = false;
    private boolean majorsFetched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewStudents = findViewById(R.id.listViewStudents);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnAddMajor = findViewById(R.id.btnAddMajor);
        btnSignOut = findViewById(R.id.btnSignIn); // Đổi tên thành btnSignOut để rõ nghĩa

        adapter = new StudentAdapter(this, R.layout.dong_student, studentList, majorList, this, this, this);
        listViewStudents.setAdapter(adapter);

        // Thiết lập Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Kiểm tra trạng thái đăng nhập
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            // Nếu chưa đăng nhập, chuyển về LoginActivity
            navigateToLoginActivity();
            return;
        }

        btnSignOut.setText("Đăng Xuất");

        fetchStudents();
        fetchMajors();

        btnAddStudent.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivityForResult(intent, 1);
        });

        btnAddMajor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddMajorActivity.class);
            startActivityForResult(intent, 2);
        });

        btnSignOut.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(this, task -> {
                Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                navigateToLoginActivity();
            });
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            studentsFetched = false;
            majorsFetched = false;
            fetchStudents();
            fetchMajors();
        }
    }

    private void fetchStudents() {
        RetrofitClient.getApiService().getStudents().enqueue(new Callback<List<Student>>() {
            @Override
            public void onResponse(Call<List<Student>> call, Response<List<Student>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    studentList.clear();
                    studentList.addAll(response.body());
                    studentsFetched = true;
                    updateAdapterIfReady();
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy danh sách sinh viên", Toast.LENGTH_SHORT).show();
                    studentsFetched = true;
                    updateAdapterIfReady();
                }
            }

            @Override
            public void onFailure(Call<List<Student>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Không thể lấy danh sách sinh viên: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                studentsFetched = true;
                updateAdapterIfReady();
            }
        });
    }

    private void fetchMajors() {
        RetrofitClient.getApiService().getMajors().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majorList.clear();
                    majorList.addAll(response.body());
                    majorsFetched = true;
                    updateAdapterIfReady();
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy danh sách ngành học", Toast.LENGTH_SHORT).show();
                    majorsFetched = true;
                    updateAdapterIfReady();
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Không thể lấy danh sách ngành học: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                majorsFetched = true;
                updateAdapterIfReady();
            }
        });
    }

    private void updateAdapterIfReady() {
        if (studentsFetched && majorsFetched) {
            adapter.notifyDataSetChanged();
        }
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Đóng MainActivity để không quay lại được
    }

    @Override
    public void onMapClick(String address) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        intent.putExtra("address", address);
        startActivity(intent);
    }

    @Override
    public void onEditClick(Student student) {
        Intent intent = new Intent(MainActivity.this, EditStudentActivity.class);
        intent.putExtra("student", student);
        startActivityForResult(intent, 3);
    }

    @Override
    public void onDeleteClick(String studentId) {
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa sinh viên này không?");
        dialogXoa.setPositiveButton("Yes", (dialog, which) -> {
            RetrofitClient.getApiService().deleteStudent(studentId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Xóa sinh viên thành công", Toast.LENGTH_SHORT).show();
                        studentsFetched = false;
                        majorsFetched = false;
                        fetchStudents();
                        fetchMajors();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Không thể xóa sinh viên", Toast.LENGTH_SHORT).show();
                }
            });
        });
        dialogXoa.setNegativeButton("No", null);
        dialogXoa.show();
    }
}