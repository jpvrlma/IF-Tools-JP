package com.example.if_tools;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {

    public String nome;
    public String email;
    public String senha;
    public String telefone;
    public int nivel;

    public Usuario() {
    }

    public Usuario(String nome,String telefone, String email, String senha, int nivel) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
        this.nivel = nivel;
    }
}
