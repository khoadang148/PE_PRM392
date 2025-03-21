package com.example.studentmanagement;

import com.example.studentmanagement.model.Major;
import com.example.studentmanagement.model.Student;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @GET("Student")
    Call<List<Student>> getStudents();

    @GET("Major")
    Call<List<Major>> getMajors();

    @POST("Student")
    Call<Student> addStudent(@Body Student student);

    @POST("Major")
    Call<Major> addMajor(@Body Major major);

    @PUT("Major/{id}")
    Call<Major> updateMajor(@Path("id") String id, @Body Major major);

    @DELETE("Student/{id}")
    Call<Void> deleteStudent(@Path("id") String id);

    @DELETE("Major/{id}")
    Call<Void> deleteMajor(@Path("id") String id);

    @PUT("Student/{id}")
    Call<Student> updateStudent(@Path("id") String id, @Body Student student);
}