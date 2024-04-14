package com.example.agendacontactos_gnsm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText txtNombres, txtEmail, txtTelefono;
    private RecyclerView recyclerView;
    private ContactosAdapter adapter;
    private List<Contacto> contactosList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewContactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        txtNombres = findViewById(R.id.txtNombres);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefono = findViewById(R.id.txtTelefono);

        contactosList = cargarContactos();
        adapter = new ContactosAdapter(contactosList);
        recyclerView.setAdapter(adapter);
    }
    public void btnGuardar_onClick(View v) {

        String nombres = txtNombres.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingresa un correo electrónico válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Contacto> contactos = cargarContactos();

        boolean encontrado = false;
        for (int i = 0; i < contactos.size(); i++) {
            if (contactos.get(i).getNombres().equalsIgnoreCase(nombres)) {
                encontrado = true;
                if (!contactos.get(i).getEmail().equals(email) || !contactos.get(i).getTelefono().equals(telefono)) {
                    contactos.get(i).setEmail(email);
                    contactos.get(i).setTelefono(telefono);
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "El contacto ya existe", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
        if (!encontrado) {
            contactos.add(new Contacto(nombres, email, telefono));
            Toast.makeText(this, "Contacto guardado", Toast.LENGTH_SHORT).show();
        }

        guardarContactos(contactos);
        actualizarListaContactos();

    }

    private void guardarContactos(List<Contacto> contactos) {
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("contactos_size", contactos.size());

        for (int i = 0; i < contactos.size(); i++) {
            editor.putString("contacto_" + i + "_nombres", contactos.get(i).getNombres());
            editor.putString("contacto_" + i + "_email", contactos.get(i).getEmail());
            editor.putString("contacto_" + i + "_telefono", contactos.get(i).getTelefono());
        }

        editor.apply();
    }

    public List<Contacto> cargarContactos() {
        SharedPreferences preferences = getSharedPreferences("datos", Context.MODE_PRIVATE);
        int size = preferences.getInt("contactos_size", 0);
        List<Contacto> contactos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String nombres = preferences.getString("contacto_" + i + "_nombres", "");
            String email = preferences.getString("contacto_" + i + "_email", "");
            String telefono = preferences.getString("contacto_" + i + "_telefono", "");
            contactos.add(new Contacto(nombres, email, telefono));
        }

        return contactos;
    }

    public void btnBuscar_onClick(View v){
        String nombreBuscado = txtNombres.getText().toString().trim();
        List<Contacto> contactos = cargarContactos();

        boolean encontrado = false;
        for (Contacto contacto : contactos) {
            if (contacto.getNombres().equalsIgnoreCase(nombreBuscado)) {
                txtEmail.setText(contacto.getEmail());
                txtTelefono.setText(contacto.getTelefono());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            Toast.makeText(this, "Contacto no encontrado", Toast.LENGTH_SHORT).show();

            txtEmail.setText("");
            txtTelefono.setText("");
        }

        actualizarListaContactos();
    }
    private void actualizarListaContactos() {
        contactosList.clear();
        contactosList.addAll(cargarContactos());
        adapter.notifyDataSetChanged();
    }




}