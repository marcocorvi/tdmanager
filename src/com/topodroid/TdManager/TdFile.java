/** @file TdFile.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief TdFile represents a cave project stored as a file
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import android.util.Log;

class TdFile 
{
  private String mName;                // filename without extension (for display purposes) or surveyname (TD database)
  private String mFilepath;            // thconfig file (fullpath) or null

  public TdFile( String filepath, String surveyname )
  {
    mFilepath = filepath;
    if ( surveyname == null ) { // get name from file
      int pos = mFilepath.lastIndexOf('/');
      mName = ( pos >= 0 )? mFilepath.substring( pos+1 ) : mFilepath;
      mName = mName.replace(".thconfig", "");
    } else {
      mName = surveyname;
    }
  }

  public String getFilepath() { return mFilepath; }

  public String getSurveyName() { return mName; }

  public String toString() { return mName; }

}
