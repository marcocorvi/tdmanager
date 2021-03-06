/* @file CWDActivity.java
 *
 * @author marco corvi
 * @date feb 2015
 *
 * @brief TdManager CWD activity
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 * CHANGES
 */
package com.topodroid.TdManager;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import android.app.Application;
import android.app.Activity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.util.Log;

public class CWDActivity extends Activity
                         implements OnItemClickListener
                         , OnItemLongClickListener
{
  private TdManagerApp mApp;

  private ListView mList;
  private EditText mETcwd;

  private void setPreference()
  {
    String dir_name = mETcwd.getText().toString();
    if ( dir_name == null ) {
      dir_name = "TopoDroid";
    } else {
      dir_name.trim();
      if ( ! dir_name.startsWith( "TopoDroid" ) ) {
        dir_name = "TopoDroid" + dir_name;
      }
    }
    File dir = new File( dir_name );
    if ( ! dir.exists() ) {
      dir.mkdir( );
    }
    // Log.v("TdManager", "dir name <" + dir_name + ">" );
    mApp.setCWDPreference( dir_name );

    Intent intent = new Intent();
    intent.putExtra( TdManagerApp.TDMANAGER_CWD, dir_name );
    setResult( RESULT_OK, intent );
  }
    
  public void updateDisplay( )
  {
    File[] dirs = TdManagerPath.getTopoDroidFiles();
    ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, R.layout.menu );
    for ( File item : dirs ) {
      adapter.add( item.getName() );
    }
    mList.setAdapter( adapter );
    mList.setOnItemClickListener( this );
    mList.setOnItemLongClickListener( this );
    mList.setDividerHeight( 2 );

    mETcwd.setText( mApp.mCWD );
  }

  // ---------------------------------------------------------------
  // list items click

  @Override 
  public boolean  onItemLongClick(AdapterView<?> parent, View view, int pos, long id)
  {
    onItemClick( parent, view, pos, id );
    return true;
  }

  @Override 
  public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  {
    CharSequence item = ((TextView) view).getText();
    mETcwd.setText( item );
  }

  public void onEntryClick( View v )
  {
    CharSequence item = ((TextView) v).getText();
    mETcwd.setText( item );
  }

  
  @Override
  public void onCreate( Bundle b )
  {
    super.onCreate( b );
    setContentView(R.layout.cwd_activity);
    mApp = (TdManagerApp) getApplication();

    getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN );

    mList = (ListView) findViewById( R.id.cwd_list );
    mETcwd = (EditText) findViewById( R.id.cwd_text );
    
    updateDisplay();

    setTitle( R.string.title_cwd );
  }


  @Override
  public synchronized void onStop()
  { 
    super.onStop();
  }

  @Override
  public void onBackPressed()
  {
    setPreference();
    finish();
  }

}
