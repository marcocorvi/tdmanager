/** @file TdDataHelper.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief Interface to TopoDroid surveu database
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.DataSetObservable;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDiskIOException;

// import android.widget.Toast;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Locale;
import java.util.HashMap;

@SuppressWarnings("SyntaxError")
class DataHelper extends DataSetObservable
{
  private static final String SURVEY_TABLE = "surveys";
  private static final String SHOT_TABLE   = "shots";

  private final static String WHERE_NAME        = "name=?";
  private final static String WHERE_SID         = "surveyId=?";
  private final static String WHERE_SID_STATUS  = "surveyId=? AND status=?";

  private final static String WHERE_SID_ID      = "surveyId=? AND id=?";

  private SQLiteDatabase myDB = null;

  static private String[] mReducedShotFields =
    { "id", "fStation", "tStation", "distance", "bearing", "clino", "extend", "leg" };
  // static private String[] mFullShotFields =
  //   { "id", "fStation", "tStation", "distance", "bearing", "clino", "acceleration", "magnetic", "dip", // 0 ..  8
  //     "extend", "flag", "leg", "comment", "type", "millis", "color", "stretch", "address"              // 9 .. 17
  //   };

  public DataHelper( Context context, String db_name, int db_version )
  {
    openDatabase( context, db_name, db_version );
  }

  void closeDatabase()
  {
    if ( myDB == null ) return;
    myDB.close();
    myDB = null;
  }

  void openDatabase( Context context, String database_name, int db_version )
  {
    // String database_name = TDPath.getDatabase();
    DistoXOpenHelper openHelper = new DistoXOpenHelper( context, database_name, db_version );

    try {
        myDB = openHelper.getWritableDatabase();
        if ( myDB == null ) {
          // Log.v("TdManager", "failed get writable database" );
          return;
        }

        // while ( myDB.isDbLockedByOtherThreads() ) {
        //   TDUtil.slowDown( 200 );
        // }

        // updateConfig = myDB.compileStatement( "UPDATE configs SET value=? WHERE key=?" );

     } catch ( SQLiteException e ) {
       myDB = null;
       logError( "Data Helper cstr failed to get DB", e );
     }
   }

  private void logError( String msg, Exception e )
  {
    Log.e("TdManager", "DB " + msg + ": " + e.getMessage() );
  }

  private void handleDiskIOError( SQLiteDiskIOException e )
  {
    Log.e("TdManager", "DB disk error " + e.getMessage() );
  }

  long getSurveyIdFromName( String name ) 
  {
    long id = -1;
    if ( myDB == null ) { return -2; }
    Cursor cursor = myDB.query( SURVEY_TABLE, new String[] { "id" },
                                "name = ?", new String[] { name },
                                null, null, null );
    if (cursor != null ) {
      if (cursor.moveToFirst() ) {
        id = cursor.getLong(0);
      }
      if ( ! cursor.isClosed()) cursor.close();
    }
    return id;
  }

  // ----------------------------------------------------------------
  // SELECT STATEMENTS - SHOT

  List<DBlock> selectAllLegShots( long sid, long status )
  {
    // Log.v("DistoXX", "B4 select shots all leg");
    List< DBlock > list = new ArrayList<>();
    if ( myDB == null ) return list;
    Cursor cursor = myDB.query(SHOT_TABLE, mReducedShotFields, // { "id", "fStation", "tStation", "distance", "bearing", "clino", "extend", "leg" };
                    WHERE_SID_STATUS, new String[]{ Long.toString(sid), Long.toString(status) },
                    null, null, "id" );
    if (cursor.moveToFirst()) {
      do {
        if ( cursor.getString(1).length() > 0 && cursor.getString(2).length() > 0 ) {
          DBlock block = new DBlock();
          reducedFillBlock( sid, block, cursor );
          list.add( block );
        }
      } while (cursor.moveToNext());
    }
    if ( /* cursor != null && */ !cursor.isClosed()) cursor.close();
    return list;
  }

  private void reducedFillBlock( long sid, DBlock blk, Cursor cursor )
  {
    long leg = cursor.getLong(7);
    blk.setId( cursor.getLong(0), sid );
    blk.setBlockName( cursor.getString(1), cursor.getString(2), (leg == LegType.BACK) );  // from - to
    blk.mLength       = (float)( cursor.getDouble(3) );  // length [meters]
    // blk.setBearing( (float)( cursor.getDouble(4) ) ); 
    blk.mBearing      = (float)( cursor.getDouble(4) );  // bearing [degrees]
    float clino       = (float)( cursor.getDouble(5) );  // clino [degrees], or depth [meters]
    blk.mClino        = clino;
    blk.mExtend       = (int)( cursor.getLong(6) ); 
  }
  
  // ----------------------------------------------------------------------
  // SURVEY

  List<String> selectAllSurveys( )
  {
    List< String > list = new ArrayList<>();
    if ( myDB == null ) return list;
    try {
      Cursor cursor = myDB.query( SURVEY_TABLE,
                                  new String[] { "name" }, // columns
                                  null, null, null, null, "name" );
      if (cursor.moveToFirst()) {
        do {
          list.add( cursor.getString(0) );
        } while (cursor.moveToNext());
      }
      if ( /* cursor != null && */ !cursor.isClosed()) cursor.close();
    } catch ( SQLException e ) {
      // ignore
    }
    return list;
  }

  SurveyInfo getSurveyInfo( String name )
  {
    SurveyInfo info = null;
    if ( myDB == null ) return null;
    Cursor cursor = myDB.query( SURVEY_TABLE,
                               new String[] { "id", "name", "day", "team", "declination", "comment", "init_station", "xsections", "datamode", "extend" }, // columns
                               WHERE_NAME, new String[] { name },
                               null, null, "name" );
    if (cursor.moveToFirst()) {
      info = new SurveyInfo();
      info.id      = cursor.getLong( 0 );
      info.name    = cursor.getString( 1 );
      info.date    = cursor.getString( 2 );
      info.team    = cursor.getString( 3 );
      info.declination = (float)(cursor.getDouble( 4 ));
      info.comment = cursor.getString( 5 );
      info.initStation = cursor.getString( 6 );
      info.xsections = (int)cursor.getLong( 7 );
      info.datamode  = (int)cursor.getLong( 8 );
      info.mExtend   = (int)cursor.getLong( 9 );
    }
    if ( /* cursor != null && */ !cursor.isClosed()) cursor.close();
    return info;
  }

  // ----------------------------------------------------------------------

  @SuppressWarnings("SyntaxError")
  private static class DistoXOpenHelper extends SQLiteOpenHelper
  {
     private static final String create_table = "CREATE TABLE IF NOT EXISTS ";

     DistoXOpenHelper(Context context, String database_name, int db_version ) 
     {
        super(context, database_name, null, db_version ); 
        // Log.v("DistoX", "DB NAME " + database_name );
     }

     @Override
     public void onCreate(SQLiteDatabase db) 
     {
     }

     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
     {  
     }
  }

}

class TdDataHelper
{
  DataHelper mData;

  TdDataHelper( Context context, String db_name, int db_version )
  {
    mData = new DataHelper( context, db_name, db_version );
  }

  public List<String> getSurveys() { return mData.selectAllSurveys(); }

  public SurveyInfo getSurveyInfo( String name ) { return mData.getSurveyInfo( name ); }

  public List<DBlock> getSurveyData( long sid ) { return mData.selectAllLegShots( sid, 0 ); }

  public long getSurveyIdFromName( String name ) { return mData.getSurveyIdFromName( name ); }
}
