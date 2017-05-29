package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.list_producto, null);
        }

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
        return v;
    }
}