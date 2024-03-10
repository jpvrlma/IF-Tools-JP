package com.example.if_tools;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.if_tools.databinding.ActivityRecuperaBinding;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperaActivity extends AppCompatActivity {

    private ActivityRecuperaBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecuperaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        IniciaToolbar();
        binding.btnRecupera.setOnClickListener(v -> validadados());
    }
    private void validadados(){
        String email = binding.editEmail.getText().toString().trim();

        if (!email.isEmpty()) {
            binding.progressBar2.setVisibility(View.VISIBLE);
            recuperaContaFirebase(email);
        } else {
            Toast.makeText(this,"Informe um e-mail.",Toast.LENGTH_SHORT).show();
        }
    }
    private void recuperaContaFirebase(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this,"Verifique sua caixa de entrada",Toast.LENGTH_SHORT).show();
                binding.progressBar2.setVisibility(View.GONE);
            } else {
                binding.progressBar2.setVisibility(View.GONE);
                Toast.makeText(this,"P.A.U",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void IniciaToolbar(){
        Toolbar toolbar = binding.toolbar;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }
}