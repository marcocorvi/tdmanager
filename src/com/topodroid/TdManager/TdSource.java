/** @file TdSource.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief survey source object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 * source survey in TopoDroid database (essentially it is TdInput)
 */
package com.topodroid.TdManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import android.util.Log;

class TdSource extends TdFile
               implements View.OnClickListener
{
  boolean mChecked;

  public TdSource( String surveyname )
  {
    super( null, surveyname );
    mChecked = false;
  }

  // void toggleChecekd() { mChecked = ! mChecked; }

  boolean isChecked() { return mChecked; }

  @Override
  public void onClick( View v ) 
  {
    mChecked = ! mChecked;
    ((CheckBox)v).setChecked( mChecked );
  }

}
