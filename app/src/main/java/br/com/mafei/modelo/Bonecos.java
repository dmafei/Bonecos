package br.com.mafei.modelo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Bonecos implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int codigo;
    private String nomeBoneco;
    private String nomeMarca;
    private String nomeModelo;
    private String nomeFilme;


    public Bonecos (){

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeMarca() {
        return nomeMarca;
    }

    public void setNomeMarca(String nomeMarca) {
        this.nomeMarca = nomeMarca;
    }

    public String getNomeBoneco() {
        return nomeBoneco;
    }

    public void setNomeBoneco(String nomeBoneco) {
        this.nomeBoneco = nomeBoneco;
    }

    @NonNull
    @Override
    public String toString() {
        return codigo + " - " + nomeBoneco + " - "+ nomeMarca;
    }

    public String getNomeModelo() {
        return nomeModelo;
    }

    public void setNomeModelo(String nomeModelo) {
        this.nomeModelo = nomeModelo;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public void setNomeFilme(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }
}
