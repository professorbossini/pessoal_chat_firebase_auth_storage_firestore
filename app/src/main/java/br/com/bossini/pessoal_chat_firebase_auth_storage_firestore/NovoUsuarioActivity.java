package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NovoUsuarioActivity extends AppCompatActivity {

    private EditText loginNovoUsuarioEditText;
    private EditText senhaNovoUsuarioEditText;
    private FirebaseAuth mAuth;
    private static final int REQ_CODE_CAMERA = 1001;
    private ImageView pictureImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);
        loginNovoUsuarioEditText = findViewById(R.id.loginNovoUsuarioEditText);
        senhaNovoUsuarioEditText = findViewById(R.id.senhaNovoUsuarioEditText);
        pictureImageView = findViewById(R.id.pictureImageView);
        mAuth = FirebaseAuth.getInstance();
    }

    public void criarNovoUsuario (View view){
        String login = loginNovoUsuarioEditText.getText().toString();
        String senha = senhaNovoUsuarioEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(login, senha).addOnSuccessListener((result) -> {
            Toast.makeText(this, result.getUser().toString(), Toast.LENGTH_SHORT).show();
            finish();
        })
        .addOnFailureListener(error -> error.printStackTrace());
    }

    public void tirarFoto (View v){
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQ_CODE_CAMERA);
        }else{
            Toast.makeText(
                    this,
                    getString(R.string.no_camera),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQ_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK){
                    Bitmap foto = (Bitmap) data.getExtras().get ("data");
                    Bitmap copy = foto.copy(foto.getConfig(), true);
                    copy.setHeight(pictureImageView.getHeight());
                    copy.setWidth(pictureImageView.getWidth());
                    pictureImageView.setImageBitmap(copy);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
