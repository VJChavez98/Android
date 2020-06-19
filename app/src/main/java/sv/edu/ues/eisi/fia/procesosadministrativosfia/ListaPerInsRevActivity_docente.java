package sv.edu.ues.eisi.fia.procesosadministrativosfia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import sv.edu.ues.eisi.fia.procesosadministrativosfia.adaptadores.ListaPerInsRevAdapter;

public class ListaPerInsRevActivity_docente extends Activity {

    ArrayList<PeriodoInscripcionRevision> listaPeriodos;
    RecyclerView recyclerViewPeriodos;

    String[] docente= null;

    ControladorBase helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_per_ins_rev_docente);


        helper = new ControladorBase(this);

        listaPeriodos = new ArrayList<>();

        recyclerViewPeriodos = (RecyclerView) findViewById(R.id.recyclerPeriodo);
        recyclerViewPeriodos.setLayoutManager(new LinearLayoutManager(this));

        consultarPeriodosIncripcionRevision();
        if (!listaPeriodos.isEmpty()) {

            ListaPerInsRevAdapter adapter = new ListaPerInsRevAdapter(listaPeriodos, docente[0], docente[1]);

            recyclerViewPeriodos.setAdapter(adapter);
        }
        else{
            //AQUI LE CAMBIE EL TOAST
            Toast.makeText(getApplicationContext(), "LISTA DE REVISIONES VACIA", Toast.LENGTH_SHORT).show();
        }
    }


    private void consultarPeriodosIncripcionRevision() {

        SQLiteDatabase db = helper.abrirLeer();

        PeriodoInscripcionRevision periodo= null;

        String sql= "SELECT * FROM periodoinscripcionrevision";

        Cursor cursor = db.rawQuery(sql, null);

        //el if que encerraba a este while debes eliminarlo
        while (cursor.moveToNext()) {
            int i = 0;
            periodo = new PeriodoInscripcionRevision();

            periodo.setFechaDesde(cursor.getString(0));
            periodo.setFechaHasta(cursor.getString(1));
            periodo.setFechaRevision(cursor.getString(2));
            periodo.setHoraRevision(cursor.getString(3));
            periodo.setTipoRevision(cursor.getString(4));
            periodo.setCodDocente(cursor.getString(5));
            periodo.setCodLocal(cursor.getString(6));
            periodo.setCodAsignatura(cursor.getString(7));
            periodo.setCodCiclo(cursor.getString(8));
            periodo.setCodTipoEval(cursor.getString(9));
            periodo.setNumeroEval(cursor.getInt(10));

            recuperarDocente(cursor.getString(5));

            listaPeriodos.add(periodo);

        }


    }



    private void recuperarDocente(String s){

        SQLiteDatabase db = helper.abrirLeer();

        String[] parametros = {s};

        for (int i=0; i<=listaPeriodos.size(); i++){

            Cursor cursor = db.rawQuery("SELECT nombredocente, apellidodocente FROM docente WHERE coddocente = ?" , parametros);

            cursor.moveToFirst();

            String[] doc= {cursor.getString(0), cursor.getString(1)};

            asignarDocente(doc);

        }

    }


    private void asignarDocente(String[] s){

        docente=s;
    }
}
