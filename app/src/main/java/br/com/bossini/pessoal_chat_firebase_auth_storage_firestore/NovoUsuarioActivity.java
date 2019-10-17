package br.com.bossini.pessoal_chat_firebase_auth_storage_firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class NovoUsuarioActivity extends AppCompatActivity {

    private EditText loginNovoUsuarioEditText;
    private EditText senhaNovoUsuarioEditText;
    private FirebaseAuth mAuth;
    //membro da classe
    private static final int REQ_CODE_CAMERA = 1001;
    //variável de instância
    private ImageView pictureImageView;
    private StorageReference pictureStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_usuario);
        loginNovoUsuarioEditText = findViewById(R.id.loginNovoUsuarioEditText);
        senhaNovoUsuarioEditText = findViewById(R.id.senhaNovoUsuarioEditText);
        //no método onCreate
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
        if (loginNovoUsuarioEditText.getText() != null
                && !loginNovoUsuarioEditText.getText().toString().isEmpty()){
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent, REQ_CODE_CAMERA);
            }else{
                Toast.makeText(
                        this,
                        getString(R.string.no_camera),//defina um texto apropriado
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
        else{
            Toast.makeText(
                    this,
                    getString(R.string.empty_email),
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQ_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK){
                    Bitmap picture = (Bitmap) data.getExtras().get ("data");
                    pictureImageView.setImageBitmap(picture);
                    uploadPicture(picture);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadPicture (Bitmap picture){
        pictureStorageReference = FirebaseStorage.getInstance().getReference(
                String.format(
                        Locale.getDefault(),
                        "images/%s/profilePic.jpg",
                        loginNovoUsuarioEditText.getText().toString().replace("@", "")
                )
        );
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        pictureStorageReference.putBytes(bytes);
    }
}
