package sv.edu.ues.eisi.fia.procesosadministrativosfia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SolicitudRevision_consultar extends Activity {

    EditText carnet, notaactual, numerogrupo, fechasol, motrevision, codasignatura,  numeroevaluacion, codciclo;
    Spinner codtiporevision, codtipoevaluacion, codtipogrupo;

    ControladorBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud_revision_consultar);

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

    }


    public void consultarSolicitudRevision(View v){

        helper.abrir();

        String regInsertados;
        String tiporevision, tipoeva, tipogrupo;
        tiporevision = codtiporevision.getSelectedItem().toString();
        tipoeva = codtipoevaluacion.getSelectedItem().toString();
        tipogrupo = codtipogrupo.getSelectedItem().toString();

        SolicitudRevision solRev =  helper.consultarSolicitudRevision(carnet.getText().toString(), tipogrupo, tiporevision, tipoeva, codasignatura.getText().toString(), String.valueOf(numeroevaluacion.getText()), codciclo.getText().toString());
        helper.cerrar();
        if(solRev == null){
            Toast.makeText(this, "Solicitud de Revision no registrado", Toast.LENGTH_LONG).show();
        }else{
            notaactual.setText(String.valueOf(solRev.getNotaantesrevision()));
            numerogrupo.setText(String.valueOf(solRev.getNumerogrupo()));
            fechasol.setText(solRev.getFechasolicitudrevision());
            motrevision.setText(solRev.getMotivorevision());
        }
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
