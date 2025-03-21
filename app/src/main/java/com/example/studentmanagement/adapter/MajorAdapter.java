package com.example.studentmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.studentmanagement.R;
import com.example.studentmanagement.model.Major;

import java.util.List;

public class MajorAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Major> majorList;
    private MajorActionListener actionListener; // Thêm listener

    // Cập nhật constructor để nhận MajorActionListener
    public MajorAdapter(Context context, int layout, List<Major> majorList, MajorActionListener actionListener) {
        this.context = context;
        this.layout = layout;
        this.majorList = majorList;
        this.actionListener = actionListener;
    }

    @Override
    public int getCount() {
        return majorList.size();
    }

    @Override
    public Object getItem(int position) {
        return majorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView txtTenMajor;
        ImageView imgDelete, imgUpdate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTenMajor = convertView.findViewById(R.id.textViewTenMajor);
            holder.imgDelete = convertView.findViewById(R.id.imageViewDeleteMajor);
            holder.imgUpdate = convertView.findViewById(R.id.imageViewEditMajor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Major major = majorList.get(position);
        holder.txtTenMajor.setText(major.getNameMajor());

        // Sử dụng listener thay vì ép kiểu context
        holder.imgDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteMajor(major.getNameMajor(), major.getIDMajor());
            }
        });

        holder.imgUpdate.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEditMajor(major.getIDMajor(), major.getNameMajor());
            }
        });

        return convertView;
    }
}