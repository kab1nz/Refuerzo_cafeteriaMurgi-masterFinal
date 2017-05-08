package a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.ActivityLogin;
import a2dam.fila1.grupo.proyecto_trimestre_2_cafeteria.R;

/**
 * Created by Gand on 03/12/16.
 */

/**
 * Clase para mostrar un AlertDialog personalizado con los datos licencias de imagenes
 * Layout asociada dialog_licencia.xml
 */
public class Dialog_menu extends DialogFragment {

    public Dialog_menu() {   }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogo();
    }

    public AlertDialog createDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_menu, null);

        builder.setView(v);

        ((ImageView)v.findViewById(R.id.iv_dialog_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }
}