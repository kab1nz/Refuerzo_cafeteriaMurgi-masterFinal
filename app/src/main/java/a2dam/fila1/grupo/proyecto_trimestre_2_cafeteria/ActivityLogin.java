package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Producto;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Usuario;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Dialog.Dialog_pass;
import dmax.dialog.SpotsDialog;

public class ActivityLogin extends AppCompatActivity {

    AlertDialog dialogo;
    private boolean doubleBackToExitPressedOnce = false;

    public static Usuario USER;
    public static String ip = null;

    private EditText usuario, pass;
    private Button entrar,registro;
    private ImageButton ayuda;
    private RadioButton local, externa;

    static boolean loginCorrecto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if((this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        inflar();
        calcularIP();
        listener();
        registroLog();

        login();
    }//Fin onCreate

    @Override
    protected void onStart() {
        super.onStart();
        USER = null;
        loginCorrecto = false;
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Pulsa otra vez para salir", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }

    /**
     *
     * calcularIP, compruba los radioButton y establece una IP para la conexión con la BBDD
     */
    private void calcularIP() {
        if (local.isChecked())
            ip = "                static int n=5;\n";
        if (externa.isChecked())
            ip = "www.iesmurgi.org";
    }//Fin calcularIP

    /**
     * Métodos listener
     */
    private void listener() {

        ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_pass dialogo= new Dialog_pass();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogo.show(ft, "Contraseñas");
            }
        });
    }//Fin listener

    /**
     * Infla todos los elementos del layout del activity
     */
    private void inflar() {
        dialogo  = new SpotsDialog(this,"Comprobando usuario...");
        usuario  = (EditText) findViewById(R.id.et_lg_usuario);
        pass     = (EditText) findViewById(R.id.et_lg_pass);
        entrar   = (Button) findViewById(R.id.btn_lg_entrar);
        registro = (Button) findViewById(R.id.btn_registrologin);
        ayuda    = (ImageButton) findViewById(R.id.ibtn_lg_ayuda);
        local    = (RadioButton) findViewById(R.id.rb_lg_local);
        externa  = (RadioButton) findViewById(R.id.rb_lg_externa);
    }//Fin inflar

    /**
     * Método listener del botón entrar
     */
    private void login() {

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario.getText().toString().trim().equals("") || pass.getText().toString().trim().equals("")){
                    Toast.makeText(getApplicationContext(), "Debe rellenar todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    dialogo.show();
                    new ComprobarUsuario("Select * from usuarios where username='"+
                            usuario.getText().toString().trim()+"' and pass='"+
                            pass.getText().toString().trim()+"'",dialogo).execute();
               }
            }
        });
    }//Fin login
    private void registroLog(){

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLogin.this,ActivityRegistrarUsuario.class);
                startActivity(i);
            }
        });
    }
    /**
     * LanzarActivity, lanza la siguiente Activity según la categoria del USER
     */
    private void lanzarActivity() {

         switch (USER.getCategoria()){
            case 0: Intent i0 = new Intent(getApplicationContext(), ActivityPedidos.class);
                startActivity(i0);
                break;
            case 1: Intent i1 = new Intent(getApplicationContext(), ActivityPedidos.class);
                startActivity(i1);
                break;
            case 2: Intent i2 = new Intent(getApplicationContext(), ActivityCafe.class);
                startActivity(i2);
                break;
            case 3: Intent i3 = new Intent(getApplicationContext(), ActivityCafe.class);
                startActivity(i3);
                break;

             case 4: Intent i4 = new Intent(getApplicationContext(), ActivityCafe.class);
                 startActivity(i4);
                 break;
            default:
        }
    }//Fin lanzarActivity

    /**
     * radio, metodo onClick de los radio button implementado en el XML
     * cambia la IP cada vez que se marca un radio button
     * @param view
     */
    public void radio(View view) {
        calcularIP();
    }//Fin radio

///////////////////////////////////////////////////////////////////////////////////////////////////

    public class ComprobarUsuario extends AsyncTask<Void,Void,ResultSet> {

        String consultaLg;
        Connection conexLg;
        Statement sentenciaLg;
        android.app.AlertDialog dialog;
        ResultSet resultLg;

        public ComprobarUsuario(String consulta, android.app.AlertDialog dialog){
            this.consultaLg=consulta;
            this.dialog=dialog;
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                conexLg = DriverManager.getConnection("jdbc:mysql://" + ip + "/base20171", "ubase20171", "pbase20171");
                sentenciaLg = conexLg.createStatement();
                resultLg = null;
                publishProgress();

                resultLg = sentenciaLg.executeQuery(consultaLg);

            } catch (SQLException e) { e.printStackTrace();}

            return resultLg;
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                while (resultSet.next()) {
                    USER=new Usuario(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),
                            resultSet.getInt(4),resultSet.getString(5),resultSet.getInt(6));
                    loginCorrecto=true;
                }

                conexLg.close();
                sentenciaLg.close();
                resultLg.close();
                dialog.dismiss();

                if (USER==null)
                    Toast.makeText(getApplicationContext(), "Usuario o Contraseña incorrectos",Toast.LENGTH_SHORT).show();
                else
                    lanzarActivity();

            }catch (Exception ex) {     Log.d("Error",""+ex.getMessage());   }
        }
    }//Fin AsynTack
}//Fin Acticity