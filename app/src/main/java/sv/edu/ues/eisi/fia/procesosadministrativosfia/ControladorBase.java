package sv.edu.ues.eisi.fia.procesosadministrativosfia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.util.ArrayList;

public class ControladorBase {


    private static final String[] camposUsuario = {"username", "password", "nombre_usuario"};
    private static final String[] camposEvaluacion = {"codasignatura", "codciclo", "codtipoeval", "numeroeval", "fechaevaluacion"};
    private static final String[] camposLocal = {"codlocal", "nomlocal", "ubicacionlocal"};
    private static final String[] camposPerInscRev = {"fechadesde", "fechahasta", "fecharevision", "horarevision", "codtiporevision", "coddocente", "codlocal", "codasignatura", "codtipoeval", "codciclo", "numeroeval"};
    private static final String[] camposSolRev = {"fechasolicitudrevision", "notaantesrevision", "codtipogrupo", "numerogrupo", "motivorevision", "carnet", "codasignatura", "codciclo", "codtipoeval", "numeroeval", "codtiporevision"};



    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ControladorBase(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String NOMBRE_BASE = "ProcesosAdmin.s3db";
        private static final int VERSION = 1;

        public DatabaseHelper(@Nullable Context context) {
            super(context, NOMBRE_BASE, null, VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("CREATE TABLE usuario(username VARCHAR(7) NOT NULL PRIMARY KEY, password VARCHAR(10), nombre_usuario VARCHAR (256));");

                db.execSQL("CREATE TABLE asignatura(codasignatura VARCHAR(6) NOT NULL PRIMARY KEY, nomasignatura VARCHAR(30) NOT NULL, unidadesval VARCHAR(1));");
                db.execSQL("CREATE TABLE docente(coddocente VARCHAR(10) NOT NULL PRIMARY KEY, nombredocente VARCHAR(50) NOT NULL, apellidodocente VARCHAR(50));");
                db.execSQL("CREATE TABLE ciclo(codciclo VARCHAR(5) NOT NULL PRIMARY KEY, fechadesde DATE, fechahasta DATE);");

                db.execSQL("CREATE TABLE tipoevaluacion(codtipoeval VARCHAR(2) NOT NULL PRIMARY KEY, nomtipoeval VARCHAR(20) NOT NULL);");
                db.execSQL("CREATE TABLE tiporevision(codtiporevision VARCHAR(2) PRIMARY KEY NOT NULL, nomtiporevision VARCHAR(30) NOT NULL);");
                db.execSQL("CREATE TABLE local(codlocal VARCHAR(10) NOT NULL PRIMARY KEY, nomlocal VARCHAR(30) NOT NULL, ubicacionlocal VARCHAR(30));");
                db.execSQL("CREATE TABLE evaluacion(codasignatura VARCHAR(6) NOT NULL, codciclo VARCHAR(5) NOT NULL, codtipoeval VARCHAR(2) NOT NULL, numeroeval INTEGER NOT NULL, fechaevaluacion DATE, " +
                        "PRIMARY KEY(codasignatura, codciclo, codtipoeval, numeroeval));");

                db.execSQL("CREATE TABLE periodoinscripcionrevision(fechadesde DATE, fechahasta DATE, fecharevision DATE, horarevision TIME, " +
                        "codtiporevision VARCHAR(2) NOT NULL, coddocente VARCHAR(10) NOT NULL, codlocal VARCHAR(10) NOT NULL, " +
                        "codasignatura VARCHAR(6) NOT NULL, codciclo VARCHAR(5) NOT NULL, codtipoeval VARCHAR(2) NOT NULL, numeroeval INTEGER NOT NULL, " +
                        "PRIMARY KEY(codtiporevision, codasignatura, codciclo, codtipoeval, numeroeval));");

                db.execSQL("CREATE TABLE primerrevision(estadoprimerrevision VARCHAR(15) NOT NULL, notadespuesprimerarevision REAL NOT NULL, asistio VARCHAR(2) NOT NULL, observacionesprimerarev VARCHAR(200), coddocente VARCHAR(10) NOT NULL,  codtipogrupo VARCHAR(2) NOT NULL," +
                        "carnet VARCHAR(7) NOT NULL, codasignatura VARCHAR(6) NOT NULL, codciclo VARCHAR(5) NOT NULL, codtipoeval VARCHAR(2) NOT NULL, numeroeval INTEGER NOT NULL, codtiporevision VARCHAR(2) NOT NULL, codmotivocambionota VARCHAR(10) NOT NULL," +
                        "PRIMARY KEY(coddocente, carnet, codtiporevision, codasignatura, codciclo, codtipoeval, numeroeval, codtipogrupo));");

                db.execSQL("CREATE TABLE segundarevision(codmotivocambionota VARCHAR(10) NOT NULL, codtipogrupo VARCHAR(2) NOT NULL, coddocente VARCHAR(10) NOT NULL, notafinalsegundarev REAL NOT NULL, observacionessegundarev VARCHAR(200), " +
                        "carnet VARCHAR(7) NOT NULL, codasignatura VARCHAR(6) NOT NULL, codciclo VARCHAR(5) NOT NULL, codtipoeval VARCHAR(2) NOT NULL, numeroeval INTEGER NOT NULL, codtiporevision VARCHAR(2) NOT NULL," +
                        "PRIMARY KEY(codtiporevision, codasignatura, codtipoeval, numeroeval, codciclo, carnet));");


                db.execSQL("CREATE TABLE motivocambionota(codmotivocambionota VARCHAR(10) NOT NULL PRIMARY KEY, motivo VARCHAR(200) NOT NULL);");

                db.execSQL("CREATE TABLE solicitudrevision(fechasolicitudrevision DATE, notaantesrevision REAL NOT NULL, codtipogrupo VARCHA(2) NOT NULL,numerogrupo INTEGER NOT NULL, motivorevision VARCHAR(200)," +
                        "carnet VARCHAR(7) NOT NULL, codasignatura VARCHAR(6) NOT NULL, codciclo VARCHAR(5) NOT NULL, codtipoeval VARCHAR(2) NOT NULL, numeroeval INTEGER NOT NULL, codtiporevision VARCHAR(2) NOT NULL," +
                        "PRIMARY KEY(carnet, codtipogrupo, codtiporevision, codasignatura, codciclo, codtipoeval, numeroeval));");

                db.execSQL("CREATE TABLE tipogrupo(codtipogrupo VARCHAR(2) NOT NULL PRIMARY KEY, nombretipogrupo VARCHAR(50) NOT NULL);");

                db.execSQL("CREATE TABLE estudiante(carnet VARCHAR(7) NOT NULL PRIMARY KEY, nombreestudiante VARCHAR(50) NOT NULL, apellidoestudiante VARCHAR(50) NOT NULL, carrera VARCHAR(50) NOT NULL);");


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //UPDATE DATABASE COMMANDS
        }
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return;
    }

    public SQLiteDatabase abrirLeer() throws SQLException{
        db = DBHelper.getReadableDatabase();
        return db;
    }

    public void cerrar() {
        DBHelper.close();
    }



    public boolean consultarUsuario(String username, String password) {
        String[] id = {username};
        Cursor cursor = db.rawQuery("select * from usuario where username ='" + username + "' and password ='" + password+"';", null);
        if (cursor.moveToFirst() == true) {
            String user = cursor.getString(0);
            String pass = cursor.getString(1);
            cerrar();
            if (user.equals(username) && pass.equals(password)) {
                return true;
            }
            else return false;
        }else return false;
    }



    public String insertar(Usuario user){
        String regAfectados = "Registro insertado Nª= ";
        long contador = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("nombre_usuario", user.getNombreUsuario());
        contador=db.insert("usuario",null,contentValues);
        if (contador == -1 || contador==0){
            regAfectados = "Error al Insertar el registro, Registro duplicado. Verificar inserción";
        }else {
            regAfectados=regAfectados+contador;
        }
        return regAfectados;
    }

    public String insertar(Asignatura asignatura){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        ContentValues asig = new ContentValues();
        asig.put("codasignatura", asignatura.getCodasignatura());
        asig.put("nomasignatura", asignatura.getNomasignatura() );
        asig.put("unidadesval", asignatura.getUnidadesval());
        contador = db.insert("asignatura", null, asig);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Ciclo ciclo){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        ContentValues cic = new ContentValues();
        cic.put("codciclo", ciclo.getCodciclo());
        cic.put("fechadesde", String.valueOf(ciclo.getFechadesde()));
        cic.put("fechahasta", String.valueOf(ciclo.getFechahasta()));
        contador = db.insert("ciclo", null, cic);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Docente docente){
        String regInsertados = "Regitro No. = ";
        long contador = 0;

        ContentValues doc = new ContentValues();
        doc.put("coddocente", docente.getCoddocente());
        doc.put("nombredocente", docente.getNomdocente());
        doc.put("apellidodocente", docente.getApellidodocente());
        contador = db.insert("docente", null, doc);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Estudiante estudiante){
        String regInsertados = "Regitro No. = ";
        long contador = 0;

        ContentValues est = new ContentValues();
        est.put("carnet", estudiante.getCarnet());
        est.put("nombreestudiante", estudiante.getNombre());
        est.put("apellidoestudiante", estudiante.getApellido());
        est.put("carrera", estudiante.getCarrera());
        contador = db.insert("estudiante", null, est);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(MotivoCambioNota motivo){
        String regInsertados = "Registro No. = ";
        long contador = 0;

        ContentValues mot = new ContentValues();
        mot.put("codmotivocambionota", motivo.getCodmotivocambionota());
        mot.put("motivo", motivo.getMotivo());
        contador = db.insert("motivocambionota", null, mot);

        if (contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(SolicitudRevision solicitud){
        String regInsertados = "Registro No. = ";
        long contador = 0;

        if(verificarIntegridadReferencial(solicitud, 31)){

            ContentValues sol = new ContentValues();
            sol.put("fechasolicitudrevision", solicitud.getFechasolicitudrevision());
            sol.put("notaantesrevision", solicitud.getNotaantesrevision());
            sol.put("numerogrupo", solicitud.getNumerogrupo());
            sol.put("codtipogrupo", solicitud.getCodtipogrupo());
            sol.put("motivorevision", solicitud.getMotivorevision());
            sol.put("carnet", solicitud.getCarnet());
            sol.put("codasignatura", solicitud.getCodasignatura());
            sol.put("codciclo", solicitud.getCodciclo());
            sol.put("codtipoeval", solicitud.getCodtipoeval());
            sol.put("numeroeval", solicitud.getNumeroeval());
            sol.put("codtiporevision", solicitud.getCodtiporevision());
            contador = db.insert("solicitudrevision", null, sol);

        }

        if (contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(TipoRevision tipoRevision){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        ContentValues tiprev = new ContentValues();
        tiprev.put("codtiporevision", tipoRevision.getCodTipoRev());
        tiprev.put("nomtiporevision", tipoRevision.getNomTipoRev());
        contador = db.insert("tiporevision", null, tiprev);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Isercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }
//************************************************************************
    public String insertar(TipoGrupo tipoGrupo){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        ContentValues tipgru = new ContentValues();
        tipgru.put("codtipogrupo", tipoGrupo.getCodtipogrupo());
        tipgru.put("nombretipogrupo", tipoGrupo.getNombreTipoGrupo());
        contador = db.insert("tipogrupo", null, tipgru);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Isercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(TipoEvaluacion tipoEval){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        ContentValues teval = new ContentValues();
        teval.put("codtipoeval", tipoEval.getCodTipoEval());
        teval.put("nomtipoeval", tipoEval.getNomTipoEval());
        contador = db.insert("tipoevaluacion", null, teval);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Evaluacion evaluacion){
        String regInsertados = "Registro Insertado No. = ";
        long contador = 0;

        if(verificarIntegridadReferencial(evaluacion, 1)){
            ContentValues evaluaciones = new ContentValues();
            evaluaciones.put("codasignatura", evaluacion.getCodAsignatura());
            evaluaciones.put("codciclo", evaluacion.getCodCiclo());
            evaluaciones.put("codtipoeval", evaluacion.getCodTipoEval());
            evaluaciones.put("fechaevaluacion", evaluacion.getFechaEvaluacion());
            evaluaciones.put("numeroeval", evaluacion.getNumeroEvaluacion());
            contador = db.insert("evaluacion", null, evaluaciones);
        }

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else {
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(Local local){
        String regInsertados = "Registro No. = ";
        long contador = 0;

        ContentValues locales = new ContentValues();
        locales.put("codlocal", local.getCodlocal());
        locales.put("nomlocal", local.getNomlocal());
        locales.put("ubicacionlocal", local.getUbicacionlocal());
        contador = db.insert("local", null, locales);

        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }

    public String insertar(PeriodoInscripcionRevision perInscRev){
        String regInsertados = "Registro No. = ";
        long contador = 0;

        if(verificarIntegridadReferencial(perInscRev, 3)){
            ContentValues inscripciones = new ContentValues();
            inscripciones.put("fechadesde", perInscRev.getFechaDesde());
            inscripciones.put("fechahasta", perInscRev.getFechaHasta());
            inscripciones.put("fecharevision", perInscRev.getFechaRevision());
            inscripciones.put("horarevision", perInscRev.getHoraRevision());
            inscripciones.put("coddocente", perInscRev.getCodDocente());
            inscripciones.put("codasignatura", perInscRev.getCodAsignatura());
            inscripciones.put("codciclo", perInscRev.getCodCiclo());
            inscripciones.put("codtipoeval", perInscRev.getCodTipoEval());
            inscripciones.put("codtiporevision", perInscRev.getTipoRevision());
            inscripciones.put("codlocal", perInscRev.getCodLocal());
            inscripciones.put("numeroeval", perInscRev.getNumeroEval());
            contador = db.insert("periodoinscripcionrevision", null, inscripciones);
        }
        if(contador == -1 || contador == 0){
            regInsertados = "Error al Insertar el registro, Registro Duplicado. Verificar Insercion";
        }else{
            regInsertados = regInsertados + contador;
        }

        return regInsertados;
    }


    public Asignatura consultarNomAsignatura(String codAsignatura){
        String[] id = {codAsignatura};
        String[] camposAsigConsul = {"nomasignatura"};
        Cursor cursor = db.query("asignatura", camposAsigConsul, "codasignatura = ?", id, null, null, null);
        if(cursor.moveToFirst()){
            Asignatura asig = new Asignatura();
            asig.setNomasignatura(cursor.getString(0));
            return asig;
        }else{
            return null;
        }
    }

    public Local consultarLocal(String codLocal){
        String[] id = {codLocal};
        Cursor cursor = db.query("local", camposLocal, "codlocal = ?", id, null, null, null);
        if(cursor.moveToFirst()){
            Local local = new Local();
            local.setCodlocal(cursor.getString(0));
            local.setNomlocal(cursor.getString(1));
            local.setUbicacionlocal(cursor.getString(2));
            return local;
        }else{
            return null;
        }
    }

    public Evaluacion consultarEvaluacion(String codAsignatura, String codCiclo, String codTipoEval, int numEval){
        String[] id = {codAsignatura, codCiclo, codTipoEval, String.valueOf(numEval)};
        Cursor cursor = db.query("evaluacion", camposEvaluacion, "codasignatura = ? AND codciclo = ? AND codtipoeval = ? AND numeroeval = ?", id, null, null, null);
        if(cursor.moveToFirst()){
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setCodAsignatura(cursor.getString(0));
            evaluacion.setCodCiclo(cursor.getString(1));
            evaluacion.setCodTipoEval(cursor.getString(2));
            evaluacion.setNumeroEvaluacion(Integer.parseInt(cursor.getString(3)));
            evaluacion.setFechaEvaluacion(cursor.getString(4));
            return evaluacion;
        }else{
            return null;
        }
    }

    public PeriodoInscripcionRevision consultarPeriodoInscripcion(String codTipoRev, String codAsignatura, String codTipoEva, String codCiclo, int numEva){
        String[] id = {codTipoRev, codAsignatura, codTipoEva, codCiclo, String.valueOf(numEva)};
        Cursor cursor = db.query("periodoinscripcionrevision", camposPerInscRev, "codtiporevision = ? AND codasignatura = ? AND codtipoeval = ? AND codciclo = ? AND numeroeval = ?", id, null, null, null);
        if(cursor.moveToFirst()){
            PeriodoInscripcionRevision perInsRev = new PeriodoInscripcionRevision();
            perInsRev.setFechaDesde(cursor.getString(0));
            perInsRev.setFechaHasta(cursor.getString(1));
            perInsRev.setFechaRevision(cursor.getString(2));
            perInsRev.setHoraRevision(cursor.getString(3));
            perInsRev.setTipoRevision(cursor.getString(4));
            perInsRev.setCodDocente(cursor.getString(5));
            perInsRev.setCodLocal(cursor.getString(6));
            perInsRev.setCodAsignatura(cursor.getString(7));
            perInsRev.setCodCiclo(cursor.getString(8));
            perInsRev.setCodTipoEval(cursor.getString(9));
            perInsRev.setNumeroEval(Integer.parseInt(cursor.getString(10)));
            return perInsRev;
        }else{
            return null;
        }
    }



    public String actualizar(Evaluacion evaluacion){
        if(verificarIntegridadReferencial(evaluacion, 2)){
            String[] id = {evaluacion.getCodAsignatura(), evaluacion.getCodCiclo(), evaluacion.getCodTipoEval(), String.valueOf(evaluacion.getNumeroEvaluacion())};
            ContentValues cv = new ContentValues();
            cv.put("fechaevaluacion", evaluacion.getFechaEvaluacion());
            db.update("evaluacion", cv, "codasignatura = ? AND codciclo = ? AND codtipoeval = ? AND numeroeval = ?",id);
            return "Registro Actualizado correctamente";
        }else{
            return "Registro no Existe";
        }
    }

    public String actualizar(Local local){
        String[] id = {local.getCodlocal()};
        ContentValues cv = new ContentValues();
        cv.put("nomlocal", local.getNomlocal());
        cv.put("ubicacionlocal", local.getUbicacionlocal());
        db.update("local", cv, "codlocal = ? ", id);
        return "Registro Actualizado correctamente";
    }

    public String actualizar (PeriodoInscripcionRevision perInscRev){
        if(verificarIntegridadReferencial(perInscRev, 4)){
            String[] id = {perInscRev.getTipoRevision(), perInscRev.getCodAsignatura(), perInscRev.getCodTipoEval(), perInscRev.getCodCiclo(), String.valueOf(perInscRev.getNumeroEval())};
            ContentValues cv = new ContentValues();
            cv.put("coddocente", perInscRev.getCodDocente());
            cv.put("codlocal", perInscRev.getCodLocal());
            cv.put("fechadesde", perInscRev.getFechaDesde());
            cv.put("fechahasta", perInscRev.getFechaHasta());
            cv.put("fecharevision", perInscRev.getFechaRevision());
            cv.put("horarevision", perInscRev.getHoraRevision());
            db.update("periodoinscripcionrevision", cv, "codtiporevision = ? AND codasignatura = ? AND codtipoeval = ? AND codciclo = ?  AND numeroeval = ?", id);
            return "Registro Actualizado correctamente";
        }else{
            return "Registro no Existe";
        }
    }



    public String eliminar(Evaluacion evaluacion){
        String regAfectados = "Filas afectadas = ";
        int contador = 0;

        String where = "codasignatura = '"+evaluacion.getCodAsignatura()+"'";
        where = where + "AND codciclo = '"+evaluacion.getCodCiclo()+"'";
        where = where + "AND codtipoeval = '"+evaluacion.getCodTipoEval()+"'";
        where = where + "AND numeroeval = '"+evaluacion.getNumeroEvaluacion()+"'";
        contador += db.delete("evaluacion", where, null);
        regAfectados += contador;
        return regAfectados;
    }

    public String eliminar(Local local){
        String regAfectados = "Filas afectadas = ";
        int contador = 0;

        String where = "codlocal = '"+local.getCodlocal()+"'";
        where = where + "AND nomlocal = '"+local.getNomlocal()+"'";
        contador += db.delete("local", where, null);
        regAfectados += contador;
        return regAfectados;
    }

    public String eliminar(PeriodoInscripcionRevision perInscRev){
        String regAfectados = "Filas afectadas = ";
        int contador = 0;

        String where = "codtiporevision = '"+perInscRev.getTipoRevision()+"'";
        where = where + "AND codasignatura = '"+perInscRev.getCodAsignatura()+"'";
        where = where + "AND codciclo = '"+perInscRev.getCodCiclo()+"'";
        where = where + "AND codtipoeval = '"+perInscRev.getCodTipoEval()+"'";
        where = where + "AND numeroeval = '"+perInscRev.getNumeroEval()+"'";
        contador += db.delete("periodoinscripcionrevision", where, null);
        regAfectados += contador;
        return regAfectados;
    }

    public String eliminar(SolicitudRevision solRev){//********************************************************************************************************************
        String regAfectados = "Solicitud Eliminada, Filas afectadas = ";
        int contador = 0;

        if(verificarIntegridadReferencial(solRev, 32)){

            String nosepuede = "Error, existen registros de esta Solicitud en otras tablas.";
            return nosepuede;
        }

        String where = "carnet = '"+solRev.getCarnet()+"'";
        where = where + " AND codtipogrupo = '"+solRev.getCodtipogrupo()+"'";
        where = where + " AND codtiporevision = '"+solRev.getCodtiporevision()+"'";
        where = where + " AND codasignatura = '"+solRev.getCodasignatura()+"'";
        where = where + " AND codciclo = '"+solRev.getCodciclo()+"'";
        where = where + " AND codtipoeval = '"+solRev.getCodtipoeval()+"'";
        where = where + " AND numeroeval = '"+solRev.getNumeroeval()+"'";
        contador += db.delete("solicitudrevision", where, null);

        if (contador==0){
            return  "Esta Solicitud de Revision No Existe o Campos Incompletos";
        }

        regAfectados += contador;
        return regAfectados;
    }


    public String actualizar(SolicitudRevision solRev) {

        if (verificarIntegridadReferencial(solRev, 33)) {

            String[] id = {solRev.getCarnet(), solRev.getCodtipogrupo(), solRev.getCodtiporevision(), solRev.getCodasignatura(), solRev.getCodciclo(), solRev.getCodtipoeval(), String.valueOf(solRev.getNumeroeval())};
            ContentValues cv = new ContentValues();
            cv.put("notaantesrevision", solRev.getNotaantesrevision());
            cv.put("numerogrupo", solRev.getNumerogrupo());
            cv.put("fechasolicitudrevision", solRev.getFechasolicitudrevision());
            cv.put("motivorevision", solRev.getMotivorevision());
            db.update("solicitudrevision", cv, "carnet = ? AND codtipogrupo = ? AND codtiporevision = ? AND codasignatura = ?  AND codciclo = ? AND codtipoeval = ? AND numeroeval = ?", id);
            return "Registro Actualizado correctamente";

        } else {
            return "Registro no Existe";
        }

    }


    public SolicitudRevision consultarSolicitudRevision(String carnet, String codtipogrupo, String codtiporevision, String codtipoeval, String codasignatura, String numeroeval, String codciclo) {

        String[] id = {carnet, codtipogrupo, codtiporevision, codasignatura, codciclo, codtipoeval, numeroeval};
        String where = "carnet=? AND codtipogrupo=? AND codtiporevision=? AND codasignatura=? AND codciclo=? AND codtipoeval =? AND numeroeval =?";

        Cursor cursor = db.query("solicitudrevision", camposSolRev, where, id, null, null, null);

        if (cursor.moveToFirst()) {

            SolicitudRevision solRev = new SolicitudRevision();

            solRev.setFechasolicitudrevision(cursor.getString(0));
            solRev.setNotaantesrevision(cursor.getFloat(1));
            solRev.setCodtipogrupo(cursor.getString(2));
            solRev.setNumerogrupo(cursor.getInt(3));
            solRev.setMotivorevision(cursor.getString(4));
            solRev.setCarnet(cursor.getString(5));
            solRev.setCodasignatura(cursor.getString(6));
            solRev.setCodciclo(cursor.getString(7));
            solRev.setCodtipoeval(cursor.getString(8));
            solRev.setNumeroeval(cursor.getInt(9));
            solRev.setCodtiporevision(cursor.getString(10));
            return solRev;

        } else {

            return null;
        }
    }


    public boolean verificarIntegridadReferencial(Object dato, int relacion) throws SQLException{
        switch (relacion) {
            case 1: {
                //Verificar que al Insertar Evaluacion exista el TipoEvaluacion, Asignatura y Ciclo
                Evaluacion evaluacion = (Evaluacion) dato;
                String[] id1 = {evaluacion.getCodAsignatura()};
                String[] id2 = {evaluacion.getCodCiclo()};
                String[] id3 = {evaluacion.getCodTipoEval()};
                abrir();

                Cursor cursor1 = db.query("asignatura", null, "codasignatura = ?", id1, null, null, null);
                Cursor cursor2 = db.query("ciclo", null, "codciclo = ?", id2, null, null, null);
                Cursor cursor3 = db.query("tipoevaluacion", null, "codtipoeval = ?", id3, null, null, null);

                if (cursor1.moveToFirst() && cursor2.moveToFirst() && cursor3.moveToFirst()) {
                    //Se encontraron los datos
                    return true;
                }

                return false;
            }
            case 2: {
                //verificar que al Modificar Evaluacion exista el TipoEvaluacion, Asignatura, Ciclo y Numero de Evaluacion
                Evaluacion evaluacion = (Evaluacion) dato;
                String[] ids = {evaluacion.getCodAsignatura(), evaluacion.getCodCiclo(), evaluacion.getCodTipoEval(), String.valueOf(evaluacion.getNumeroEvaluacion())};
                abrir();

                Cursor c = db.query("evaluacion", null, "codasignatura = ? AND codciclo = ? AND codtipoeval = ? AND numeroeval = ?", ids, null, null, null);

                if (c.moveToFirst()) {
                    return true;
                }
                return false;
            }
            case 3: {
                //Verificar que al Insertar PeriodoInscripcionRevision exista Asignatura, Ciclo, TipoEvaluacion, NumeroEvaluacion, Docente, Local, TipoRevision
                PeriodoInscripcionRevision perInscRev = (PeriodoInscripcionRevision) dato;
                String[] id1 = {perInscRev.getCodAsignatura()};
                String[] id2 = {perInscRev.getCodCiclo()};
                String[] id3 = {perInscRev.getCodTipoEval()};
                String[] id4 = {String.valueOf(perInscRev.getNumeroEval())};
                String[] id5 = {perInscRev.getCodDocente()};
                String[] id6 = {perInscRev.getCodLocal()};
                String[] id7 = {perInscRev.getTipoRevision()};
                abrir();

                Cursor cursor1 = db.query("asignatura", null, "codasignatura = ?", id1, null, null, null);
                Cursor cursor2 = db.query("ciclo", null, "codciclo = ?", id2, null, null, null);
                Cursor cursor3 = db.query("tipoevaluacion", null, "codtipoeval = ?", id3, null, null, null);
                Cursor cursor4 = db.query("evaluacion", null, "numeroeval = ?", id4, null, null, null);
                Cursor cursor5 = db.query("docente", null, "coddocente = ?", id5, null, null, null);
                Cursor cursor6 = db.query("local", null, "codlocal = ?", id6, null, null, null);
                Cursor cursor7 = db.query("tiporevision", null, "codtiporevision = ?", id7, null, null, null);

                if (cursor1.moveToFirst() && cursor2.moveToFirst() && cursor3.moveToFirst() && cursor4.moveToFirst() && cursor5.moveToFirst() && cursor6.moveToFirst() && cursor7.moveToFirst()) {
                    return true;
                }
            }
            case 4: {
                //Verificar que al modificar el PeriodoInscripcionRevision exista Asignatura, Ciclo, TipoEvaluacion, NumeroEvaluacion y TipoRevision
                PeriodoInscripcionRevision perInscRev = (PeriodoInscripcionRevision) dato;
                String[] ids = {perInscRev.getCodAsignatura(), perInscRev.getCodCiclo(), perInscRev.getCodTipoEval(), String.valueOf(perInscRev.getNumeroEval()), perInscRev.getTipoRevision()};
                abrir();


                Cursor c = db.query("periodoinscripcionrevision", null, "codasignatura = ? AND codciclo = ? AND codtipoeval = ? AND numeroeval = ? AND codtiporevision = ?", ids, null, null, null);

                if (c.moveToFirst()) {
                    return true;
                }
                return false;
            }
            case 31: {
                //Verificar que al Insertar SolicitudRevision exista Asignatura, Ciclo, TipoEvaluacion, NumeroEvaluacion, Estudiante, TipoGrupo, TipoRevision
                SolicitudRevision solicitud = (SolicitudRevision) dato;
                String[] id1 = {solicitud.getCodasignatura()};
                String[] id2 = {solicitud.getCodciclo()};
                String[] id3 = {solicitud.getCodtipoeval()};
                String[] id4 = {String.valueOf(solicitud.getNumeroeval())};
                String[] id5 = {solicitud.getCodtipogrupo()};
                String[] id6 = {solicitud.getCarnet()};
                String[] id7 = {solicitud.getCodtiporevision()};
                abrir();

                Cursor cursor1 = db.query("asignatura", null, "codasignatura = ?", id1, null, null, null);
                Cursor cursor2 = db.query("ciclo", null, "codciclo = ?", id2, null, null, null);
                Cursor cursor3 = db.query("tipoevaluacion", null, "codtipoeval = ?", id3, null, null, null);
                Cursor cursor4 = db.query("evaluacion", null, "numeroeval = ?", id4, null, null, null);
                Cursor cursor5 = db.query("tipogrupo", null, "codtipogrupo = ?", id5, null, null, null);
                Cursor cursor6 = db.query("estudiante", null, "carnet = ?", id6, null, null, null);
                Cursor cursor7 = db.query("tiporevision", null, "codtiporevision = ?", id7, null, null, null);

                if (cursor1.moveToFirst() && cursor2.moveToFirst() && cursor3.moveToFirst() && cursor4.moveToFirst() && cursor5.moveToFirst() && cursor6.moveToFirst() && cursor7.moveToFirst()) {
                    return true;
                }

            }
            case 32: {

                //Verificar que no haya registro de solicitud revision en otras tablas antes de eliminar
                SolicitudRevision solRev = (SolicitudRevision) dato;

                //carnet, codtipogrupo, codtiporevision, codasignatura, codciclo, codtipoeval, numeroeval

                String[] idrecu = new String[]{"carnet","codtipogrupo","codtiporevision","codasignatura","codciclo","codtipoeval","numeroeval"};
                String[] parametros = {solRev.getCarnet(), solRev.getCodtipogrupo(), solRev.getCodtiporevision(), solRev.getCodasignatura(), solRev.getCodciclo(), solRev.getCodtipoeval(), String.valueOf(solRev.getNumeroeval())};
                String where = "carnet =? AND codtipogrupo =?  AND codtiporevision =? AND codasignatura =? AND codciclo =? AND codtipoeval =? AND numeroeval =?";

                Cursor c1 = db.query(true, "primerrevision", idrecu, where, parametros, null, null, null, null);
                Cursor c2 = db.query(true, "segundarevision", idrecu, where, parametros, null, null, null, null);

                if(c1.moveToFirst() || c2.moveToFirst()){
                    return true;
                }
                else {
                    return false;
                }


            }
            case 33: {
                //Verificar que al modificar una SolicitudRevision exista el registro
                SolicitudRevision solRev = (SolicitudRevision) dato;
                String[] idrecu = new String[]{"carnet","codtipogrupo","codtiporevision","codasignatura","codciclo","codtipoeval","numeroeval"};
                String where = "carnet =? AND codtipogrupo =?  AND codtiporevision =? AND codasignatura =? AND codciclo =? AND codtipoeval =? AND numeroeval =?";
                String[] ids = {solRev.getCarnet(), solRev.getCodtipogrupo(), solRev.getCodtiporevision(), solRev.getCodasignatura(), solRev.getCodciclo(), solRev.getCodtipoeval(), String.valueOf(solRev.getNumeroeval())};
                abrir();

                Cursor c = db.query("solicitudrevision", idrecu, where, ids, null, null, null);

                if (c.moveToFirst()) {
                    return true;
                } else return false;

            }
            default:
                return false;
        }
    }

    public String LlenarDatos(){
        final String[] usersId = {"CM17048","RM17039","AG17023","MM14030","PR17017"};
        final String[] names = {"Victor","Shaky","Daniel","Cristian","Roberto"};
        final String[] userPass = {"0123456789","0123456789","0123456789","0123456789","0123456789"};

        final String[] TTEcodtipoeval = {"EP", "ED", "EL"};
        final String[] TTEnomtipoeval = {"Examen Parcial", "Examen Discusion", "Examen Laboratorio"};

        final String[] TTGcodtipoGrupo = {"GT", "GD", "GL"};
        final String[] TTGnomtipogrupo = {"Grupo Teorico", "Grupo de Discusion", "Grupo de Laboratorio"};

        final String[] TRcodrevision = {"PR", "SR"};
        final String[] TRnomrevision = {"Primera Revision", "Segunda Revision"};


        final String[] TAcodasignatura = {"MAT115", "FIR115"};
        final String[] TAnomasignatura = {"Matematicas I", "Fisicas I"};
        final String[] TAunidadesval = {"4", "4"};

        final String[] TCcodciclo = {"12020", "22020"};
        final Date[]   TCfechadesde = {Date.valueOf("2020-02-20"), Date.valueOf("2020-10-08")};
        final Date[]   TCfechahasta = {Date.valueOf("2020-06-20"), Date.valueOf("2020-12-20")};

        final String[] TDcoddocente = {"CV00001", "MN00002"};
        final String[] TDnombredocente = {"Rudy Wilfredo", "Boris Alexander"};
        final String[] TDapellidodocente = {"Chicas Villegas", "Montano Navarrete"};

        final String[] TEcarnet = {"CM17048","RM17039","AG17023","MM14030","PR17017"};
        final String[] TEnombreestudiante = {"Victor","Shaky","Daniel","Cristian","Roberto"};
        final String[] TEapellidoestudiante = {"Lopes","Renderos","Amaya","Melendez","Paz"};
        final String[] TEcarrera = {"Ingenieria de Sistemas Informaticos", "Ingenieria de Sistemas Informaticos", "Ingenieria de Sistemas Informaticos", "Ingenieria de Sistemas Informaticos", "Ingenieria de Sistemas Informaticos"};


        final String[] TMCNcodmotivocambnota = {"ERRSUM", "ERRELPR", "ERRCAL"};
        final String[] TMCNmotivo = {"Error de suma", "Error de elaboracion de preguntas", "Error de calificacion"};

        final String[] TSoRfechasolicitudrev = {"2020-05-10", "2020-05-15"};
        final String[] TSoRmotivorevision = {"Mal calculo en la suma", "Pregunta incoherente"};
        final String[] TSoRcarnet = {"RM17039", "PR17017"};
        final String[] TSoRcodasignatura = {"MAT115", "FIR115"};
        final String[] TSoRcodcilo = {"12020", "22020"};
        final String[] TSoRcodtipoeval = {"EP", "ED"};
        final String[] TSoRcodtiporev = {"PR", "SR"};
        final float[] TSoRnotaantesrev = {Float.parseFloat("7.5"), Float.parseFloat("6.0")};
        final int[] TSoRnumerogrupo = {Integer.parseInt("1"), Integer.parseInt("3")};
        final int[] TSoRcodnumeroeval = {Integer.parseInt("1"), Integer.parseInt("2")};

        abrir();

        db.execSQL("DELETE FROM usuario;");
        db.execSQL("DELETE FROM asignatura");
        db.execSQL("DELETE FROM ciclo");
        db.execSQL("DELETE FROM tipoevaluacion");
        db.execSQL("DELETE FROM tiporevision");
        db.execSQL("DELETE FROM docente");
        db.execSQL("DELETE FROM estudiante");
        db.execSQL("DELETE FROM motivocambionota");
        db.execSQL("DELETE FROM solicitudrevision");
        db.execSQL("DELETE FROM tipogrupo");
        db.execSQL("DELETE FROM local");
        db.execSQL("DELETE FROM evaluacion");
        db.execSQL("DELETE FROM periodoinscripcionrevision");
        db.execSQL("DELETE FROM primerrevision");
        db.execSQL("DELETE FROM segundarevision");


        Usuario user = new Usuario();
        for (int i = 0; i<usersId.length; i++){
            user.setUsername(usersId[i]);
            user.setNombreUsuario(names[i]);
            user.setPassword(userPass[i]);
            insertar(user);
        }

        Asignatura asignatura = new Asignatura();
        for (int i=0; i<TAcodasignatura.length; i++){
            asignatura.setCodasignatura(TAcodasignatura[i]);
            asignatura.setNomasignatura(TAnomasignatura[i]);
            asignatura.setUnidadesval(TAunidadesval[i]);
            insertar(asignatura);
        }

        Ciclo ciclo = new Ciclo();
        for( int i=0; i<TCcodciclo.length; i++){
            ciclo.setCodciclo(TCcodciclo[i]);
            ciclo.setFechadesde(TCfechadesde[i]);
            ciclo.setFechahasta(TCfechahasta[i]);
            insertar(ciclo);
        }

        TipoEvaluacion tipoeval = new TipoEvaluacion();
        for(int i=0; i<TTEcodtipoeval.length; i++){
            tipoeval.setCodTipoEval(TTEcodtipoeval[i]);
            tipoeval.setNomTipoEval(TTEnomtipoeval[i]);
            insertar(tipoeval);
        }

        TipoRevision tiporev = new TipoRevision();
        for(int i=0; i<TRcodrevision.length; i++){
            tiporev.setCodTipoRev(TRcodrevision[i]);
            tiporev.setNomTipoRev(TRnomrevision[i]);
            insertar(tiporev);
        }

        Docente docente = new Docente();
        for(int i=0; i<TDcoddocente.length; i++){
            docente.setCoddocente(TDcoddocente[i]);
            docente.setNomdocente(TDnombredocente[i]);
            docente.setApellidodocente(TDapellidodocente[i]);
            insertar(docente);
        }

        Estudiante estudiante = new Estudiante();
        for(int i=0; i<TEcarnet.length; i++){
            estudiante.setCarnet(TEcarnet[i]);
            estudiante.setNombre(TEnombreestudiante[i]);
            estudiante.setApellido(TEapellidoestudiante[i]);
            estudiante.setCarrera(TEcarrera[i]);
            insertar(estudiante);
        }

        MotivoCambioNota motivo = new MotivoCambioNota();
        for(int i=0; i<TMCNcodmotivocambnota.length; i++){
            motivo.setCodmotivocambionota(TMCNcodmotivocambnota[i]);
            motivo.setMotivo(TMCNmotivo[i]);
            insertar(motivo);
        }

        SolicitudRevision solRev = new SolicitudRevision();
        for(int i=0; i<TSoRcarnet.length; i++){
            solRev.setCarnet(TSoRcarnet[i]);
            solRev.setCodasignatura(TSoRcodasignatura[i]);
            solRev.setCodciclo(TSoRcodcilo[i]);
            solRev.setCodtipoeval(TSoRcodtipoeval[i]);
            solRev.setCodtiporevision(TSoRcodtiporev[i]);
            solRev.setFechasolicitudrevision(TSoRfechasolicitudrev[i]);
            solRev.setMotivorevision(TSoRmotivorevision[i]);
            solRev.setNotaantesrevision(TSoRnotaantesrev[i]);
            solRev.setNumeroeval(TSoRcodnumeroeval[i]);
            solRev.setNumerogrupo(TSoRnumerogrupo[i]);
            solRev.setCodtipogrupo(TTGcodtipoGrupo[i]);
            insertar(solRev);
        }

        TipoGrupo tipogrupo = new TipoGrupo();
        for(int i=0; i<TTGcodtipoGrupo.length; i++){
            tipogrupo.setCodtipogrupo(TTGcodtipoGrupo[i]);
            tipogrupo.setNombreTipoGrupo(TTGnomtipogrupo[i]);
            insertar(tipogrupo);
        }




        cerrar();
        return "Guardado correctamente";
    }
}
