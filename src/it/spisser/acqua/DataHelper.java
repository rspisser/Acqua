package it.spisser.acqua;

/* copiato pari pari da 
 *  http://www.screaming-penguin.com/node/7742
 */

import it.spisser.acqua.R;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataHelper {

	private static final String TAG = "Acqua";
	private static final String DATABASE_NAME = "acqua.db";
	private static final int DATABASE_VERSION = 5;
	private static final String RELEASE_VERSION = "10";
	private Context context;
	private SQLiteDatabase db;

	private SQLiteStatement insertStmt;

	public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		
		this.db = openHelper.getWritableDatabase();
	//	Log.w(TAG, "starting Datahelper");
	}

	public long insert(String name) {
		this.insertStmt.bindString(1, name);
		return this.insertStmt.executeInsert();
	}

	public void deleteAll() {
		this.db.delete("fonti", null, null);
		this.db.delete("comuni", null, null);

	}

	public List<Fonte> getFonti(Double lat_min, Double lat_max,
			Double long_min, Double long_max) {
		String query = "select nomecommerciale, nomefonte, comune, provincia,  lat_gradi_dec, "
				+ "long_gradi_dec,id from fonti where "
				+ "lat_gradi_dec>="
				+ lat_min
				+ " and lat_gradi_dec<"
				+ lat_max
				+ " and long_gradi_dec>"
				+ long_min
				+ " and long_gradi_dec<"
				+ long_max;

//		Log.d(TAG, "query: " + query);

		List<Fonte> elencoFonti = new ArrayList<Fonte>();
		Cursor cursor = this.db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				Fonte fonte = new Fonte();
				fonte.setNomeCommerciale(cursor.getString(0));
				fonte.setNomeFonte(cursor.getString(1));
				fonte.setComuneFonte(cursor.getString(2));
				fonte.setProvinciaFonte(cursor.getString(3));
				fonte.setLat_gradi_dec(cursor.getDouble(4));
				fonte.setLong_gradi_dec(cursor.getDouble(5));
				fonte.setId(cursor.getLong(6));
				elencoFonti.add(fonte);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return elencoFonti;

	}

	public List<String> selectAll() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query("fonti", new String[] { "nomefonte" },
				null, null, null, null, "nomefonte desc");
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public List<String> elencoComuni() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query("comuni", new String[] { "comune" },
				null, null, null, null, "comune");

		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public List<String> elencoFonti() {
		List<String> list = new ArrayList<String>();
		Cursor cursor = this.db.query("fonti", new String[] { "nomeCommerciale" },
				null, null, null, null, "nomeCommerciale");

		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}

	public List<Fonte> elencoFontiBean() {
		List<Fonte> list = new ArrayList<Fonte>();
		Cursor cursor = this.db.query("fonti", new String[] { "nomeCommerciale, lat_gradi_dec, long_gradi_dec" },
				null, null, null, null, "nomeCommerciale");

		if (cursor.moveToFirst()) {
			do {
				Fonte f = new Fonte();
				f.setNomeCommerciale(cursor.getString(0));
				f.setLat_gradi_dec(cursor.getDouble(1));
				f.setLong_gradi_dec(cursor.getDouble(2));
				
				list.add(f);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list;
	}
	
	public Fonte getFonteByName(String nomeFonte) {

		nomeFonte=nomeFonte.replace("'","''");
		Fonte fonteResult = new Fonte();
		Cursor cursor = this.db
				.query("fonti",
						new String[] { "nomeCommerciale, comune, provincia, lat_gradi_dec, long_gradi_dec " },
						" nomeCommerciale='" + nomeFonte + "'", null, null, null,
						"comune desc");
		if (cursor.moveToFirst()) {
			fonteResult.setNomeCommerciale(cursor.getString(0));
			fonteResult.setComuneFonte(cursor.getString(1));
			fonteResult.setProvinciaFonte(cursor.getString(2));
			fonteResult.setLat_gradi_dec(cursor.getDouble(3));
			fonteResult.setLong_gradi_dec(cursor.getDouble(4));

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return fonteResult;

	}

	public Comune getComuneByName(String nomeComune) {
		Log.d(TAG,"ricercaComune: "+nomeComune);
		nomeComune=nomeComune.replace("'","''");
		Comune comuneResult = new Comune();
		Cursor cursor = this.db
				.query("comuni",
						new String[] { "comune, provincia, lat_gradi_dec, long_gradi_dec " },
						" comune='" + nomeComune + "'", null, null, null,
						"comune desc");
		if (cursor.moveToFirst()) {
			comuneResult.setNomecomune(cursor.getString(0));
			comuneResult.setProvincia(cursor.getString(1));
			comuneResult.setLatitudine(cursor.getDouble(2));
			comuneResult.setLongitudine(cursor.getDouble(3));
			Double cl = Math.rint(cursor.getDouble(2)* 10000);

			Double clong = Math.rint(cursor.getDouble(3)* 10000);

			comuneResult.setLatitudine_display(cl / 10000);
			comuneResult.setLongitudine_display(clong / 10000);


		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return comuneResult;

	}

	
	/* questo metodo verifica se � la prima volta che viene lanciata una
	 * nuova versione
	 * Questo serve per far vedere un popup con "Whats new"
	 */
	public boolean checkIfNewVersion(String versione){
		
		boolean result=false;
		Fonte fonteResult = new Fonte();
		Cursor cursor = this.db
				.query("versioni",
						new String[] { "nomeversione" },
						" nomeversione='" + versione + "'", null, null, null,
						"nomeversione");
		if (cursor.moveToFirst()) {
			fonteResult.setNomeCommerciale(cursor.getString(0));
			fonteResult.setComuneFonte(cursor.getString(1));
			fonteResult.setProvinciaFonte(cursor.getString(2));
			fonteResult.setLat_gradi_dec(cursor.getDouble(3));
			fonteResult.setLong_gradi_dec(cursor.getDouble(4));

		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		/* inserire la versione attuale nel db */
		return result;

	}

	
	
	private static class OpenHelper extends SQLiteOpenHelper {

		private final Context mContext;

		OpenHelper(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.mContext = context;
		}

		public void onCreate(SQLiteDatabase db) {

//			Log.w(TAG, "Creating database");
			db.execSQL("CREATE TABLE fonti (id INTEGER PRIMARY KEY,   nomecommerciale VARCHAR(100) collate nocase,   nomefonte VARCHAR(100) ,   comune VARCHAR(35) ,  provincia VARCHAR(20),   lat_gradi_dec INTEGER,long_gradi_dec INTEGER)");
			db.execSQL("CREATE TABLE comuni (id INTEGER PRIMARY KEY,   comune VARCHAR(100) collate nocase,   provincia VARCHAR(35) ,  regione  VARCHAR(20), lat_gradi_dec INTEGER,long_gradi_dec INTEGER)");
//			db.execSQL("CREATE TABLE versioni (id INTEGER PRIMARY KEY,   nomeversione  VARCHAR(35))");

			String[] fonti = this.mContext.getString(
					R.string.AcquaDatabase_fonti).split("\n");
			String sqlstart1 = "INSERT INTO fonti (ID, nomecommerciale, nomefonte, comune, provincia, lat_gradi_dec, long_gradi_dec) VALUES (";

			String fontesql;
			for (String fonte : fonti) {
				if (fonte.trim().length() > 0) {
					fonte = fonte.replace("'", "''");
					String[] fontedati = fonte.split("\t");
					fontesql = sqlstart1.concat(fontedati[0]);
					fontesql = fontesql.concat(",'");
					fontesql = fontesql.concat(fontedati[1]);
					fontesql = fontesql.concat("','");
					fontesql = fontesql.concat(fontedati[2]);
					fontesql = fontesql.concat("','");
					fontesql = fontesql.concat(fontedati[3]);
					fontesql = fontesql.concat("','");
					fontesql = fontesql.concat(fontedati[4]);
					fontesql = fontesql.concat("',");
					fontesql = fontesql.concat(fontedati[5]);
					fontesql = fontesql.concat(",");
					fontesql = fontesql.concat(fontedati[6]);
					fontesql = fontesql.concat(")");
					try {
						db.execSQL(fontesql);
					} catch (Exception e) {

					}

				}

			}

			String[] comuninew = this.mContext.getString(
					R.string.AcquaDatabase_comuni).split("\n");
			String sqlstart = "insert into comuni (comune, provincia, regione, lat_gradi_dec, long_gradi_dec) values ('";
			String comunesql;
			for (String comune : comuninew) {
				if (comune.trim().length() > 0) {
					comune = comune.replace("'", "''");

					String[] comunedati = comune.split("\t");
					comunesql = sqlstart.concat(comunedati[0]);
					comunesql = comunesql.concat("', '");
					comunesql = comunesql.concat(comunedati[1]);
					comunesql = comunesql.concat("', '");
					comunesql = comunesql.concat(comunedati[2]);
					comunesql = comunesql.concat("', ");
					comunesql = comunesql.concat(comunedati[3]);
					comunesql = comunesql.concat(", ");
					comunesql = comunesql.concat(comunedati[4]);
					comunesql = comunesql.concat(") ");
					try {
						db.execSQL(comunesql);
					} catch (Exception e) {

					}

				}

			}

		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// this.LogUtils("Upgrading database, this will drop tables and recreate.");
			db.execSQL("drop table if exists fonti");
			db.execSQL("drop table if exists comuni");
			onCreate(db);

		}
	}

}
/*
 * 
 * per usare il db in locale, far partire l'elmulatore android, poi andare:
 * 
 * in cd C:\Program Files (x86)\Android\android-sdk\platform-tools adb -e shell
 * cd /data/data/it.spisser.chqua/databases sqlite3 acqua.db
 * 
 * per cancellare
 * 
 * adb -e shell cd /data/data/it.spisser.acqua/databases
 * 
 * rm acqua.db
 */
