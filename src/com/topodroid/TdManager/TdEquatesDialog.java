/** @file TdEquatesDialog.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief list of equates
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 *
 */
package com.topodroid.TdManager;

import java.util.ArrayList;
import java.io.File;

import android.content.DialogInterface;
import android.app.Dialog;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

class TdEquatesDialog extends Dialog
                      implements OnItemClickListener
{
  Context mContext;
  TdEquateAdapter mTdEquateAdapter;
  TdConfig mConfig;
  TdViewActivity mActivity;
  ListView mList;
  ArrayList< TdEquate > mEquates;

  TdEquatesDialog( Context context, TdConfig config, TdViewActivity activity )
  {
    super( context );
    mContext = context;
    mConfig  = config;
    mActivity = activity;
    if ( mConfig != null && mConfig.mEquates != null ) {
      mEquates = mConfig.mEquates;
    } else {
      mEquates = new ArrayList< TdEquate >();
    }
    // Log.v("TdManager", "TdEquatesDialog equates " + mEquates.size() );
  }

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.tdequates_dialog);
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    mList = (ListView) findViewById(R.id.list);
    mList.setDividerHeight( 2 );
    mList.setOnItemClickListener( this );

    setTitle("THERION EQUATES");

    updateList();
  }

  void updateList()
  {
    if ( mEquates != null && mEquates.size() > 0 ) {
      mTdEquateAdapter = new TdEquateAdapter( mContext, R.layout.tdequate_adapter, mEquates );
      mList.setAdapter( mTdEquateAdapter );
      // mList.invalidate();
    } else {
      hide();
      Toast.makeText( mContext, R.string.no_equate, Toast.LENGTH_LONG ).show();
      dismiss();
    }
  }

  void doRemoveEquate( TdEquate equate )
  {
    mEquates.remove( equate );
    updateList();
    if ( mActivity != null ) mActivity.updateViewEquates();
  }

  void askRemoveEquate( final TdEquate equate )
  {
    new TdManagerAlertDialog( mContext, mContext.getResources(), 
      String.format( mContext.getResources().getString( R.string.ask_remove_equate ), equate.stationsString() ),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick( DialogInterface dialog, int btn ) {
          doRemoveEquate( equate );
        }
    } );
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    TdEquateViewHolder vh = (TdEquateViewHolder) view.getTag();
    if ( vh != null ) {
      askRemoveEquate( vh.equate );
    }
  }
      
}
