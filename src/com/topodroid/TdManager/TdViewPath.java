/** @file TdViewPath.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief path display object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Matrix;

class TdViewPath
{
  TdViewStation mSt1;
  TdViewStation mSt2;
  Path mPath;

  TdViewPath( TdViewStation st1, TdViewStation st2 )
  {
    mSt1 = st1;
    mSt2 = st2;
    mPath = new Path();
    mPath.moveTo( st1.x, st1.y );
    mPath.lineTo( st2.x, st2.y );
  }

  void draw( Canvas canvas, Matrix matrix, Paint paint )
  {
    Path path = new Path( mPath );
    path.transform( matrix );
    canvas.drawPath( path, paint );
  }
}
