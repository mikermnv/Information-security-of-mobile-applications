package com.example.lab3decryptor;

import static com.example.lab3decryptor.MyDecryptor.findAllEncryptedFiles;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        try {
            Start_Decryption();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void Start_Decryption() throws Exception{
        MyDecryptor decryptor = new MyDecryptor();
        File root = Environment.getExternalStorageDirectory();
        System.out.println(root);
        List<String> files = findAllEncryptedFiles(root);
        System.out.println(files);
        for (String inputFilename : files) {
            String outputFilename = inputFilename.substring(0, inputFilename.length() - 4);
            System.out.println("Decrypting " + outputFilename);
            decryptor.decrypt(inputFilename, outputFilename);
            (new File(inputFilename)).delete();
        }

        Toast.makeText(this, "Decryption completed", Toast.LENGTH_SHORT).show();
    }
}