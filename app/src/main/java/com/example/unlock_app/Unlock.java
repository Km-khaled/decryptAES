package com.example.unlock_app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Unlock extends AppCompatActivity {
    private EditText hashedStringInput;
    private Button getOriginalStringButton ;

    private ImageButton copyText ;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        hashedStringInput = findViewById(R.id.Unlock_code);
        getOriginalStringButton = findViewById(R.id.Decrypt);
        tv = findViewById(R.id.code);
        copyText = findViewById(R.id.copy_code);

        getOriginalStringButton.setOnClickListener(v -> {
            if (!hashedStringInput.getText().toString().isEmpty()) {
                String originalString = decryptAES(hashedStringInput.getText().toString(), "?#ijd?c$9Hyxne6@");
                tv.setText(originalString);
            }
        });

        copyText.setOnClickListener(v -> {
            // Copy the text from the TextView to the clipboard
            copyTextToClipboard(tv.getText().toString());
            showToastMessage("Code copié");
        });
    }

    private void copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Device Code", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    private String decryptAES(String encryptedText, String encryptionKey) {
        try {
            byte[] keyBytes = encryptionKey.getBytes("UTF-8");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedBytes = android.util.Base64.decode(encryptedText, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
