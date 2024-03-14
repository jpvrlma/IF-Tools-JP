package com.example.if_tools;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.example.if_tools.databinding.ActivityEditaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditaActivity extends AppCompatActivity {

    private ActivityEditaBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        carregarDados();
        binding.btnAlterar.setOnClickListener(v -> validaDados());
    }

    private void carregarDados() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            mDatabase.child("usuarios").child(userId).get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(this, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show();
                }
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    String nome = dataSnapshot.child("nome").getValue(String.class);
                    String telefone = dataSnapshot.child("telefone").getValue(String.class);
                    String senha = dataSnapshot.child("senha").getValue(String.class);

                    binding.editNome.setText(nome);
                    binding.editTelefone.setText(telefone);
                    binding.editSenha.setText(senha);
                }
            });
        }
    }

    private void validaDados() {
        String novonome = binding.editNome.getText().toString().trim();
        String novotelefone = binding.editTelefone.getText().toString().trim();
        String novosenha = binding.editSenha.getText().toString().trim();

        if (novonome.isEmpty()) {
            binding.editNome.setError("Informe um Nome");
            return;
        }
        if (novotelefone.isEmpty()) {
            binding.editTelefone.setError("Informe um telefone");
            return;
        }
        if (novosenha.isEmpty()) {
            binding.editSenha.setError("Informe uma senha");
        }
        alterarDados(novonome, novotelefone,  novosenha);
    }

    private void alterarDados(String novoNome, String novoTelefone, String novoSenha) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            DatabaseReference userReference = mDatabase.child("usuarios").child(userId);

            Map<String, Object> updates = new HashMap<>();
            updates.put("nome", novoNome);
            updates.put("telefone", novoTelefone);
            updates.put("senha", novoSenha);
            updates.put("nivel", 1);

            userReference.updateChildren(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            alterarSenha();
                            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Erro ao atualizar", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void alterarSenha () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = binding.editSenha.getText().toString().trim();
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "Senha deu erro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}