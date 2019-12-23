/** @file TdInput.h
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief survey input objetcs
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 *
 */
package com.topodroid.TdManager;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import android.util.Log;

class TdInput extends TdSurvey
              implements View.OnClickListener
{
  boolean mChecked;

  // @param name   db survey name
  TdInput( String name )
  {
    super( name );
    mChecked = false;
  }

  String getSurveyName() { return getName(); }

  // void toggleChecekd() { mChecked = ! mChecked; }

  boolean isChecked() { return mChecked; }

  @Override
  public void onClick( View v ) 
  {
    mChecked = ! mChecked;
    ((CheckBox)v).setChecked( mChecked );
  }

}

