/** @file TdViewEquateDialog.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief dialog that displays the equates
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import android.app.Dialog;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;

import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

class TdViewEquateDialog extends Dialog
                         implements OnClickListener
{
  Context mContext;
  TdManagerApp mApp;
  TdViewActivity mParent;
  ArrayList< TdViewCommand > mCommands;
  ArrayList< TdViewStationAdapter > mAdapters;
  LinearLayout mLayout;
  Button mBTok;

  TdViewEquateDialog( Context context, TdViewActivity parent, TdManagerApp app )
  {
    super( context );
    mContext = context;
    mParent = parent;
    mApp = app;
    // Log.v("TdManager", "TdViewEquateDialog equates " + mEquates.size() );
    mAdapters = new ArrayList< TdViewStationAdapter >();
  }

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    mCommands = mParent.getCommands();
    if ( mCommands.size() < 2 ) {
      dismiss();
      return;
    } 

    setContentView(R.layout.tdviewequate_dialog);
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );
    setTitle("EQUATES");

    mLayout = (LinearLayout) findViewById( R.id.layout );

    mBTok = (Button) findViewById( R.id.ok );
    mBTok.setOnClickListener( this );

    populateLayout();
  }

  void populateLayout()
  {
    for ( TdViewCommand command : mCommands ) {
      FrameLayout fl = new FrameLayout( mContext );
      // RelativeLayout rl = new RelativeLayout( mContext );
      LinearLayout rl = new LinearLayout( mContext );
      rl.setOrientation( LinearLayout.VERTICAL );
      ListView lv = new ListView( mContext );
      TextView tv = new TextView( mContext );

      tv.setText( command.name() );
      TdViewStationAdapter adapter = 
          new TdViewStationAdapter( mContext, R.layout.tdviewstation_adapter, command.mStationsArray, tv, command );
      lv.setAdapter( adapter );
      mAdapters.add( adapter );
      
      rl.addView( tv, new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT ) );
      rl.addView( lv, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );
      fl.addView( rl, new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ) );
      mLayout.addView( fl, new LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT ) );

    }
  }

  @Override
  public void onClick( View v )
  {
    
    Button b = (Button)v;
    if ( b == mBTok ) { // SAVE equate
      TdEquate equate = new TdEquate();
      for ( TdViewStationAdapter adapter : mAdapters ) {
        TdStation ts = adapter.getCheckedStation();
        String st = adapter.getStationName();
        int len = st.length();
        while ( len > 0 && st.charAt( len - 1 ) == '.' ) -- len;
        if ( len < st.length() ) st = st.substring(0,len);
        equate.addStation( st );
      }
      // Log.v("TdManager", "EQUATE " + equate.stationsString() );
      mApp.mConfig.addEquate( equate );
    }
    dismiss();
  }

}
