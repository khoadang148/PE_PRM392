package com.example.studentmanagement.adapter;

public interface MajorActionListener {
    void onDeleteMajor(String nameMajor, String idMajor);
    void onEditMajor(String idMajor, String nameMajor);
}