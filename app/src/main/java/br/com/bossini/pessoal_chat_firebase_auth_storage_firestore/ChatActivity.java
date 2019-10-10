package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mensagensRecyclerView;
    private ChatAdapter adapter;
    private List <Mensagem> mensagens;

    private EditText mensagemEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mensagensRecyclerView = findViewById(R.id.mensagensRecyclerView);
        mensagens = new ArrayList<>();
        adapter = new ChatAdapter(mensagens, this);
        mensagensRecyclerView.setAdapter(adapter);
        mensagensRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mensagemEditText = findViewById(R.id.mensagemEditText);
    }

    public void enviarMensagem (View view){

        String mensagem = mensagemEditText.getText().toString();
        Mensagem m = new Mensagem ("dono", new Date(), mensagem);
        mensagens.add(m);
        adapter.notifyDataSetChanged();
        esconderTeclado(view);

    }

    private void esconderTeclado (View v){
        InputMethodManager ims = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ims.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        TextView dataNomeTextView;
        TextView mensagemTextView;
        ChatViewHolder (View v){
            super (v);
            this.dataNomeTextView = v.findViewById(R.id.dataNomeTextView);
            this.mensagemTextView = v.findViewById(R.id.mensagemTextView);
        }

    }

    class ChatAdapter extends RecyclerView.Adapter <ChatViewHolder>{

        private List<Mensagem> mensagens;
        private Context context;

        ChatAdapter (List<Mensagem> mensagens, Context context){
            this.mensagens = mensagens;
            this.context = context;
        }

        @NonNull
        @Override
        public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(R.layout.list_item, parent, false);
            return new ChatViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
            Mensagem m = mensagens.get(position);
            holder.dataNomeTextView.setText(context.getString(R.string.data_nome, DateHelper.format(m.getData()), m.getUsuario()));
            holder.mensagemTextView.setText(m.getTexto());
            mensagemEditText.setText("");
            mensagemEditText.clearFocus();
        }

        @Override
        public int getItemCount() {
            return mensagens.size();
        }
    }
}
