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
 * Diálogo personalizado que extiende de DialogFragment.
 * Un diálogo en Android es un Fragment, es algo así como un fragmento de una actividad, no una
 * actividad completa. Hay fragmentos predefinidos pero también puedes programarlos de forma
 * personalizada, como es este caso.
 *
 * Para definirlo de forma personalizada hay que asignarle una vista, un layout. El layout
 * de este fragmento es "dialogowin_layout.xml". En él dibujamos un título, un contenido e incluimos
 * un EditText y un CheckBox para que el usuario meta su nombre y decida si quiere guardar el resultado
 * o no.
 *
 * El ciclo de vida de un fragmento empieza cuando lo llamas desde alguna actividad con el método
 * show(). El método en el que se construye el diálogo es el onCreateDialog, sobrescrito en esta clase.
 * @author Carlos García y Javier Sánchez
 */

public class DialogoWin extends DialogFragment {

    /**
     * Atributo que hace referencia al CheckBox del diálogo, según el cual se va a guardar el resultado
     * en la base de datos o no.
     */
    private CheckBox cbSave;
    /**
     * EditText donde el usuario va a introducir el nombre con el que quiere guardar el resultado.
     */
    private EditText edText;

    /**
     * Constructor que únicamente llama al constructor de la clase padre. Obligatorio ponerlo.
     */
    public DialogoWin(){
        super();
    };


    /**
     * Este método es parecido al onCreate de las actividades. En él básicamente se va a construir el mensaje.
     * Se ejecuta al llamar al método show.
     *
     * Para construir el mensaje con un layout personalizado es necesario primero inflar ese layout.
     * Esto se hace con un objeto LayoutInflater. El layout inflado se le pasa como vista al fragment, y de
     * esa vista se sacan los componentes con los que se va a trabajar (EditText y CheckBox).
     *
     * El diálogo va a tener dos botones: uno para reiniciar la partida y otro para crear una nueva. Los botones
     * no están incluidos en el layout sino que los construye éste método directamente con los métodos
     * setNeutralButton y setPositiveButton.
     *
     * Los botones llaman al método público onRespuesta de la actividad principal.
     * @param savedInstanceState Bundle en el cual puedes meter datos para poder restaurarlos (en caso
     *                           de rotar la pantalla, por ejemplo)
     * @return Devuelve el diálogo contruido
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        setCancelable(false); //Para que sea modal, que obligue al usuario a actuar en el dialog

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
