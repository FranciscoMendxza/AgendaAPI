package mx.edu.isc.tesoem.mojf.p3p7;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

import Concentrado.Peticiones;
import DTO.DatosDTO;
import kotlinx.coroutines.Delay;

public class Principal extends AppCompatActivity {

    GridView gvdatos;
    List<DatosDTO> ldatos = new ArrayList<>();
    List<String> sdatos = new ArrayList<>();
    EditText txtid, txtnombre, txtedad, txtcorreo;
    TextView tvid;
    Button registrar, actualizar, eliminar, cancela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtedad = findViewById(R.id.txtedad);
        txtcorreo = findViewById(R.id.txtcorreo);
        registrar = findViewById(R.id.btnregistra);
        actualizar = findViewById(R.id.btnactualiza);
        eliminar = findViewById(R.id.btnelimina);
        tvid = findViewById(R.id.tvid);
        cancela = findViewById(R.id.btncancela);

        gvdatos = findViewById(R.id.gvDatos);
        Peticiones peticionweb = new Peticiones(this);

        peticionweb.regilla(gvdatos);
        limpiarDatosGridView();
        peticionweb.CargarDatos();
        ldatos = peticionweb.getLdatos();
        sdatos = peticionweb.getSdatos();

        eliminar.setEnabled(false);
        actualizar.setEnabled(false);

        tvid.setVisibility(View.INVISIBLE);
        txtid.setVisibility(View.INVISIBLE);

        //peticionweb.cargarRegistroact("55");
        //peticionweb.RegistraNuevo("Prueba6","6","prueba6");
        //peticionweb.ActualizaRegistro("56","pru 6", "66", "pru6@prue6.com");
        //peticionweb.EliminarRegistro("17");

        cancela.setEnabled(false);

        cancela.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvid.setVisibility(View.INVISIBLE);
                txtid.setVisibility(View.INVISIBLE);

                actualizar.setEnabled(false);
                eliminar.setEnabled(false);
                cancela.setEnabled(false);

                txtnombre.setText("");
                txtedad.setText("");
                txtcorreo.setText("");
            }
        });

        gvdatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int celda, long l) {
                if ((celda>1) && ((celda % 2)==0)){
                    for (DatosDTO dato:ldatos) {
                        if (dato.getId() == Integer.parseInt(sdatos.get(celda))){
                            txtid.setText(String.valueOf(dato.getId()));
                            txtnombre.setText(dato.getNombre());
                            txtedad.setText(String.valueOf(dato.getEdad()));
                            txtcorreo.setText(dato.getCorreo());

                            actualizar.setEnabled(true);
                            eliminar.setEnabled(true);

                            tvid.setVisibility(View.VISIBLE);
                            txtid.setVisibility(View.VISIBLE);
                            cancela.setEnabled(true);
                        }
                    }
                }else{
                    tvid.setVisibility(View.INVISIBLE);
                    txtid.setVisibility(View.INVISIBLE);
                }

                limpiarDatosGridView();
                peticionweb.CargarDatos();
            }
        });

        registrar.setOnClickListener(v -> {
            String nombre = txtnombre.getText().toString();
            String edadStr = txtedad.getText().toString();
            String correo = txtcorreo.getText().toString();

            if (!nombre.isEmpty() && !edadStr.isEmpty() && !correo.isEmpty()) {
                try {
                    int edad = Integer.parseInt(edadStr);

                    txtnombre.setText(nombre);
                    txtedad.setText(String.valueOf(edad));
                    txtcorreo.setText(correo);

                    peticionweb.RegistraNuevo(nombre, String.valueOf(edad), correo);
                    Toast.makeText(this, "Se insertaron los datos correctamente", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    Toast.makeText(Principal.this, "La edad debe ser un valor entero válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Principal.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            }

            limpiarDatosGridView();
            peticionweb.CargarDatos();

            txtnombre.setText("");
            txtcorreo.setText("");
            txtedad.setText("");

            eliminar.setEnabled(false);
            actualizar.setEnabled(false);
            cancela.setEnabled(false);
            tvid.setVisibility(View.INVISIBLE);
            txtid.setVisibility(View.INVISIBLE);
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar.setEnabled(false);
                actualizar.setEnabled(false);
                tvid.setVisibility(View.INVISIBLE);
                txtid.setVisibility(View.INVISIBLE);

                String id = txtid.getText().toString();
                String nombre = txtnombre.getText().toString();
                String edadStr = txtedad.getText().toString();
                String correo = txtcorreo.getText().toString();

                if (!id.isEmpty() && !nombre.isEmpty() && !edadStr.isEmpty() && !correo.isEmpty()) {
                    try {
                        int edad = Integer.parseInt(edadStr);

                        txtid.setText(id);
                        txtnombre.setText(nombre);
                        txtedad.setText(String.valueOf(edad));
                        txtcorreo.setText(correo);

                        peticionweb.ActualizaRegistro(id, nombre, String.valueOf(edad), correo);

                        Toast.makeText(Principal.this, "Se actualizaron los datos", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(Principal.this, "La edad debe ser un valor entero válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Principal.this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                }
                txtnombre.setText("");
                txtcorreo.setText("");
                txtedad.setText("");
                limpiarDatosGridView();
                peticionweb.CargarDatos();

                eliminar.setEnabled(false);
                actualizar.setEnabled(false);
                cancela.setEnabled(false);
                tvid.setVisibility(View.INVISIBLE);
                txtid.setVisibility(View.INVISIBLE);
            }
        });

        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String idd = txtid.getText().toString();
                if (!idd.isEmpty()){
                    try {
                        int id = Integer.parseInt(idd);
                        peticionweb.EliminarRegistro(String.valueOf(id));
                        Toast.makeText(Principal.this, "Se elimino el registro", Toast.LENGTH_SHORT).show();
                    }catch (NumberFormatException e){
                        Toast.makeText(Principal.this, "Hay algo mal", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Principal.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }

                txtnombre.setText("");
                txtcorreo.setText("");
                txtedad.setText("");
                limpiarDatosGridView();
                peticionweb.CargarDatos();

                eliminar.setEnabled(false);
                actualizar.setEnabled(false);
                cancela.setEnabled(false);
                tvid.setVisibility(View.INVISIBLE);
                txtid.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void limpiarDatosGridView() {
        sdatos.clear();
        ArrayAdapter<String> adaptador = (ArrayAdapter<String>) gvdatos.getAdapter();
        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
    }


}