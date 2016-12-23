package com.example.joaopaulo.domoticaapp.data;

import java.io.Serializable;

public class Dispositivo implements Serializable {

    public static String TIPO_INTERRUPTOR = "Interruptor";
    public static String TIPO_SENSOR = "Sensor";
    public static String TIPO_INTERRUPTOR_VARIAVEL = "Interruptor variável";

    private String codigo;
    private String nome;
    private String tipo;
    private String comando;
    private String comandoOn;
    private String comandoOff;
    private String comandoVar;

    public Dispositivo(String codigo, String nome, String tipo) {

        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public String getComando() {
        return comando;
    }
    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getComandoOn() {
        return comandoOn;
    }
    public void setComandoOn(String comandoOn) {
        this.comandoOn = comandoOn;
    }

    public String getComandoOff() {
        return comandoOff;
    }
    public void setComandoOff(String comandoOff) {
        this.comandoOff = comandoOff;
    }

    public String getComandoVar() {
        return comandoVar;
    }
    public void setComandoVar(String comandoVar) {
        this.comandoVar = comandoVar;
    }

    public String getComandos() {

        return "Comandos: \r\n" +
                "   Geral: " + this.comando + "\r\n" +
                "   On: " + this.comandoOn + "\r\n" +
                "   Off: " + this.comandoOff + "\r\n" +
                "   Variação: " + this.comandoVar + "\r\n";
    }

    public String toString() {

        return "Código: " + this.codigo + "\r\n" +
                "Nome: " + this.nome + "\r\n" +
                "Tipo: " + this.tipo + "\r\n" +
                getComandos();
    }
}
