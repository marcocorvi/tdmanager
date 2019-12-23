/* @file SurveyInfo.java
 *
 * @author marco corvi
 * @date may 2012
 *
 * @brief TopoDroid survey info (name, date, comment etc)
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import android.util.Log;

import android.widget.EditText;

class SurveyInfo
{
  final static int XSECTION_SHARED  = 0;
  final static int XSECTION_PRIVATE = 1;

  final static int DATAMODE_NORMAL  = 0;
  final static int DATAMODE_DIVING  = 1;

  final static int EXTEND_NORMAL  = 90;
  final static int EXTEND_LEFT    = -1000;
  final static int EXTEND_RIGHT   =  1000;

  final static float DECLINATION_MAX = 720;    // twice 360
  final static float DECLINATION_UNSET = 1080; // three times 360

  long id;
  String name;
  String date;
  String team;
  float  declination;
  String comment;
  String initStation;
  int xsections; // 0: shared, 1: private
  int datamode;
  int mExtend;

  boolean hasDeclination() { return declination < DECLINATION_MAX; }

  // get the declination or 0 if not-defined
  float getDeclination()
  {
    if ( declination < DECLINATION_MAX ) return declination;
    return 0;
  }

}
