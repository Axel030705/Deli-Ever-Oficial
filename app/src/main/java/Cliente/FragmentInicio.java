package Cliente;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agenda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Perfil.Perfil_Activity;
import Vendedor.Tiendas.TiendaAdapter;
import Vendedor.Tiendas.TiendaClase;
import Vendedor.Tiendas.Tiendas_Activity;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentInicio extends Fragment {

    private TiendaAdapter adapter;
    private final List<TiendaClase> tiendas = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference tiendaRef = database.getReference("Tienda");
    private TextView TXTNombreUsuario;
    CircleImageView ImagenUsuario;

    public FragmentInicio() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new TiendaAdapter(tiendas);
        recyclerView.setAdapter(adapter);

        TXTNombreUsuario = view.findViewById(R.id.TXTNombreUsuario);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userId = currentUser.getUid();

        ImagenUsuario = view.findViewById(R.id.ImagenUsuario);

        tiendaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tiendas.clear(); // Limpiar la lista antes de agregar nuevos datos

                // Recorrer los nodos hijos de dataSnapshot para obtener los datos de cada tienda
                for (DataSnapshot tiendaSnapshot : dataSnapshot.getChildren()) {
                    // Obtener los valores de cada atributo de la tienda
                    String nombre = tiendaSnapshot.child("nombre").getValue(String.class);
                    String descripcion = tiendaSnapshot.child("descripcion").getValue(String.class);
                    String direccion = tiendaSnapshot.child("direccion").getValue(String.class);
                    String extra = tiendaSnapshot.child("extra").getValue(String.class);
                    String usuarioAsociado = tiendaSnapshot.child("usuarioAsociado").getValue(String.class);

                    // Obtener la URL de descarga de la imagen
                    String imageUrl = tiendaSnapshot.child("imageUrl").getValue(String.class);

                    // Crear una instancia de TiendaClase con los datos obtenidos
                    String tiendaId = tiendaSnapshot.child("id").getValue(String.class);
                    TiendaClase tienda = new TiendaClase(tiendaId, nombre, descripcion, direccion, extra, usuarioAsociado, imageUrl);

                    // Agregar la tienda a la lista
                    tiendas.add(tienda);
                }

                // Notificar al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que ocurra
            }
        });

        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(userId);
        usuariosRef.child("nombre").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombreUsuario = dataSnapshot.getValue(String.class);
                    TXTNombreUsuario.setText("Bienvenido(a): " + " " + nombreUsuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error en caso de que ocurra
            }
        });

        ImagenUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), Perfil_Activity.class);
            startActivity(intent);
        });

        usuariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Recupera la URL de la imagen almacenada en Firebase Database
                String imageUrl = dataSnapshot.child("imagenPerfil").child("url").getValue(String.class);
                if (imageUrl != null) {
                    Picasso.get().load(imageUrl).into(ImagenUsuario);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Maneja cualquier error en la lectura de datos
            }
        });

    }
}