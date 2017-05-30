package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.BDFinal;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Pedido;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Bd.Producto;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Dialog.Dialog_menu;
import dmax.dialog.SpotsDialog;

public class ActivityCafe extends AppCompatActivity {

    private AlertDialog dialogo;// Dialogo

    private Spinner spTipo;     //Tipo de cafe hola
    private Spinner spLeche;    //Temperatura leche
    private Spinner spAzucar;   //Tipo de azucar

    private CheckBox lactosa;
    private CheckBox crema;
    private CheckBox chocolate;
    private CheckBox hielo;

    private TextView precio;
    private TextView cantidad;

    private ImageButton volver;
    private ImageButton menu;

    private Button pedir;
    private Button menos;
    private Button mas;


    private FloatingActionButton fab;

    ArrayAdapter<CharSequence> adapterLeche;    //Adapter para el spinner de la temperatura de la leche
    ArrayAdapter<CharSequence> adapterAzucar;   //Adapter para el spinner del tipo de azucar
    ArrayAdapter adapterTipo;           //Adapter para el spinner de tipos de cafe


    static ArrayList<String> arrayTipo = new ArrayList<>();
    static float precioCafeFinal;
    static boolean datos = false;
    static boolean leche = false;
    static String datosPedido;
    static int id;
    static String nombre;
    static int cant;
    static float precioCafe = 0f;//Precio cafe

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);

        if((this.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                <= Configuration.SCREENLAYOUT_SIZE_LARGE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        inflar();
        listenerBotones();
        inflarSpinnerTipoCafe();
    }//Fin onCreate

    /**
     * Carga la BBDD desde cada vez que se cargue la Activity
     */
    @Override
    protected void onStart() {
        super.onStart();
        arrayTipo.clear();
        datos = false;

    }

    /**
     * Activa el Dialog con el String del parametro
     * @param texto
     */
    private void lanzarDialogo(String texto) {
        dialogo = new SpotsDialog(this, texto);
        dialogo.show();
    }

    /**
     * Infla todos los elementos del layout del activity
     */
    private void inflar() {
        spTipo       = (Spinner)     findViewById(R.id.sp_cf_tipo);
        spLeche      = (Spinner)     findViewById(R.id.sp_cf_leche);
        spAzucar     = (Spinner)     findViewById(R.id.sp_cf_azucar);

        lactosa      = (CheckBox)    findViewById(R.id.cb_cf_lactosa);
        crema        = (CheckBox)    findViewById(R.id.cb_cf_crema);
        chocolate    = (CheckBox)    findViewById(R.id.cb_cf_choco);
        hielo        = (CheckBox)    findViewById(R.id.cb_cf_hielo);

        precio       = (TextView)    findViewById(R.id.tv_cf_precioNum);
        cantidad     = (TextView)    findViewById(R.id.tv_cnt_cantidad);

        volver       = (ImageButton) findViewById(R.id.ib_cf_volver);
        menu         = (ImageButton) findViewById(R.id.ib_cf_menu);

        pedir        = (Button)      findViewById(R.id.btn_cf_pedir);
        menos        = (Button)      findViewById(R.id.btn_cnt_menos);
        mas          = (Button)      findViewById(R.id.btn_cnt_mas);

        fab          = (FloatingActionButton) findViewById(R.id.fab_cf);

        adapterLeche = ArrayAdapter.createFromResource(this, R.array.leche , android.R.layout.simple_spinner_dropdown_item);
        adapterAzucar= ArrayAdapter.createFromResource(this, R.array.azucar, android.R.layout.simple_spinner_dropdown_item);

        adapterTipo  = ArrayAdapter.createFromResource(this, R.array.productos, android.R.layout.simple_spinner_dropdown_item);
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spLeche .setAdapter(adapterLeche);
        spAzucar.setAdapter(adapterAzucar);
        spTipo.setAdapter(adapterTipo);

    }//Fin inflar

    /**
     * Captura la acción de pulsar el botón atrás y vuelve a la pantalla de login, borrando los pedidos guardados
     */
    /*
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("Si sale se cerrará la sesión y se perderán los pedidos no realizados\n¿Continuar?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //ActivityLogin.USER=null;
                        BDFinal.pedidosFinal.clear();
                        finish();
                    }
                }).create().show();
    }
    */


    /**
     * Métodos de los spinner
     */
    private void inflarSpinnerTipoCafe() {
        lanzarDialogo("Calculando precios...");


        //Al seleccionar un tipo de cafe accedemos a la bbdd para consultar su precio y la posibilidad de acompañar con leche
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch (position){
                        case 0:
                            Toast.makeText(parent.getContext(), "Hola de prueba", Toast.LENGTH_LONG).show();


                                precioCafe =0;
                                setPrecio();
                                llevaLeche(false);
                                dialogo.dismiss();


                            break;

                        case 1:
                            precioCafe =0.90f;
                            nombre="cortado";
                            llevaLeche(true);
                            setPrecio();

                            break;
                        case 2:
                            nombre="con leche";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(true);

                            break;

                        case 3:
                            nombre="Manchada";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(true);

                            break;

                        case 4:
                            nombre="Solo";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;

                        case 5:
                            nombre="Bombón";
                            precioCafe =1;
                            setPrecio();
                            llevaLeche(false);

                        case 6:
                            nombre="Des. sobre";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(true);
                            break;

                        case 7:
                            nombre="Des. máquina";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(true);
                            break;

                        case 8:
                            nombre="Colacao";
                            precioCafe =1;
                            setPrecio();
                            llevaLeche(true);
                            break;

                        case 9:
                            nombre="Infusión manz.";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;

                        case 10:
                            nombre="Infusión Menta-Poleo";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;

                        case 11:
                            nombre="Infusión Tila";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;

                        case 12:
                            nombre="Infusión Té Rojo";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;

                        case 13:
                            nombre="Insusión Té Verde";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;
                        case 14:
                            nombre="Infusión Té";
                            precioCafe =0.90f;
                            setPrecio();
                            llevaLeche(false);
                            break;
                        default: dialogo.show();

                    }
                }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //No se usa pero no se puede borrar
            }
        });//Fin Spinner Tipo
    }

    /**
     * Métodos lístener de todos los botones del layout
     */
    private void listenerBotones() {

        menu.setOnClickListener(new View.OnClickListener() {//Muestra la imagen con los cafes disponibles
            @Override
            public void onClick(View v) {
                Dialog_menu dialogoMenu= new Dialog_menu();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogoMenu.setCancelable(true);
                dialogoMenu.show(ft, "Menú");
            }
        });

        volver.setOnClickListener(new View.OnClickListener() {//Boton volver
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        menos.setOnClickListener(new View.OnClickListener() {//Boton menos resta un cafe
            @Override
            public void onClick(View v) {
                setCantidad(-1);
            }
        });


        mas.setOnClickListener(new View.OnClickListener() {//Boton mas agrega un cafe
            @Override
            public void onClick(View v) {
                setCantidad(1);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spTipo.getSelectedItemPosition() != 0){
                    precioCafeFinal=Float.parseFloat(setPrecio());
                    Producto producto = new Producto(id,nombre,precioCafeFinal,leche);
                    Pedido pedido = new Pedido(producto,cant+1,precioCafeFinal, generarComentarios());
                    BDFinal.pedidosFinal.add(pedido);
                    Toast.makeText(getApplicationContext(),"Añadido a la cesta",Toast.LENGTH_SHORT).show();

                    limpiar();
                }else
                    Toast.makeText(getApplicationContext(),"Debe seleccionar un TIPO de café",Toast.LENGTH_SHORT).show();
            }
        });

 pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Lanza la activity detalles para conocer el resumen del pedido
                if (BDFinal.pedidosFinal.size() == 0){
                    Toast.makeText(getApplicationContext(),"Debe añadir al menos un producto",Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(getApplicationContext(), ActivityDetalles.class);
                    startActivity(i);
                }
            }
        });

    }//Fin ListenerBotones

    /**
     * Limpiar, al realizar un pedido con FAB, desmarca los checkBox y coloca los spinner  y la
     * cantidad en posicion inicial
     */
    private void limpiar() {
        spTipo      .setSelection(0);
        spLeche     .setSelection(0);
        spAzucar    .setSelection(0);
        lactosa     .setChecked(false);
        crema       .setChecked(false);
        chocolate   .setChecked(false);
        hielo       .setChecked(false);
        cantidad    .setText(""+1);

        //setPrecio();
    }//Fin limpiar

    /**
     * Comprueba los checkbox y spinner seleccionados
     * @return String: Cadena con las opciones seleccionadas en spinner y checkbox
     */
    private String generarComentarios() {
        String comentarios = "";
        if (leche)
            comentarios = comentarios.concat(spLeche.getSelectedItem().toString().trim()+", ");
        comentarios = comentarios.concat(spAzucar.getSelectedItem().toString().trim()+"");
        if (leche)
            if (lactosa.isChecked())
                comentarios = comentarios.concat(", Sin lactosa");
        if (crema.isChecked())
            comentarios = comentarios.concat(", Crema");
        if (chocolate.isChecked())
            comentarios = comentarios.concat(", Chocolate");
        if (hielo.isChecked())
            comentarios = comentarios.concat(", Hielo");

        return comentarios;
    }//Fin generarComentarios

    /**
     * setCantidad, calcula y controla la cantidad de producto a pedir
     * @param i Cantidad a sumar o restar a la cantidad actual
     */
    private void setCantidad(int i) {
        cant = Integer.parseInt(cantidad.getText().toString().trim());
        if ((cant + i) < 1 || (cant + i) > 20 )
            Toast.makeText(getApplicationContext(), "Cantidad mínima 1, catidad máxima 20", Toast.LENGTH_SHORT).show();
        else
            cantidad.setText("" + (cant + i));

        setPrecio();
    }//Fin setCantidad

    /**
     * setPrecio, calcula el precio del pedido según el producto y las opciones seleccionadas
     * Redondea para que salga sólo con 2 decimales máximo
     */


    private String setPrecio(){
        String d;
        float precioFinal = precioCafe;
        if (leche)
            if (lactosa.isChecked())
                precioFinal += 0.2f;
        if (hielo.isChecked())
            precioFinal += 0.3f;
        if (crema.isChecked())
            precioFinal += 0.4f;
        if (chocolate.isChecked())
            precioFinal += 0.5f;

        precioFinal = precioFinal * Integer.parseInt(cantidad.getText().toString().trim());
        double redondeo = Math.round(precioFinal*100.0)/100.0;
        precio.setText(""+redondeo);
        return  d= String.valueOf(redondeo);
    }//Fin setPrecio

    /**
     * Habilita o deshabilita las opciones de leche según el producto
     * @param leche
     */
    private void llevaLeche(boolean leche) {
        spLeche.setEnabled(leche);
        lactosa.setEnabled(leche);
    }//Fin llevaLeche

    /**
     * Actualiza precio al detectar eventos de los checkbox (implementados en XML)
     * @param view
     */
    public void metodosCheked(View view) {
        setPrecio();
    }//Fin metodosCheked

////////////////////////////////////////////////////////////////////////////////////////////////////
}//Fin Acticity
