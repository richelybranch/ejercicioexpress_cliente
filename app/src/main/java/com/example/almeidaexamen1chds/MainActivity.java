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

    private TextView titleTextView, resultTextView;
    private EditText inputA, inputB, inputC, input1, input2, input3;
    private LinearLayout inputLayout, inputTrinomioLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asignaciones de vistas
        titleTextView = findViewById(R.id.titleTextView);
        resultTextView = findViewById(R.id.resultTextView);
        inputA = findViewById(R.id.inputA);
        inputB = findViewById(R.id.inputB);
        inputC = findViewById(R.id.inputC);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        input3 = findViewById(R.id.input3);
        inputLayout = findViewById(R.id.inputLayout);
        inputTrinomioLayout = findViewById(R.id.inputTrinomioLayout);

        setupButtonListeners();
    }

    private void setupButtonListeners() {
        findViewById(R.id.btnRichely).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Richely");
            callService("http://192.168.0.103:3000/richely");
        });

        findViewById(R.id.btnNombre).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Nombre");
            callService("http://192.168.0.103:3000/nombre");
        });

        findViewById(R.id.btnSuma).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Suma Fija");
            callService("http://192.168.0.103:3000/suma");
        });

        findViewById(R.id.btnSumaParam).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Suma con Parámetro");
            setupInputFields(1);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String param = input1.getText().toString();
                if (!param.isEmpty()) {
                    callService("http://192.168.0.103:3000/suma/" + param);
                } else {
                    input1.setError("Campo requerido");
                }
            });
        });

        findViewById(R.id.btnTrinomioCuadrado).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Trinomio Cuadrado Perfecto");
            inputTrinomioLayout.setVisibility(View.VISIBLE);
            findViewById(R.id.btnCalcularTrinomio).setOnClickListener(view -> {
                String a = inputA.getText().toString();
                String b = inputB.getText().toString();
                String c = inputC.getText().toString();

                if (validateFields(a, b, c)) {
                    callService("http://192.168.0.103:3000/trinomiocuadrado/" + a + "/" + b + "/" + c);
                }
            });
        });

        findViewById(R.id.btnCuadrado).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Cuadrado");
            setupInputFields(1);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String lado = input1.getText().toString();
                if (!lado.isEmpty()) {
                    callService("http://192.168.0.103:3000/cuadrado/" + lado);
                } else {
                    input1.setError("Campo requerido");
                }
            });
        });

        findViewById(R.id.btnRectangulo).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Rectángulo");
            setupInputFields(2);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String base = input1.getText().toString();
                String altura = input2.getText().toString();

                if (validateFields(base, altura)) {
                    callService("http://192.168.0.103:3000/rectangulo/" + base + "/" + altura);
                }
            });
        });

        findViewById(R.id.btnRombo).setOnClickListener(v -> {
            resetFields();
            updateTitle("Función: Rombo");
            setupInputFields(3);
            findViewById(R.id.btnCalculate).setOnClickListener(view -> {
                String diagMayor = input1.getText().toString();
                String diagMenor = input2.getText().toString();
                String lado = input3.getText().toString();

                if (validateFields(diagMayor, diagMenor, lado)) {
                    callService("http://192.168.0.103:3000/rombo/" + diagMayor + "/" + diagMenor + "/" + lado);
                }
            });
        });
    }

    private void setupInputFields(int numFields) {
        inputLayout.setVisibility(View.VISIBLE);
        input1.setVisibility(numFields >= 1 ? View.VISIBLE : View.GONE);
        input2.setVisibility(numFields >= 2 ? View.VISIBLE : View.GONE);
        input3.setVisibility(numFields == 3 ? View.VISIBLE : View.GONE);
    }

    private boolean validateFields(String... fields) {
        boolean valid = true;
        for (String field : fields) {
            if (field.isEmpty()) {
                valid = false;
            }
        }
        if (!valid) {
            resultTextView.setText("Por favor complete todos los campos");
        }
        return valid;
    }

    private void resetFields() {
        inputLayout.setVisibility(View.GONE);
        inputTrinomioLayout.setVisibility(View.GONE);
        input1.setText("");
        input2.setText("");
        input3.setText("");
        inputA.setText("");
        inputB.setText("");
        inputC.setText("");
        resultTextView.setText("");
    }

    private void updateTitle(String newTitle) {
        titleTextView.setText(newTitle);
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

                runOnUiThread(() -> resultTextView.setText(result.toString()));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> resultTextView.setText("Error al conectar con el servicio"));
            }
        }).start();
    }
}
