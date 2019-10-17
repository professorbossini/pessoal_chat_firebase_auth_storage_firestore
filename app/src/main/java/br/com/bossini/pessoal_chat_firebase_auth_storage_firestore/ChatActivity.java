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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.Nullable;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mensagensRecyclerView;
    private ChatAdapter adapter;
    private List<Mensagem> mensagens;
    private EditText mensagemEditText;
    private FirebaseUser fireUser;
    private CollectionReference mMsgsReference;

    private void getRemoteMsgs (){
        mMsgsReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mensagens.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    Mensagem incomingMsg = doc.toObject(Mensagem.class);
                        mensagens.add(incomingMsg);
                }
                Collections.sort(mensagens);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupFirebase (){
        fireUser = FirebaseAuth.getInstance().getCurrentUser();
        mMsgsReference = FirebaseFirestore.getInstance().collection("mensagens");
        getRemoteMsgs();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer);
        mensagensRecyclerView = findViewById(R.id.mensagensRecyclerView);
        mensagens = new ArrayList<>();
        adapter = new ChatAdapter(mensagens, this);
        mensagensRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mensagensRecyclerView.setLayoutManager(linearLayoutManager);
        mensagemEditText = findViewById(R.id.mensagemEditText);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupFirebase();

    }

    public void enviarMensagem (View view){
        String mensagem = mensagemEditText.getText().toString();
        Mensagem m = new Mensagem (fireUser.getEmail(), new Date(), mensagem);
        esconderTeclado(view);
        mMsgsReference.add(m);
        mensagemEditText.setText("");
    }

    private void esconderTeclado (View v){
        InputMethodManager ims = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        ims.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePicImageView;
        TextView dataNomeTextView;
        TextView mensagemTextView;
        ChatViewHolder (View v){
            super (v);
            this.dataNomeTextView = v.findViewById(R.id.dataNomeTextView);
            this.mensagemTextView = v.findViewById(R.id.mensagemTextView);
            this.profilePicImageView = v.findViewById(R.id.profilePicImageView);
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
            StorageReference profilePicReference = FirebaseStorage.getInstance().getReference(
                    String.format(
                            Locale.getDefault(),
                            "images/%s/profilePic.jpg",
                            m.getUsuario().replace("@", "")
                    )
            );
            //verifica se a pessoa tem foto
            profilePicReference.getDownloadUrl().addOnSuccessListener((result) -> {
                Glide.with(context).load(profilePicReference).into(holder.profilePicImageView);
            }).addOnFailureListener((failure) ->{
                holder.profilePicImageView.setImageResource(R.drawable.ic_person_black_50dp);
            });

        }

        @Override
        public int getItemCount() {
            return mensagens.size();
        }
    }
}
