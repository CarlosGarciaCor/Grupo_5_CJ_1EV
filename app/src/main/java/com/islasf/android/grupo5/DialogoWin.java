package com.islasf.android.grupo5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * @author Carlos García y Javier Sánchez
 */

public class DialogoWin extends DialogFragment {

    public DialogoWin(){
        super();
    };


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setMessage(getString(R.string.dDesc))
                .setTitle(getString(R.string.dTitle))
                .setNeutralButton(getString(R.string.restart), new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        ActividadPrincipal activity=(ActividadPrincipal)getActivity();
                        activity.onRespuesta(1);
                    }
                })
                .setPositiveButton(getString(R.string.newGame), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActividadPrincipal activity=(ActividadPrincipal)getActivity();
                        activity.onRespuesta(0);
                    }
                });

        return builder.create();
    }
}
