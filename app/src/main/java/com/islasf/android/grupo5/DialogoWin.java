package com.islasf.android.grupo5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author Carlos García y Javier Sánchez
 */

public class DialogoWin extends DialogFragment {

    private CheckBox cbSave;
    private EditText edText;

    public DialogoWin(){
        super();
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //Hay que inflar la vista con el layout del diálogo y sacar los componentes de ella.
        View view=inflater.inflate(R.layout.dialogowin_layout, null);
        cbSave=(CheckBox)view.findViewById(R.id.cbSave);
        edText=(EditText)view.findViewById(R.id.etUser);

        builder.setView(view)
                .setNeutralButton(getString(R.string.restart), new DialogInterface.OnClickListener()  {
                    public void onClick(DialogInterface dialog, int id) {
                        if (edText.getText().toString().equals(""))
                            edText.setText(getString(R.string.dUser));
                        ActividadPrincipal activity=(ActividadPrincipal)getActivity();
                        activity.onRespuesta(1, cbSave.isChecked(), edText.getText().toString());
                    }
                })
                .setPositiveButton(getString(R.string.newGame), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (edText.getText().toString().equals(""))
                            edText.setText(getString(R.string.dUser));
                        ActividadPrincipal activity=(ActividadPrincipal)getActivity();
                        activity.onRespuesta(0, cbSave.isChecked(), edText.getText().toString());
                    }
                });

        return builder.create();
    }
}
