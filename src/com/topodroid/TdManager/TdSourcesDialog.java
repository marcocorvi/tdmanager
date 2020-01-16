/** @file TdSourcesDialog.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief survey source dialog
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
import android.widget.ListView;

import android.view.ViewGroup.LayoutParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.widget.Toast;

class TdSourcesDialog extends Dialog
{
  Context mContext;
  TdSourceAdapter mTdSourceAdapter;
  TdConfigActivity mParent;
  ListView mList;
  ArrayList< TdSource > mSources;

  TdSourcesDialog( Context context, TdConfigActivity parent )
  {
    super( context );
    mContext = context;
    mParent = parent;
    mSources = new ArrayList< TdSource >();
  }

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.tdsources_dialog);
    getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    mList = (ListView) findViewById(R.id.list);
    mList.setDividerHeight( 2 );

    setTitle("SURVEYS");

    updateList();
  }

  void updateList()
  {
    mTdSourceAdapter = new TdSourceAdapter( mContext, R.layout.tdsource_adapter, mSources );
    List< String > surveys = mParent.getTdDataHelper().getSurveys(); 
    for ( String name : surveys ) {
      if ( ! mParent.hasSource( name ) ) {
        // Log.v("TdManager", "source name " + name + " path " + path );
        mTdSourceAdapter.addTdSource( new TdSource( name ) );
      }
    }
    if ( mTdSourceAdapter.size() > 0 ) {
      mList.setAdapter( mTdSourceAdapter );
      // mList.invalidate();
    } else {
      hide();
      Toast.makeText( mContext, R.string.no_th_file, Toast.LENGTH_LONG ).show();
      dismiss();
    }
  }

  @Override
  public void onBackPressed()
  {
    hide();
    List<String> sources = mTdSourceAdapter.getCheckedSources();
    // Log.v("TdManager", "checked sources " + sources.size() );
    mParent.addSources( sources );
    dismiss();
  }

}
