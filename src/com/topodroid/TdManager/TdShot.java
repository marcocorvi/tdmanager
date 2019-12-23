/** @file TdShot.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief survey shot object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 */
package com.topodroid.TdManager;

import android.util.Log;

public class TdShot
{
  private static final float DEG2RAD = (float)(Math.PI/180);

  String mFrom;
  String mTo;
  TdStation mFromStation;
  TdStation mToStation; 
  float mLength, mBearing, mClino;  // radians
  int mExtend;
  TdSurvey mSurvey;  // survey this shot belongs to

  public TdShot( String f, String t, float l, float b, float c, int e, TdSurvey survey )
  {
    mFrom = f;
    mTo   = t;
    mLength  = l;
    mBearing = b * DEG2RAD;
    mClino   = c * DEG2RAD;
    mExtend  = e;
    mFromStation = null;
    mToStation   = null;
    mSurvey = survey;
  }

  public TdShot( float l, float b, float c, int e, TdSurvey survey )
  {
    mFrom = null;
    mTo   = null;
    mLength  = l;
    mBearing = b * DEG2RAD;
    mClino   = c * DEG2RAD;
    mExtend  = e;
    mFromStation = null;
    mToStation   = null;
    mSurvey = survey;
  }

  void setStations( TdStation fs, TdStation ts )
  {
    mFromStation = fs;
    mToStation   = ts;
  }

}

