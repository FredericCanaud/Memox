package com.example.memox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CreaMemox extends Activity {

    Spinner dropdown;
    String[] selectItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memox_crea);

        //Création du select

        dropdown = findViewById(R.id.select);

        selectItems = new String[]{"Coronavirus", "Anglais", "Beber"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreaMemox.this, android.R.layout.simple_spinner_dropdown_item, selectItems);
        //set the spinners adapter to the previously created one.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);

        final Button back = findViewById((R.id.back));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreaMemox.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
