/** @file TdManagerAlertDialog.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief project manager alert dialog
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;

import android.widget.TextView;

class TdManagerAlertDialog 
{
  TdManagerAlertDialog( Context context, Resources res, String title,
                        DialogInterface.OnClickListener pos )
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

