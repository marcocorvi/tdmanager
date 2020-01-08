/* @file TdConfigDialog.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief Dialog to enter the filename of a project
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * CHANGES
 */
package com.topodroid.TdManager;

import android.os.Bundle;
import android.app.Dialog;
// import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;


public class TdConfigDialog extends Dialog 
                            implements View.OnClickListener
{
    private EditText mLabel;
    private Button mBtnOK;
    // private Button mBtnCancel;

    private TdManagerActivity mActivity;

    public TdConfigDialog( Context context, TdManagerActivity activity )
    {
      super(context);
      mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tdconfig_dialog);
      getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

      mLabel     = (EditText) findViewById(R.id.label_text);
      mBtnOK     = (Button) findViewById(R.id.label_ok);
      // mBtnCancel = (Button) findViewById(R.id.label_cancel);

      mBtnOK.setOnClickListener( this );
      // mBtnCancel.setOnClickListener( this );

      setTitle("TdConfig filename");
    }

    public void onClick(View view)
    {
      if (view.getId() == R.id.label_ok ) {
        String name = mLabel.getText().toString();
        if ( ! name.endsWith( ".tdconfig" ) ) {
          name = name + ".tdconfig";
        }
        mActivity.addTdConfig( name );
      }
      dismiss();
    }
}
        

