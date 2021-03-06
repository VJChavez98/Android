package sv.edu.ues.eisi.fia.procesosadministrativosfia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class PrimeraRevision_actualizar extends Activity {
    EditText editAsignatura, editCiclo, editNumEval, editDocente, editCarnet, editNotaFinal, editObservaciones;
    Spinner spinTipoEval, spinTipoGrupo, spinMotCambNota;
    RadioGroup radioEstado, radioAsistencia;
    RadioButton radioSi, radioNo, radioPendiente, radioTerminada;
    ControladorBase helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primera_revision_actualizar);
        helper = new ControladorBase(this);
        editAsignatura = (EditText) findViewById(R.id.editCodasignatura);
        editCiclo = (EditText) findViewById(R.id.editCodciclo);
        editNumEval = (EditText) findViewById(R.id.editNumeval);
        editDocente = (EditText) findViewById(R.id.editCoddocente);
        editCarnet = (EditText) findViewById(R.id.editCarnet);
        editNotaFinal = (EditText) findViewById(R.id.editNotaDespues);
        editObservaciones = (EditText) findViewById(R.id.editObservaciones);
        spinTipoEval = (Spinner) findViewById(R.id.spinTipoEval);
        spinMotCambNota = (Spinner) findViewById(R.id.spinMotCambioNota);
        spinTipoGrupo = (Spinner) findViewById(R.id.spinTipoGrupo);
        radioEstado = (RadioGroup) findViewById(R.id.opciones_asistencia);
        radioSi = (RadioButton) findViewById(R.id.radio_Si);
        radioNo = (RadioButton) findViewById(R.id.radio_No);
        radioAsistencia = (RadioGroup) findViewById(R.id.opciones_estado);
        radioPendiente = (RadioButton) findViewById(R.id.radio_Pendiente);
        radioTerminada = (RadioButton) findViewById(R.id.radio_Terminada);
    }

    public void actualizarPrimeraRevision(View v){
        PrimeraRevision primRev = new PrimeraRevision();
        primRev.setCodtiporevision("PR");
        primRev.setCodasignatura(editAsignatura.getText().toString());
        primRev.setCodciclo(editCiclo.getText().toString());
        primRev.setCoddocente(editDocente.getText().toString());
        primRev.setCarnet(editCarnet.getText().toString());
        primRev.setObservacionesprimerarev(editObservaciones.getText().toString());
        primRev.setMotivoCambioNota(spinMotCambNota.getSelectedItem().toString());

        if(!editNumEval.getText().toString().isEmpty()){
            primRev.setNumeroeval(Integer.parseInt(editNumEval.getText().toString()));
        }else{
            primRev.setNumeroeval(0);
        }

        if(!editNotaFinal.getText().toString().isEmpty()){
            primRev.setNotadespuesprimerarevision(Float.parseFloat(editNotaFinal.getText().toString()));
        }else{
            primRev.setNotadespuesprimerarevision(0.0f);
        }

        //Validacion de los Spinner para guardar los codigos.
        if(spinTipoEval.getSelectedItem().toString().equals("Examen Parcial")){
            String tipoEval = "EP";
            primRev.setCodtipoeval(tipoEval);
        }else if(spinTipoEval.getSelectedItem().toString().equals("Examen Discusion")){
            String tipoEval = "ED";
            primRev.setCodtipoeval(tipoEval);
        }else if(spinTipoEval.getSelectedItem().toString().equals("Examen Laboratorio")){
            String tipoEval = "EL";
            primRev.setCodtipoeval(tipoEval);
        }else{
            primRev.setCodtipoeval("");
        }

        if(spinMotCambNota.getSelectedItem().toString().equals("Error de suma")){
            String motivoCambNota = "ERRSUM";
            primRev.setMotivoCambioNota(motivoCambNota);
        }else if(spinMotCambNota.getSelectedItem().toString().equals("Error de elaboracion de preguntas")){
            String motivoCambNota = "ERRELPR";
            primRev.setMotivoCambioNota(motivoCambNota);
        }else if(spinMotCambNota.getSelectedItem().toString().equals("Error de calificacion")){
            String motivoCambNota = "ERRCAL";
            primRev.setMotivoCambioNota(motivoCambNota);
        }else{
            primRev.setMotivoCambioNota("");
        }

        if(spinTipoGrupo.getSelectedItem().toString().equals("GT")){
            String tipoGrupo = "GT";
            primRev.setCodtipogrupo(tipoGrupo);
        }else if(spinTipoGrupo.getSelectedItem().toString().equals("GD")){
            String tipoGrupo = "GD";
            primRev.setCodtipogrupo(tipoGrupo);
        }else if(spinTipoGrupo.getSelectedItem().toString().equals("GL")){
            String tipoGrupo = "GL";
            primRev.setCodtipogrupo(tipoGrupo);
        }else{
            primRev.setCodtipogrupo("");
        }

        if(radioSi.isChecked()){
            String asistencia = "SI";
            primRev.setAsistio(asistencia);
        }else if(radioNo.isChecked()){
            String asistencia = "NO";
            primRev.setAsistio(asistencia);
        }

        if(radioPendiente.isChecked()){
            String estado = "Pendiente";
            primRev.setEstadoprimerrevision(estado);
        }else if(radioTerminada.isChecked()){
            String estado = "Terminada";
            primRev.setEstadoprimerrevision(estado);
        }

        helper.abrir();
        String estado = helper.actualizar(primRev);
        helper.cerrar();

        Toast.makeText(this, estado, Toast.LENGTH_SHORT).show();
    }

    public void limpiarTexto(View v){
        editAsignatura.setText("");
        editCiclo.setText("");
        editNumEval.setText("");
        editDocente.setText("");
        editCarnet.setText("");
        editNotaFinal.setText("");
        editObservaciones.setText("");
        spinTipoEval.setSelection(0);
        spinMotCambNota.setSelection(0);
        spinTipoGrupo.setSelection(0);
        radioSi.setChecked(true);
        radioNo.setChecked(false);
        radioTerminada.setChecked(true);
        radioPendiente.setChecked(false);
    }
}
