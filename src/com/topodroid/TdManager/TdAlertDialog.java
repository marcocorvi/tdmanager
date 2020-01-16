/** @file TdAlertDialog.java
 *
 * @author marco corvi
 * @date may 2012
 *
 * @brief alert dialog (copied from TopoDroid)
 * TODO replace with TopoDroidAlertDialog
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;

import android.widget.TextView;

class TdAlertDialog 
{
  TdAlertDialog( Context context, Resources res, String title, DialogInterface.OnClickListener pos )
  {
      // NEED API LEVEL 11 for custom background color
      AlertDialog.Builder alert_builder = new AlertDialog.Builder( context );

      alert_builder.setMessage( title );

      alert_builder.setPositiveButton( res.getString( R.string.button_cancel ), 
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialog, int btn ) { }
          }
      );

      alert_builder.setNegativeButton( res.getString( R.string.button_ok ), pos );

      AlertDialog alert = alert_builder.create();
      // alert.getWindow().setBackgroundDrawableResource( R.color.background );
      alert.show();
  }
}
