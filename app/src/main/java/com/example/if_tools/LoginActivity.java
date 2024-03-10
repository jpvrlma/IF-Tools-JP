package com.example.if_tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.if_tools.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        IniciaToolbar();
        binding.btnLogin.setOnClickListener(v -> validadados());
    }

    private void validadados(){
        String email = binding.editEmail.getText().toString().trim();
        String senha = binding.editSenha.getText().toString().trim();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                binding.progressBar3.setVisibility(View.VISIBLE);

                LogarFirebase(email, senha);
            } else{
                Toast.makeText(this,"Informe uma senha.",Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this,"Informe um e-mail.",Toast.LENGTH_SHORT).show();
        }
    }
    private void LogarFirebase(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                binding.progressBar3.setVisibility(View.GONE);
                Toast.makeText(this, "Falha na criação da conta: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void IniciaToolbar(){
        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
}