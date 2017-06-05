package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dmax.dialog.SpotsDialog;

public class ActivityRegistrarUsuario extends AppCompatActivity {
    static int n=5;
    static boolean control=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        final AlertDialog dialogo;
        int result=0;


        final EditText etNombre,etContra,etMail,etTele;
        Button btn_registrar;
        etNombre =(EditText)findViewById(R.id.et_reg_usuario);
        etContra =(EditText)findViewById(R.id.et_reg_pass);
        etMail=(EditText)findViewById(R.id.et_reg_email);

        dialogo  = new SpotsDialog(this,"Insertando cliente...");


        etTele=(EditText)findViewById(R.id.et_reg_phone);
        btn_registrar=(Button)findViewById(R.id.btn_registrar);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre=etNombre.getText().toString();
                String contra = etContra.getText().toString();
                String mail = etMail.getText().toString();
                String tel = etTele.getText().toString();



                if(control){
                    dialogo.show();
                    String insert = "INSERT INTO usuarios (username, pass, telefono, email, categoria) "
                            + "VALUES ('"+nombre + "', '" + contra + "', "
                            + "'" + tel + "', '" + mail + "', "
                            + 3 + ");";

                    new registrarUsuario(insert,dialogo).execute();
                    n++;

                }else{
                    Toast.makeText(getApplicationContext(), "El usuario introducido ya existe, inserte otro distinto", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

//--------------------------------------------------------------------------------------------------
    public class registrarUsuario extends AsyncTask<Object, Object, Integer> {

        String consultaDt;
        Connection conexDt;
        Statement sentenciaDt;
        android.app.AlertDialog dialog;

        public registrarUsuario(String consulta, android.app.AlertDialog dialog){
            this.consultaDt=consulta;
            this.dialog=dialog;
        }

        @Override
        protected Integer doInBackground(Object... params) {
            int result=0;
            try {
                conexDt = DriverManager.getConnection("jdbc:mysql://" + ActivityLogin.ip + "/base20171", "ubase20171", "pbase20171");
                sentenciaDt = conexDt.createStatement();
                publishProgress();

                if (consultaDt.startsWith("INSERT")) {
                    result=sentenciaDt.executeUpdate(consultaDt);
                }

            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(),"Usuario No se pudo insertar.", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer resultSet) {
            super.onPostExecute(resultSet);


            if(resultSet==1){
                showToast("Usuario Registrado con éxito.");

            }else{
                showToast("Usuario No se pudo registrar.");
            }

            try {
                conexDt.close();
                sentenciaDt.close();

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            dialog.dismiss();
        }
    }//Fin AsynTack

    private void showToast(String s) {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }
}
