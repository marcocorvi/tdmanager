/** @file TdViewEquate.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief equate display object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import java.util.ArrayList;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Matrix;

import android.widget.CheckBox;

import android.util.Log;

class TdViewEquate
{
  TdEquate mEquate;
  ArrayList< TdViewStation > mStations;
   
  Path mPath;

  TdViewEquate( TdEquate equate )
  {
    mEquate = equate;
    mStations = new ArrayList< TdViewStation >();
    mPath = null;
  }

  void addViewStation( TdViewStation st )
  {
    mStations.add( st );
    makePath();
  }

  void shift( float dx, float dy, TdViewCommand command )
  {
    for ( TdViewStation st : mStations ) {
      if ( command == st.mCommand ) {
        // st.xoff += dx;
        // st.yoff += dy;
        makePath();
        break;
      }
    }
  }

  void makePath()
  {
    if ( mStations.size() > 1 ) {
      mPath = null;
      for ( TdViewStation vst : mStations ) {
        if ( mPath == null ) {
          mPath = new Path();
          mPath.moveTo( vst.fullX(), vst.fullY() );
        } else {
          mPath.lineTo( vst.fullX(), vst.fullY() );
        }
      }
    }
  }

  // void dump()
  // {
  //   Log.v("TdManager", "equate (size " + mStations.size() + ")" );
  //   for ( TdViewStation vst : mStations )
  //     Log.v("TdManager", "  station: " + vst.mStation.mName + " " + vst.mCommand.name() );
  // }

  void draw( Canvas canvas, Matrix matrix, Paint paint )
  {
    if ( mPath != null ) {
      Path path = new Path( mPath );
      path.transform( matrix );
      canvas.drawPath( path, paint );
    }
  }
  
}
