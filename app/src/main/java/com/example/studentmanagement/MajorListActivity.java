package com.example.studentmanagement;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmanagement.model.Major;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MajorListActivity extends AppCompatActivity {

    private ListView listViewMajors;
    private ArrayList<String> majorNames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_list);

        listViewMajors = findViewById(R.id.listViewMajors);
        majorNames = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, majorNames);
        listViewMajors.setAdapter(adapter);

        fetchMajors(); // Gọi API để lấy danh sách ngành học
    }

    private void fetchMajors() {
        RetrofitClient.getApiService().getMajors().enqueue(new Callback<List<Major>>() {
            @Override
            public void onResponse(Call<List<Major>> call, Response<List<Major>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    majorNames.clear();
                    for (Major major : response.body()) {
                        majorNames.add(major.getNameMajor()); // Giả sử Major có phương thức getName()
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MajorListActivity.this, "Không thể lấy danh sách ngành học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Major>> call, Throwable t) {
                Toast.makeText(MajorListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
