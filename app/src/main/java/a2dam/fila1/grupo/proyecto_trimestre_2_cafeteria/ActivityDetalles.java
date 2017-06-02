package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;
import dmax.dialog.SpotsDialog;

public class ActivityDetalles extends AppCompatActivity {

    AlertDialog dialogo;

    private static ListView listaProcductos;
    private static TextView precio;
    private ImageButton volver;
    private Button confirmar;
    private TimePicker reloj;

    static boolean sesion=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);

        if((this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        inflar();

        lanzarAdapter(this);
        listenerBotones();

    }
    /**
     * Infla todos los elementos del layout del activity
     */
    private void inflar() {
        dialogo          = new SpotsDialog(this,"Enviado Pedidos...");
        listaProcductos  = (ListView)findViewById(R.id.lv_dt);
        precio           = (TextView)findViewById(R.id.tv_dt_precio);
        volver           = (ImageButton)findViewById(R.id.ib_dt_volver);
        confirmar        = (Button)findViewById(R.id.btn_dt_confirmar);
        reloj            = (TimePicker)findViewById(R.id.timePicker2);
    }

    /**
     * lanzarAdapter, crea y lanza un adapter en el listView con una lista de los productos del pedido
     */
    static void lanzarAdapter(Activity activity) {
        AdapterDetalles adapta = new AdapterDetalles(activity, BDFinal.pedidosFinal);
        listaProcductos.setAdapter(adapta);
        precioTotal();
    }

    /**
     * precioTotal, calcula el precio total haciendo una suma de los precios de todos los productos
     * del pedido y redondea para sólo 2 decimales máximo
     */
    private static void precioTotal() {
        float precioFinal = 0f;
        for (int i = 0; i < BDFinal.pedidosFinal.size(); i++){
            precioFinal += BDFinal.pedidosFinal.get(i).getPrecio();
        }
        double redondeo = Math.round(precioFinal*100.0)/100.0;
        precio.setText("" + redondeo);
    }

    /**
     * Listener de los botones de la activity
     */
    private void listenerBotones() {
        volver.setOnClickListener(new View.OnClickListener() {//Boton volver
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /**
         * Listener confirmar, comprueba que hay productos añadidos
         * Realiza un insert en la BBDD con todos los productos de la lista, el usuario y la hora
         * Una vez realizado se borra el array de pedidos y se desabilita el botón para evitar
         * realizar pedidos duplicados
         */
        confirmar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                if(!sesion){
                    Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                    startActivity(i);

                }else {
                    int hour = 0;
                    int minute = 0;

                    //Detecta la API que se esta ejecutando, si es menor que 23 se usan metodos deprecated para obtener la hora actual
                    if (Build.VERSION.SDK_INT >= 23) {
                        hour = reloj.getHour();
                        minute = reloj.getMinute();
                    } else {
                        hour = reloj.getCurrentHour();
                        minute = reloj.getCurrentMinute();
                    }
                    int hora = hour * 10000 + minute * 100;

                    if (BDFinal.pedidosFinal.size() == 0)
                        Toast.makeText(getApplicationContext(), "No se han añadido productos.", Toast.LENGTH_SHORT).show();
                    else if (!comprobarHora(hora)) {
                        Toast.makeText(getApplicationContext(), "Hora incorrecta. Horario 8:15-14:45.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Entra en el foorrrrr", Toast.LENGTH_SHORT).show();
                        for (Pedido p : BDFinal.pedidosFinal) {
                            dialogo.show();
                            String insert = "insert into pedidos (idProducto, idCliente, complementos, hora, cantidad, precio, estado) "

                                    + "values (" + p.getProducto().getNumProducto() + ","

                                    + ActivityLogin.USER.getId() + ", '" + p.getComentarios() + "', "

                                    + "'" + hora + "', " + p.getCantidad() + ", "

                                    + p.getPrecio() + ", " + 0 + ");";

                            Toast.makeText(getApplicationContext(),"Insertado realizado con exito.", Toast.LENGTH_LONG).show();

                            new Insertar(insert, dialogo).execute();
                        }
                    }
                }
            }
        });
    }//Fin Listener

    /**
     * Horario de cafeteria de 8:15 a 14:45
     * @param hora
     * @return
     */
    private boolean comprobarHora(int hora) {
        if (hora >= 81500 && hora <= 144500)
            return true;
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public class Insertar extends AsyncTask<Void,Void,ResultSet> {

        String consultaDt;
        Connection conexDt;
        Statement sentenciaDt;
        android.app.AlertDialog dialog;

        public Insertar(String consulta, android.app.AlertDialog dialog){
            this.consultaDt=consulta;
            this.dialog=dialog;
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                conexDt = DriverManager.getConnection("jdbc:mysql://" + ActivityLogin.ip + "/base20171", "ubase20171", "pbase20171");
                sentenciaDt = conexDt.createStatement();
                publishProgress();

                if (consultaDt.startsWith("insert"))
                    sentenciaDt.executeUpdate(consultaDt);

            } catch (SQLException a) {
                a.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try {
                confirmar.setEnabled(false);
                confirmar.setBackgroundColor(0xaaC69487);
                BDFinal.pedidosFinal.clear();
                Toast.makeText(getApplicationContext(),"Pedido realizado con exito.", Toast.LENGTH_LONG).show();

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

}//Fin Activity

