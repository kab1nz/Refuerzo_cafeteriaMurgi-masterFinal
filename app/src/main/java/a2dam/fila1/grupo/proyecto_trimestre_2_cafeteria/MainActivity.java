package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    static boolean ini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ini = false;

        //No modificar ni cambiar la posicion del siguiente metodo
        //cargarDriverJDBC();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!ini){
            ini = true;
            Intent i = new Intent(getApplicationContext(),ActivityCafe.class);
            startActivity(i);
        }else
            finish();
    }

    private void cargarDriverJDBC(){//No modificar
        try
        {
            Class.forName("com.mysql.jdbc.Driver");///////////////////DOCUMENTAR LA NECESIDAD DE ESTA LINEA para el funcionamiento de BBDD
            Log.d("Carga Driver JDBC: ","Driver cargado correctamente");
        }
        catch(Exception ex)
        {
            Log.e("Carga Driver JDBC: ", "No se ha podido cargar el driver JDBC: " + ex.getMessage());
        }
    }//Fin cargarDriverJDBC No Modificar
}
