/* @file CurrentStation.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief TopoDroid current station
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

class CurrentStation
{
  final String mName;
  final String mComment;
  final int mFlag;

  static final int STATION_NONE    = 0;
  static final int STATION_FIXED   = 1;
  static final int STATION_PAINTED = 2;

  CurrentStation( String name, String comment, long flag )
  {
    mName    = name;
    mComment = (comment == null)? "" : comment;
    mFlag    = (int)flag;
  }

}
