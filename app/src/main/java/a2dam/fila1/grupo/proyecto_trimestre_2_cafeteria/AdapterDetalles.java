package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;

import static a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.AdapterPedidos.pedidos;

/**
 * Created by Gand on 20/01/17.
 */

public class AdapterDetalles extends BaseAdapter {
    ArrayList<Pedido> aPedidos=new ArrayList<>() ;
    Activity activity;

    public AdapterDetalles(Activity activity, ArrayList aPedidos) {
        this.activity=activity;
        this.aPedidos=aPedidos;
    }

    public AdapterDetalles(ArrayList<Pedido> pedidos) {
        this.aPedidos = pedidos;
    }

    @Override
    public int getCount() {
        return aPedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.list_producto, null);
        }

        final Pedido pedidoActual=aPedidos.get(position);
        Pedido pedido = aPedidos.get(position);

        ImageView imagen=(ImageView)v.findViewById(R.id.ivListPedidosLogo);
        TextView nombre=(TextView)v.findViewById(R.id.tv_dt_list_nombre);
        TextView comentarios=(TextView)v.findViewById(R.id.tv_dt_list_comentarios);
        TextView precio=(TextView)v.findViewById(R.id.tvListPedidosPrecio);
        TextView veces =(TextView)v.findViewById(R.id.tv_dt_list_cantidad) ;

        nombre.setText(pedido.getProducto().getNombre());
        comentarios.setText(pedido.getComentarios());
        precio.setText(""+pedido.getProducto().getPrecio());
        veces.setText(""+pedido.getCantidad());

        if (!pedidoActual.getComentarios().contains("lactosa")){
            ((ImageView)v.findViewById(R.id.ivListPedidosAlert)).setVisibility(View.GONE);
        }

            v.findViewById(R.id.ibListDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(parent.getContext())
                            .setTitle("Eliminar producto")
                            .setMessage("¿Quiere eliminar el producto?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    aPedidos.remove(pedidoActual);
                                    BDFinal.pedidosFinal = aPedidos;
                                    ActivityDetalles.lanzarAdapter(activity);
                                }
                            }).create().show();
                }
            });
//brtyr6hy6r5
        return v;
    }
}