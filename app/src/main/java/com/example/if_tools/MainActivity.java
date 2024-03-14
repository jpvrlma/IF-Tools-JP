package com.example.if_tools;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.example.if_tools.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        carregarDados();
        binding.btnUpload.setOnClickListener(v -> pegarImagem());
        binding.btnEditar.setOnClickListener(v -> startActivity(new Intent(this, EditaActivity.class)));
    }

    private void pegarImagem() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userID = user.getUid();
                String nomeImagem = binding.editArquivo.getText().toString().trim();
                if (nomeImagem.isEmpty()) {
                    binding.editArquivo.setError("Informe um Nome para a foto");
                } else {
                    StorageReference userFolderRef = storageRef.child(userID);
                    StorageReference fileRef = userFolderRef.child(nomeImagem + "_" + System.currentTimeMillis() + ".jpg");
                    if (fileUri != null) {
                        fileRef.putFile(fileUri)
                                .addOnSuccessListener(taskSnapshot -> Toast.makeText(this, "Imagem enviada com sucesso", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(exception -> Toast.makeText(this, "Erro ao enviar a imagem", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Erro ao selecionar a imagem", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
                    binding.textNome.setText(nome);
                }
            });
        }
    }
}