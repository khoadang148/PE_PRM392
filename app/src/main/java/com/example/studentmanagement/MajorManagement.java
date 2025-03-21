package com.example.studentmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.adapter.MajorActionListener;
import com.example.studentmanagement.adapter.MajorAdapter;
import com.example.studentmanagement.model.Major;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MajorManagement extends AppCompatActivity implements MajorActionListener {
    private ListView listViewMajors;
    private Button btnAddMajor;
    private List<Major> majorList = new ArrayList<>();
    private MajorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_management);

        listViewMajors = findViewById(R.id.listViewMajors);
        btnAddMajor = findViewById(R.id.btnAddMajor);

        adapter = new MajorAdapter(this, R.layout.list_item_major, majorList, this);
        listViewMajors.setAdapter(adapter);

        fetchMajors();

        btnAddMajor.setOnClickListener(v -> {
            Intent intent = new Intent(MajorManagement.this, AddMajorActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void fetchMajors() {
        RetrofitClient.getApiService().getMajors().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful()) {
                    majorList.clear();
                    majorList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                Toast.makeText(MajorManagement.this, "Không thể lấy danh sách ngành học", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteMajor(String nameMajor, String idMajor) {
        DialogXoaMajor(nameMajor, idMajor);
    }

    @Override
    public void onEditMajor(String idMajor, String nameMajor) {
        DialogSuaMajor(idMajor, nameMajor);
    }

    public void DialogXoaMajor(String nameMajor, String idMajor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác Nhận Xóa");
        builder.setMessage("Bạn có chắc muốn xóa ngành học " + nameMajor + " không?");
        builder.setPositiveButton("Có", (dialog, which) -> {
            RetrofitClient.getApiService().deleteMajor(idMajor).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MajorManagement.this, "Xóa ngành học thành công", Toast.LENGTH_SHORT).show();
                        fetchMajors();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MajorManagement.this, "Không thể xóa ngành học", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Không", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void DialogSuaMajor(String idMajor, String nameMajor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa Ngành Học");

        // Tạo EditText để người dùng nhập tên ngành mới
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setText(nameMajor);
        builder.setView(input);

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newName = input.getText().toString();
            if (newName.isEmpty()) {
                Toast.makeText(this, "Tên ngành không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            Major updatedMajor = new Major();
            updatedMajor.setIDMajor(idMajor);
            updatedMajor.setNameMajor(newName);

            RetrofitClient.getApiService().updateMajor(idMajor, updatedMajor).enqueue(new Callback<Major>() {
                @Override
                public void onResponse(Call<Major> call, Response<Major> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MajorManagement.this, "Cập nhật ngành học thành công", Toast.LENGTH_SHORT).show();
                        fetchMajors();
                    }
                }

                @Override
                public void onFailure(Call<Major> call, Throwable t) {
                    Toast.makeText(MajorManagement.this, "Không thể cập nhật ngành học", Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            fetchMajors();
        }
    }
}