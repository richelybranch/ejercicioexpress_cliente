package com.example.almeidaexamen1chds;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    private EditText input1, input2, input3;
    private LinearLayout inputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        inputLayout = findViewById(R.id.inputLayout);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        findViewById(R.id.btnRichely).setOnClickListener(v -> callService("http://192.168.0.103:3000/richely"));
        findViewById(R.id.btnNombre).setOnClickListener(v -> callService("http://192.168.0.103:3000/nombre"));
        findViewById(R.id.btnSuma).setOnClickListener(v -> callService("http://192.168.0.103:3000/suma"));

        findViewById(R.id.btnSumaParam).setOnClickListener(v -> {
            setupInputFields(1);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String param = input1.getText().toString();
                callService("http://192.168.0.103:3000/suma/" + param);
            });
        });

        findViewById(R.id.btnCuadrado).setOnClickListener(v -> {
            setupInputFields(1);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String lado = input1.getText().toString();
                callService("http://192.168.0.103:3000/cuadrado/" + lado);
            });
        });

        findViewById(R.id.btnRectangulo).setOnClickListener(v -> {
            setupInputFields(2);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String base = input1.getText().toString();
                String altura = input2.getText().toString();
                callService("http://192.168.0.103:3000/rectangulo/" + base + "/" + altura);
            });
        });

        findViewById(R.id.btnRombo).setOnClickListener(v -> {
            setupInputFields(3);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String diagMayor = input1.getText().toString();
                String diagMenor = input2.getText().toString();
                String lado = input3.getText().toString();
                callService("http://192.168.0.103:3000/rombo/" + diagMayor + "/" + diagMenor + "/" + lado);
            });
        });
    }

    private void setupInputFields(int numFields) {
        inputLayout.setVisibility(View.VISIBLE);
        input1.setVisibility(numFields >= 1 ? View.VISIBLE : View.GONE);
        input2.setVisibility(numFields >= 2 ? View.VISIBLE : View.GONE);
        input3.setVisibility(numFields == 3 ? View.VISIBLE : View.GONE);
    }

    private void callService(String urlString) {
        new Thread(() -> {
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();

                runOnUiThread(() -> {
                    inputLayout.setVisibility(View.GONE);
                    resultTextView.setText(result.toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
