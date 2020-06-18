package sv.edu.ues.eisi.fia.procesosadministrativosfia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class SolicitudRevision_actualizar extends Activity {

    EditText carnet, notaactual, numerogrupo, fechasol, motrevision, codasignatura,  numeroevaluacion, codciclo;
    Spinner codtiporevision, codtipoevaluacion, codtipogrupo;

    ControladorBase helper;

    private int nYearIni, nMonthIni, nDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_revision_actualizar);

        helper = new ControladorBase(this);


        carnet= (EditText) findViewById(R.id.editCarnet);
        notaactual= (EditText) findViewById(R.id.editNotaActual);
        numerogrupo= (EditText) findViewById(R.id.editnumerogrupo);
        fechasol= (EditText) findViewById(R.id.editFechaSolicitud);
        motrevision= (EditText) findViewById(R.id.editmotivorev);
        codasignatura = (EditText) findViewById(R.id.editCodasignatura);
        numeroevaluacion = (EditText) findViewById(R.id.editNumeval);
        codciclo = (EditText) findViewById(R.id.editCodciclo);
        codtiporevision = (Spinner) findViewById(R.id.spinTipoRev);
        codtipoevaluacion = (Spinner) findViewById(R.id.spinTipoEval);
        codtipogrupo= (Spinner) findViewById(R.id.spinTipoGrupo);

        sMonthIni = c.get(Calendar.MONTH);
        sDayIni = c.get(Calendar.DAY_OF_MONTH);
        sYearIni = c.get(Calendar.YEAR);

        fechasol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_ID);
            }
        });

    }


    private void colocar_fecha() {
        if(String.valueOf(nMonthIni).length() == 1 && String.valueOf(nDayIni).length() == 1){
            fechasol.setText( nYearIni + "-0" + (nMonthIni + 1) + "-0" +  nDayIni );
        }else if(String.valueOf(nMonthIni).length() == 1){
            fechasol.setText( nYearIni + "-0" + (nMonthIni + 1) + "-" +  nDayIni );
        }else if(String.valueOf(nDayIni).length() == 1){
            fechasol.setText( nYearIni + "-" + (nMonthIni + 1) + "-0" +  nDayIni );
        }else{
            fechasol.setText( nYearIni + "-" + (nMonthIni + 1) + "-" +  nDayIni );
        }
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    nYearIni = year;
                    nMonthIni = monthOfYear;
                    nDayIni = dayOfMonth;
                    colocar_fecha();
                }
            };
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDateSetListener, sYearIni, sMonthIni, sDayIni);
        }
        return null;
    }


    public void actualizarSolicitudRevision(View v){

        String regInsertados;
        String tiporevision, tipoeva, tipogrupo;
        tiporevision = codtiporevision.getSelectedItem().toString();
        tipoeva = codtipoevaluacion.getSelectedItem().toString();
        tipogrupo = codtipogrupo.getSelectedItem().toString();

        SolicitudRevision solicitud = new SolicitudRevision();
        solicitud.setCarnet(carnet.getText().toString());
        solicitud.setFechasolicitudrevision(fechasol.getText().toString());
        solicitud.setMotivorevision(motrevision.getText().toString());
        solicitud.setCodasignatura(codasignatura.getText().toString());
        solicitud.setCodciclo(codciclo.getText().toString());
        solicitud.setCodtiporevision(tiporevision);
        solicitud.setCodtipoeval(tipoeva);
        solicitud.setCodtipogrupo(tipogrupo);


        if(!numeroevaluacion.getText().toString().isEmpty()){
            solicitud.setNumeroeval(Integer.parseInt(numeroevaluacion.getText().toString()));
        }else{
            solicitud.setNumeroeval(0);
        }

        if(!notaactual.getText().toString().isEmpty()){
            solicitud.setNotaantesrevision(Float.parseFloat(notaactual.getText().toString()));
        }
        else{
            solicitud.setNotaantesrevision(0.0f);
        }

        if(!numerogrupo.getText().toString().isEmpty()){
            solicitud.setNumerogrupo(Integer.parseInt(numerogrupo.getText().toString()));
        }
        else{
            solicitud.setNumerogrupo(0);
        }


        helper.abrir();
        regInsertados = helper.actualizar(solicitud);
        helper.cerrar();
        Toast.makeText(this, regInsertados, Toast.LENGTH_SHORT).show();
    }



    public void limpiarTexto(View v){
        carnet.setText("");
        notaactual.setText("");
        numerogrupo.setText("");
        fechasol.setText("");
        motrevision.setText("");
        codasignatura.setText("");
        numeroevaluacion.setText("");
        codciclo.setText("");
        codtiporevision.setSelection(0);
        codtipoevaluacion.setSelection(0);
        codtipogrupo.setSelection(0);

    }
}
