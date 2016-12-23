package com.example.joaopaulo.domoticaapp.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Comodo implements Serializable {

    public static String TIPO_BANHEIRO = "Banheiro";
    public static String TIPO_COZINHA = "Cozinha";
    public static String TIPO_EXTERNO = "Externo";
    public static String TIPO_QUARTO = "Quarto";
    public static String TIPO_SALA = "Sala";

    private String nome;
    private String codigo;
    private String tipo;
    private int imagem;

    public Comodo(String nome, String codigo, String tipo, int imagem) {

        this.nome = nome;
        this.codigo = codigo;
        this.tipo = tipo;
        this.imagem = imagem;
    }

    public String getTipo() {
        return tipo;
    }

    public int getImagem() {
        return imagem;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String toString() {

        return "Código: " + this.codigo + "\r\n" +
               "Nome: " + this.nome + "\r\n" +
               "Tipo: " + this.tipo;
    }
}
