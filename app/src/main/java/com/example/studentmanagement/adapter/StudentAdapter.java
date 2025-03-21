package com.example.studentmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.example.studentmanagement.R;
import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private List<Student> students;
    private List<Major> majors;
    private OnMapClickListener mapClickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    public interface OnMapClickListener {
        void onMapClick(String address);
    }

    public interface OnEditClickListener {
        void onEditClick(Student student);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String studentId);
    }

    public StudentAdapter(Context context, int resource, List<Student> students,
                          List<Major> majors, OnMapClickListener mapClickListener,
                          OnEditClickListener editClickListener, OnDeleteClickListener deleteClickListener) {
        super(context, resource, students);
        this.students = students;
        this.majors = majors;
        this.mapClickListener = mapClickListener;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dong_student, parent, false);
        }

        Student student = getItem(position);
        if (student == null) {
            return convertView; // Tránh crash nếu student là null
        }

        TextView tvID = convertView.findViewById(R.id.tvID);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvGender = convertView.findViewById(R.id.tvGender);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvAddress = convertView.findViewById(R.id.tvAddress);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvMajor = convertView.findViewById(R.id.tvMajor);
        Button btnShowOnMap = convertView.findViewById(R.id.btnShowOnMap);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        // Hiển thị thông tin sinh viên với kiểm tra null
        tvID.setText("Mã SV: " + (student.getID() != null ? student.getID() : "N/A"));
        tvName.setText("Tên: " + (student.getName() != null ? student.getName() : "N/A"));
        tvDate.setText("Ngày Sinh: " + (student.getDate() != null ? student.getDate() : "N/A"));
        tvGender.setText("Giới Tính: " + (student.getGender() != null ? student.getGender() : "N/A"));
        tvEmail.setText("Email: " + (student.getEmail() != null ? student.getEmail() : "N/A"));
        tvAddress.setText("Địa Chỉ: " + (student.getAddress() != null ? student.getAddress() : "N/A"));
        tvPhone.setText("Số Điện Thoại: " + (student.getPhone() != null ? student.getPhone() : "N/A"));

        String majorName = "Không xác định";
        if (student.getIdMajor() != null && majors != null) {
            // Debug: Kiểm tra giá trị idMajor của sinh viên
            System.out.println("Student ID: " + student.getID() + ", idMajor: " + student.getIdMajor());
            System.out.println("Danh sách majors: " + (majors != null ? majors.size() : "null"));

            for (Major major : majors) {
                if (major != null && major.getIDMajor() != null) {
                    // Debug: Kiểm tra từng major
                    System.out.println("Major ID: " + major.getIDMajor() + ", Name: " + major.getNameMajor());
                    if (major.getIDMajor().equals(student.getIdMajor())) {
                        majorName = major.getNameMajor() != null ? major.getNameMajor() : "Không xác định";
                        break;
                    }
                }
            }
        } else {
            // Debug: Kiểm tra tại sao không vào được vòng lặp
            System.out.println("Không thể tìm major - idMajor: " + student.getIdMajor() + ", majors: " + (majors != null ? majors.size() : "null"));
        }
        tvMajor.setText("Ngành Học: " + majorName);

        btnShowOnMap.setOnClickListener(v -> {
            if (student.getAddress() != null) {
                mapClickListener.onMapClick(student.getAddress());
            }
        });
        btnEdit.setOnClickListener(v -> editClickListener.onEditClick(student));
        btnDelete.setOnClickListener(v -> {
            if (student.getID() != null) {
                deleteClickListener.onDeleteClick(student.getID());
            }
        });

        return convertView;
    }
}