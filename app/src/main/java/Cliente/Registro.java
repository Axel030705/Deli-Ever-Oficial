package Cliente;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.agenda.R;
import Perfil.Ubicacion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import Vendedor.MainActivityEspera;
import Vendedor.SolicitudesVendedores;
import Vendedor.Tiendas.Tiendas_Activity;

public class Registro extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private EditText txt_Nombre, txt_Correo, txt_Password, txt_ConfirmarPassword, txt_vendera;
    private Button Btn_RegistrarUsuario;
    private TextView TengoCuentaTXT;
    private AutoCompleteTextView spinner;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private String nombre = "", correo = "", password = "", confirmarpassword = "", tipoUsuario = "", txt_vendera2 = "";
    private double latitud, longitud; // Variables para almacenar la ubicación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Inicializar los componentes de la UI
        spinner = findViewById(R.id.spinner);
        String[] OpcionesUsuario = {"Cliente", "Vendedor"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, OpcionesUsuario);
        spinner.setAdapter(adapter);

        txt_Nombre = findViewById(R.id.txt_Nombres);
        txt_Correo = findViewById(R.id.txt_Correo);
        txt_Password = findViewById(R.id.txt_Password);
        txt_ConfirmarPassword = findViewById(R.id.txt_ConfirmarPassword);
        Btn_RegistrarUsuario = findViewById(R.id.Btn_RegistrarUsuario);
        TengoCuentaTXT = findViewById(R.id.TengoCuentaTXT);
        txt_vendera = findViewById(R.id.txt_vendera);
        txt_vendera.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String tipoSeleccionado = (String) spinner.getAdapter().getItem(i);
                txt_vendera.setVisibility("Vendedor".equals(tipoSeleccionado) ? View.VISIBLE : View.GONE);
            }
        });

        Btn_RegistrarUsuario.setOnClickListener(view -> obtenerUbicacionYValidarDatos());

        TengoCuentaTXT.setOnClickListener(view -> startActivity(new Intent(Registro.this, Login.class)));

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    latitud = locationResult.getLastLocation().getLatitude();
                    longitud = locationResult.getLastLocation().getLongitude();
                } else {
                    Toast.makeText(Registro.this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    }, REQUEST_LOCATION_PERMISSION);
                    return;
                }
            }
            obtenerUbicacionUsuario();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
                        !hasPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION, permissions, grantResults)) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    }, REQUEST_LOCATION_PERMISSION);
                } else {
                    obtenerUbicacionUsuario();
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean hasPermission(String permission, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(permission) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    private void obtenerUbicacionUsuario() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);

            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    private void obtenerUbicacionYValidarDatos() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_LOCATION_PERMISSION);

            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                latitud = location.getLatitude();
                longitud = location.getLongitude();
            }
            ValidarDatos();
        }).addOnFailureListener(e -> {
            Toast.makeText(Registro.this, "No se pudo obtener la ubicación.", Toast.LENGTH_SHORT).show();
            ValidarDatos();
        });
    }

    private void ValidarDatos() {
        nombre = txt_Nombre.getText().toString();
        correo = txt_Correo.getText().toString();
        password = txt_Password.getText().toString();
        confirmarpassword = txt_ConfirmarPassword.getText().toString();
        tipoUsuario = spinner.getText().toString().trim();
        txt_vendera2 = txt_vendera.getText().toString();

        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingrese su nombre", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese un correo válido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmarpassword)) {
            Toast.makeText(this, "Confirme su contraseña", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmarpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        } /*else if (TextUtils.isEmpty(tipoUsuario)) {
            Toast.makeText(this, "Seleccione su tipo de usuario", Toast.LENGTH_SHORT).show();
        }*/ else if ("Vendedor".equals(tipoUsuario) && TextUtils.isEmpty(txt_vendera2)) {
            Toast.makeText(this, "Ingrese el nombre de su tienda", Toast.LENGTH_SHORT).show();
        } else {
            RegistrarUsuario();
        }
    }

    private void RegistrarUsuario() {
        progressDialog.setMessage("Registrando usuario...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            GuardarInformacion(token);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Error al obtener token de FCM", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void GuardarInformacion(String token) {
        progressDialog.setMessage("Guardando su información");

        String uid = firebaseAuth.getUid();

        HashMap<String, Object> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("password", password);
        Datos.put("Tipo de usuario", tipoUsuario);
        Datos.put("Vendera", txt_vendera2);
        Datos.put("latitud", latitud); // Guardar latitud
        Datos.put("longitud", longitud); // Guardar longitud

        if (token != null) {
            Datos.put("tokenFCM", token);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid).setValue(Datos)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                    iniciarActividadSegunTipoUsuario(uid);
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(Registro.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void iniciarActividadSegunTipoUsuario(String uid) {
        if ("Cliente".equals(tipoUsuario)) {
            startActivity(new Intent(Registro.this, Tiendas_Activity.class));
        } else if ("Vendedor".equals(tipoUsuario)) {
            DatabaseReference vendedorReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(uid);
            vendedorReference.child("estado").setValue("pendiente");
            Intent intent = new Intent(this, MainActivityEspera.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } else if("Administrador".equals(password)){
            Intent intent = new Intent(this, SolicitudesVendedores.class);
            startActivity(intent);
        }else{
            Toast.makeText(Registro.this, "Tipo de usuario no válido", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
