package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Email.SendMail;
import dmax.dialog.SpotsDialog;

public class ActivityPedidosDetalles extends AppCompatActivity {
    AlertDialog dialogo;
    private static boolean mail = false;
    private ListView listView;
    private TextView tvNombre, tvHora, precioT;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_detalles);

        if((this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        inflar();
        listeners();//Todos los listener

    }

    private void inflar() {
        dialogo     = new SpotsDialog(this,  "Termiando pedidos...");

        listView    = (ListView) findViewById(R.id.lvAPedidosD);

        tvNombre    = (TextView) findViewById(R.id.tvAPedidosDNomCli);
        tvHora      = (TextView) findViewById(R.id.tvAPedidosDHora);
        precioT     = (TextView) findViewById(R.id.tvAPedidosDPrecioT);

        fab         = (FloatingActionButton) findViewById(R.id.fab_done);

        tvNombre    .setText(BDFinal.pedidosFinal.get(0).getUsuario().getNombre());
        tvHora      .setText(BDFinal.pedidosFinal.get(0).getHora());
        precioT     .setText(calcularPrecio() + " €");
    }

    @Override
    public void onBackPressed() {
        BDFinal.pedidosFinal.clear();
        finish();
    }

    private void listeners() {
        listView.setAdapter(new AdapterPedidosDetalles(BDFinal.pedidosFinal));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SMS
                /*
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(""+numTelefono, null, "Pedido preparado para las "
                            + BDFinal.pedidosFinal.get(0).getHora()
                            + " con un precio total de " + precioT.getText(), null, null);


                 */
                if (!mail){
                    String message = "Pedido preparado para las " + BDFinal.pedidosFinal.get(0).getHora()
                            + " con un precio total de " + precioT.getText();
                    SendMail sendEmail = new SendMail(ActivityPedidosDetalles.this,
                            BDFinal.pedidosFinal.get(0).getUsuario().getMail(),
                            "Pedido C@fetería", message);


                    sendEmail.execute();//ejecuta el AsynTask  //Descomentar esta liena para mail

                    fab.setImageResource(R.drawable.ic_done);
                    mail = true;

                    String update = "update pedidos set estado = 2 where idCliente = " +
                            BDFinal.pedidosFinal.get(0).getUsuario().getId() +
                            " and hora = '" + BDFinal.pedidosFinal.get(0).getHora() + "'";
                    new UpdatePedido(update, dialogo).execute();
                }else{
                    mail = false;
                    onBackPressed();
                }
            }
        });
    }

    private float calcularPrecio() {
        float p = 0;
        for (Pedido pd : BDFinal.pedidosFinal){
            p += pd.getPrecio();
        }
        float redondeo = (float) (Math.round(p*100.0)/100.0);
        return redondeo;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////

    public class UpdatePedido extends AsyncTask<Void,Void,Void> {

        String consultaPDt;
        Connection conexPDt;
        Statement sentenciaPDt;
        android.app.AlertDialog dialog;

        public UpdatePedido(String consulta, android.app.AlertDialog dialog){
            this.consultaPDt=consulta;
            this.dialog=dialog;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                conexPDt = DriverManager.getConnection("jdbc:mysql://" + ActivityLogin.ip + "/base20171",
                        "ubase20171", "pbase20171");
                sentenciaPDt = conexPDt.createStatement();
                publishProgress();

                if (consultaPDt.startsWith("update"))
                    sentenciaPDt.executeUpdate(consultaPDt);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            try {
                conexPDt.close();
                sentenciaPDt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }//Fin AsynTack
}//Fin Activity
