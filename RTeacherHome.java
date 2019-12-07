package com.aamrtu.aicte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RTeacherHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rteacher_home);
    }
    public void signOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(RTeacherHome.this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RTeacherHome.this, TeachersLogin.class);
        startActivity(intent);
    }
}
