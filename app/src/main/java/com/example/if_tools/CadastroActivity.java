package com.example.if_tools;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.if_tools.databinding.ActivityCadastroBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroActivity extends AppCompatActivity {

    private ActivityCadastroBinding binding;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        binding.btnCadastro.setOnClickListener(v -> validaDados());
        binding.textRecuperaConta.setOnClickListener(v -> {
            startActivity(new Intent(this, RecuperaActivity.class));
        });
        binding.textLogue.setOnClickListener( v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void validaDados() {
        String nome = binding.editNome.getText().toString().trim();
        String telefone = binding.editTelefone.getText().toString().trim();
        String email = binding.editEmail.getText().toString().trim();
        String senha = binding.editSenha.getText().toString().trim();

        if (nome.isEmpty()) {
            binding.editNome.setError("Informe um Nome");
            return;
        }
        if (telefone.isEmpty()) {
            binding.editTelefone.setError("Informe um telefone");
            return;
        }
        if (email.isEmpty()) {
            binding.editEmail.setError("Informe um email");
            return;
        }
        if (senha.isEmpty()) {
            binding.editSenha.setError("Informe uma senha");
        }
        cadastroFirebase(nome, telefone, email, senha,1);
    }

    public void cadastroFirebase(String nome, String telefone, String email, String senha, int nivel) {
        Usuario user = new Usuario(nome, telefone, email, senha, nivel);
        binding.progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            mDatabase.child("usuarios").child(userId).setValue(user);
                            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar usuário.", Toast.LENGTH_SHORT).show();
                    }
                    binding.progressBar.setVisibility(View.GONE);
                });
    }
}