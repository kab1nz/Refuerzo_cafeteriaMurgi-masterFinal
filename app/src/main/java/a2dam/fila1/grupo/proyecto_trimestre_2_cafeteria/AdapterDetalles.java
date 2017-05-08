package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

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

/**
 * Created by Gand on 20/01/17.
 */

public class AdapterDetalles extends BaseAdapter {

    private ArrayList<Pedido> pedidos;

    public AdapterDetalles(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public int getCount() {
        return pedidos.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View vista, final ViewGroup parent) {
        if (vista==null){
            Context cnt=parent.getContext();
            vista= LayoutInflater.from(cnt).inflate(R.layout.list_producto,null);
        }
        final Pedido pedidoActual=pedidos.get(position);

        ((TextView)vista.findViewById(R.id.tv_dt_list_nombre)).setText(pedidoActual.getProducto().getNombre());
        ((TextView)vista.findViewById(R.id.tvListPedidosPrecio)).setText(""+pedidoActual.getPrecio()+" €");
        ((TextView)vista.findViewById(R.id.tv_dt_list_cantidad)).setText("x"+pedidoActual.getCantidad());
        ((TextView)vista.findViewById(R.id.tv_dt_list_comentarios)).setText(pedidoActual.getComentarios());

        if (!pedidoActual.getComentarios().contains("lactosa")){
            ((ImageView)vista.findViewById(R.id.ivListPedidosAlert)).setVisibility(View.GONE);
        }

        if (ActivityLogin.USER.getCategoria()<2){
            vista.findViewById(R.id.ibListDelete).setVisibility(View.GONE);
        }else{
            vista.findViewById(R.id.ibListDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(parent.getContext())
                            .setTitle("Eliminar producto")
                            .setMessage("¿Quiere eliminar el producto?")
                            .setNegativeButton("Cancelar", null)
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    pedidos.remove(pedidoActual);
                                    BDFinal.pedidosFinal = pedidos;
                                    ActivityDetalles.lanzarAdapter();
                                }
                            }).create().show();
                }
            });
        }

        return vista;
    }
}
