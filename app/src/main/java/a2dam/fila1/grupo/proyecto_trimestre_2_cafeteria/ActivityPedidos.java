package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Producto;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Usuario;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.VistaPedido;
import dmax.dialog.SpotsDialog;

public class ActivityPedidos extends AppCompatActivity {

    AlertDialog dialogo;
    private ListView listView;

    ActualizacionPedidos actualizacionPedidos;


    ArrayList<VistaPedido> vistaPedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        if((this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }//Fin onCreate

    @Override
    protected void onStart() {
        super.onStart();
        /**
         * Actualiza la base de datos y lanza el adapter5
         */
            dialogo = new SpotsDialog(this,  "Cargando pedidos...");
            dialogo.show();

            vistaPedidos.clear();


            String consulta = "select username, hora, sum(precio) as total, estado from pedidos, usuarios " +
                    "where id_cli = idCliente group by username, hora order by hora, num_pedido, username, estado, total";
            new ConsultasPedidos(consulta, dialogo).execute();
            actualizacionPedidos = new ActualizacionPedidos();
            actualizacionPedidos.execute();

    }

    /**
     * Lanza el Adapter del listView con todos los pedidos actuales
     */
    private void lanzarAdapter() {
        listView = (ListView) findViewById(R.id.lvAPedidos);
        listView.setAdapter(new AdapterPedidos(vistaPedidos));
        itemListener();
    }//Fin lanzarAdapter

    private void itemListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogo.show();

                String consulta = "select nom_pro, complementos, cantidad, pedidos.precio, idCliente, " +
                        "username, hora, email from usuarios, pedidos, productos " +
                        "where usuarios.id_cli = pedidos.idCliente " +
                        "AND productos.id_pro = pedidos.idProducto AND " +
                        "username = '" + vistaPedidos.get(position).getNombre() + "' AND " +
                        "hora = '" + vistaPedidos.get(position).getHora() + "'" +
                        "group by idCliente, hora, num_pedido;";
                new ConsultasPedidos(consulta, dialogo).execute();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String nombre = vistaPedidos.get(position).getNombre();
                final String hora = vistaPedidos.get(position).getHora();
                new android.support.v7.app.AlertDialog.Builder(ActivityPedidos.this)
                        .setTitle("Finalizar Pedido")
                        .setMessage("¿Finalizar y eliminar el pedido?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                borrarPedido(nombre,hora);
                            }
                        }).create().show();
                return true;
            }
        });
    }//Fin itemlistener

    /**
     * Lanza ActivityDetalles con todos los productos del pedido
     * @param usuario
     * @param hora
     */
    private void lanzarDetalles(String usuario, int idCli, String hora) {
        actualizacionPedidos.cancel(true);
        dialogo.show();
        String update = "update pedidos set estado = 1 where idCliente = " + idCli +
                            " and hora = '" + hora+ "'";
        new ConsultasPedidos(update, dialogo).execute();
        Intent intent = new Intent(getApplicationContext(), ActivityPedidosDetalles.class);
        startActivity(intent);
    }//Fin lanzarDetalles

    /**
     * Borra todos los pedidos del usuario y hora seleccionados para limpiar la lista de pedidos
     * finalizados
     * @param usuario
     * @param hora
     */


    public void borrarPedido(String usuario, String hora){
        String delete = "delete from pedidos where idCliente = (Select id_cli " +
                "from usuarios where username = '" + usuario + "') " +
                "and hora = '" + hora + "'";
        new ConsultasPedidos(delete, dialogo).execute();
    }

    /**
     * Captura la acción de pulsar el botón atrás y vuelve a la pantalla de login
     */
    /*
    @Override

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que desea cerrar sesión?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        actualizacionPedidos.cancel(true);
                        finish();
//                        Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
//                        startActivity(i);
                    }
                }).create().show();
    }//Fin onBackPressed
    */

////////////////////////////////////////////////////////////////////////////////////////////////////

    public class ConsultasPedidos extends AsyncTask<Void,Void,ResultSet> {

        android.app.AlertDialog dialog;
        String consultaPd;
        Connection conexPd;
        Statement sentenciaPd;
        ResultSet resultPd;

        public ConsultasPedidos(String consulta, android.app.AlertDialog dialog){
            this.consultaPd=consulta;
            this.dialog=dialog;
        }

        @Override
        protected ResultSet doInBackground(Void... params) {

            try {
                conexPd = DriverManager.getConnection("jdbc:mysql://" + ActivityLogin.ip + "/base20171",
                        "ubase20171", "pbase20171");
                sentenciaPd = conexPd.createStatement();
                resultPd = null;
                publishProgress();

                if (consultaPd.startsWith("select"))
                    resultPd = sentenciaPd.executeQuery(consultaPd);

                if (consultaPd.startsWith("update") || consultaPd.startsWith("delete")){
                    sentenciaPd.executeUpdate(consultaPd);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resultPd;
        }

        @Override
        protected void onPostExecute(ResultSet resultSet) {
            super.onPostExecute(resultSet);

            try{
                if (consultaPd.contains("sum(precio)")){
                    while (resultSet.next()){
                        vistaPedidos.add(new VistaPedido(resultSet.getString("username"),
                                resultSet.getTime("hora").toString(), resultSet.getFloat("total"),
                                resultSet.getInt("estado")));
                    }
                    lanzarAdapter();
                }

                if (consultaPd.contains("complementos")){
                    int idCli = 0;
                    String usuario = null;
                    String hora = null;

                    while (resultSet.next()){
                         BDFinal.pedidosFinal.add(new Pedido(new Usuario(resultSet.getInt("idCliente"),
                                 resultSet.getString("username"), resultSet.getString("email")),
                                 new Producto(resultSet.getString("nom_pro")),
                                 resultSet.getInt("cantidad"), resultSet.getFloat("precio"),
                                 resultSet.getString("complementos"), resultSet.getString("hora")));
                    }
                    idCli = BDFinal.pedidosFinal.get(0).getUsuario().getId();
                    usuario = BDFinal.pedidosFinal.get(0).getUsuario().getNombre();
                    hora = BDFinal.pedidosFinal.get(0).getHora().trim();

                    lanzarDetalles(usuario, idCli, hora);
                }

                if (consultaPd.startsWith("delete")){
                    lanzarAdapter();
                }

                conexPd.close();
                sentenciaPd.close();
                resultPd.close();
                dialog.dismiss();

            }catch (Exception ex) { Log.d("Fallo de cojones",""); }
        }
    }//Fin AsynTack


    ///////////////////////////////////////////////////////////////////////////////////////////////
    /**
     *  ///////////////////////////////////////////////////////////////////////////////////////////
     */
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public class ActualizacionPedidos extends AsyncTask<Void,Void,Void> {

        public ActualizacionPedidos(){  }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress();
                return null;
        }

        @Override
        protected void onProgressUpdate(Void... voids)
        {
            super.onProgressUpdate();
            onStart();
            dialogo.dismiss();
        }
    }//Fin AsynTack

}//Fin Activity
