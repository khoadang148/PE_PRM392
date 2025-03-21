package com.example.studentmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.model.Major;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMajorActivity extends AppCompatActivity {
    private EditText etMajorId, etMajorName;
    private Button btnSaveMajor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_major);

        etMajorId = findViewById(R.id.etMajorId);
        etMajorName = findViewById(R.id.etMajorName);
        btnSaveMajor = findViewById(R.id.btnSaveMajor);

        btnSaveMajor.setOnClickListener(v -> {
            String majorId = etMajorId.getText().toString();
            String majorName = etMajorName.getText().toString();

            if (majorId.isEmpty() || majorName.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            Major major = new Major();
            major.setIDMajor(majorId);
            major.setNameMajor(majorName);

            RetrofitClient.getApiService().addMajor(major).enqueue(new Callback<Major>() {
                @Override
                public void onResponse(Call<Major> call, Response<Major> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(AddMajorActivity.this, "Thêm ngành học thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Major> call, Throwable t) {
                    Toast.makeText(AddMajorActivity.this, "Không thể thêm ngành học", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}