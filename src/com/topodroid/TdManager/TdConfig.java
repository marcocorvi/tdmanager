/** @file TdConfig.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief cave-project object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.util.Log;

class TdConfig extends TdFile
{
  String mParentDir;            // parent directory
  String mSurveyName;
  TdSurvey mSurvey;             // inline survey in the tdconfig file
  ArrayList< TdInput > mInputs; // surveys: th files on input
  ArrayList< TdEquate > mEquates;
  private boolean mRead;        // whether the TdConfig has read the file

  public TdConfig( String filepath )
  {
    super( filepath, null );

    // Log.v("TdManager", "TdConfig cstr filepath " + filepath );
    mParentDir = (new File( filepath )).getParentFile().getName() + "/";
    mSurvey  = null;
    mInputs  = new ArrayList< TdInput >();
    mEquates = new ArrayList< TdEquate >();
    mRead = false;
  }

  void dropEquates( String survey )
  {
    // Log.v("TdManager", "drop equates with " + survey + " before " + mEquates.size() );
    if ( survey == null || survey.length() == 0 ) return;
    ArrayList< TdEquate > equates = new ArrayList< TdEquate >();
    for ( TdEquate equate : mEquates ) {
      if ( equate.dropStations( survey ) > 1 ) {
        equates.add( equate );
      }
    }
    mEquates = equates;
    // Log.v("TdManager", "dropped equates with " + survey + " after " + mEquates.size() );
  }

  void addEquate( TdEquate equate ) 
  {
    if ( equate == null ) return;
    mEquates.add( equate );
    // Log.v("TdManager", "nr. equates " + mEquates.size() );
  }

  // unconditionally remove an equate
  void removeEquate( TdEquate equate ) { mEquates.remove( equate ); }

  void readTdConfig()
  {
    if ( mRead ) return;
    // Log.v( TdManagerApp.TAG, "readTdConfig() for file " + mName );
    readFile();
    // Log.v( TdManagerApp.TAG, "TdConfig() inputs " + mInputs.size() + " equates " + mEquates.size() );
    mRead = true;
  }
    
  boolean hasInput( String name )
  {
    if ( name == null ) return false;
    // Log.v("TdManager", "TdConfig check input name " + name );
    for ( TdInput input : mInputs ) {
      // Log.v("TdManager", "TdConfig check input " + input.mName );
      if ( name.equals( input.getSurveyName() ) ) return true;
    }
    return false;
  }

  private void addInput( String name )
  {
    if ( name == null ) return;
    // Log.v("TdManager", "add input name " + surveyname );
    mInputs.add( new TdInput( name ) );
  }

  private void dropInput( String name )
  {
    if ( name == null ) return;
    for ( TdInput input : mInputs ) {
      if ( name.equals( input.getSurveyName() ) ) {
        mInputs.remove( input );
        return;
      }
    }
  }


// ---------------------------------------------------------------
// READ and WRITE
  static String currentDate()
  {
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy.MM.dd", Locale.US );
    return sdf.format( new Date() );
  }


  void writeTdConfig( boolean force )
  {
    if ( mRead || force ) {
      writeTd( getFilepath() );
    }
  }

  void writeTd( String filepath )
  {
    try {
      FileWriter fw = new FileWriter( filepath );
      PrintWriter pw = new PrintWriter( fw );
      pw.format("# created by TdManager %s - %s\n", TdManagerApp.VERSION, currentDate() );
      pw.format("source\n");
      pw.format("  survey %s\n", mSurveyName );
      for ( TdInput input : mInputs ) {
        // FIXME path
        String path = input.getSurveyName();
        // Log.v("TdManager", "config write add survey " + path );
        pw.format("    load %s\n", path );
      }
      for ( TdEquate equate : mEquates ) {
        pw.format("    equate");
        for ( String st : equate.mStations ) pw.format(" %s", st );
        pw.format("\n");
      }
      pw.format("  endsurvey\n");
      pw.format("endsource\n");
      fw.flush();
      fw.close();
    } catch ( IOException e ) { 
      Log.e("TdManager", "write file " + getFilepath() + " I/O error " + e.getMessage() );
    }
  }

  String exportTherion( boolean overwrite )
  {
    String filepath = getFilepath().replace(".tdconfig", ".th").replace("/tdconfig/", "/th/");
    File file = new File( filepath );
    if ( file.exists() ) {
      if ( ! overwrite ) return null;
    } else {
      File dir = file.getParentFile();
      if ( dir != null ) dir.mkdirs();
    }
    writeTherion( filepath );
    return filepath;
  }

  void writeTherion( String filepath )
  {
    try {
      FileWriter fw = new FileWriter( filepath );
      PrintWriter pw = new PrintWriter( fw );
      pw.format("# created by TdManager %s - %s\n", TdManagerApp.VERSION, currentDate() );
      pw.format("source\n");
      pw.format("  survey %s\n", mSurveyName );
      for ( TdInput input : mInputs ) {
        // FIXME path
        String path = "../th/" + input.getSurveyName() + ".th";
        // Log.v("TdManager", "config write add survey " + path );
        pw.format("    input %s\n", path );
      }
      for ( TdEquate equate : mEquates ) {
        pw.format("    equate");
        for ( String st : equate.mStations ) pw.format(" %s", st );
        pw.format("\n");
      }
      pw.format("  endsurvey\n");
      pw.format("endsource\n");
      fw.flush();
      fw.close();
    } catch ( IOException e ) { 
      Log.e("TdManager", "write file " + getFilepath() + " I/O error " + e.getMessage() );
    }
  }

  String exportSurvex( boolean overwrite )
  {
    String filepath = getFilepath().replace(".tdconfig", ".svx").replace("/tdconfig/", "/svx/");
    File file = new File( filepath );
    if ( file.exists() ) {
      if ( ! overwrite ) return null;
    } else {
      File dir = file.getParentFile();
      if ( dir != null ) dir.mkdirs();
    }
    writeSurvex( filepath );
    return filepath;
  }

  private String toSvxStation( String st )
  {
    int pos = st.indexOf('@');
    return st.substring(pos+1) + "." + st.substring(0,pos);
  }

  void writeSurvex( String filepath )
  {
    try {
      FileWriter fw = new FileWriter( filepath );
      PrintWriter pw = new PrintWriter( fw );
      pw.format("; created by TdManager %s - %s\n", TdManagerApp.VERSION, currentDate() );
      // TODO EXPORT
      for ( TdInput s : mInputs ) {
        String path = "../svx/" + s.getSurveyName() + ".svx";
        pw.format("*include %s\n", path );
      }
      for ( TdEquate equate : mEquates ) {
        pw.format("*equate");
        for ( String st : equate.mStations ) pw.format(" %s", toSvxStation( st ) );
        pw.format("\n");
      }

      fw.flush();
      fw.close();
    } catch ( IOException e ) { 
      Log.e("TdManager", "write file " + getFilepath() + " I/O error " + e.getMessage() );
    }
  }

  // private void loadFile()
  // {
  //   // Log.v("TdManager", "load file path " + getFilepath() );
  //   mSurvey = new TdSurvey( "." );
  //   new TdParser( getFilepath(), mSurvey, new TdUnits() );
  // }

  private void readFile( )
  {
    // if the file does not exists creates it and write an empty tdconfig file
    // Log.v("TdManager", "read file path " + getFilepath() );
    File file = new File( getFilepath() );
    if ( ! file.exists() ) {
      // Log.v("TdManager", "file does not exist");
      writeTdConfig( true );
      return;
    }

    try {
      FileReader fr = new FileReader( file );
      BufferedReader br = new BufferedReader( fr );
      String line = br.readLine();
      int cnt = 1;
      // Log.v( TdManagerApp.TAG, cnt + ":" + line );
      while ( line != null ) {
        line = line.trim();
        int pos = line.indexOf( '#' );
        if ( pos >= 0 ) line = line.substring( 0, pos );
        if ( line.length() > 0 ) {
          String[] vals = line.split( " " );
          if ( vals.length > 0 ) {
            if ( vals[0].equals( "source" ) ) {
            } else if ( vals[0].equals( "survey" ) ) {
              for (int k=1; k<vals.length; ++k ) {
                if ( vals[k].length() > 0 ) {
                  mSurveyName = vals[k];
                  break;
                }
              }
            } else if ( vals[0].equals( "load" ) ) {
              for (int k=1; k<vals.length; ++k ) {
                if ( vals[k].length() > 0 ) {
                  String surveyname = vals[k];
                  addInput( surveyname );
                  break;
                }
              }    
            } else if ( vals[0].equals( "equate" ) ) {
              TdEquate equate = new TdEquate();
              for (int k=1; k<vals.length; ++k ) {
                if ( vals[k].length() > 0 ) {
                  equate.addStation( vals[k] );
                }
              }
              mEquates.add( equate );
            }
          }
        }
        line = br.readLine();
        ++ cnt;
      }
      fr.close();
    } catch ( IOException e ) {
      // TODO
      Log.e( TdManagerApp.TAG, "exception " + e.getMessage() );
    }
    // Log.v( "TdManager", "TdConfig read file: nr. sources " + mInputs.size() );
  }

}
