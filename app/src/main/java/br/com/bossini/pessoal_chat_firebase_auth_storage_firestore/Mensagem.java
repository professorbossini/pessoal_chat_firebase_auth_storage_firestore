package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import java.util.Date;

class Mensagem {

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
}
