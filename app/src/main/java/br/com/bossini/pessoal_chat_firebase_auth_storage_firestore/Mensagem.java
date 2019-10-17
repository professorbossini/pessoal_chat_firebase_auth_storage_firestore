package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import androidx.annotation.Nullable;

import java.util.Date;
import java.util.Objects;

class Mensagem implements Comparable <Mensagem>{

    private String usuario;
    private Date data;
    private String texto;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Mensagem(String usuario, Date data, String texto) {
        this.usuario = usuario;
        this.data = data;
        this.texto = texto;
    }

    public Mensagem (){}

    @Override
    public boolean equals(@Nullable Object obj) {
        Mensagem outra = (Mensagem)obj;
        return this.usuario.equals(outra.usuario) && this.data.equals(outra.data) && this.texto.equals(outra.texto);
    }

    @Override
    public int compareTo(Mensagem mensagem) {
        return this.data.compareTo(mensagem.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, data, texto);
    }
}
