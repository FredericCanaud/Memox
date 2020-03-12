package com.example.memox;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
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

    }
}
