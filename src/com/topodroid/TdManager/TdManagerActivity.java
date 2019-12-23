/** @file TdManagerActivity.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief project manager main activity
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 * Displays the list of thconfig files
 * - long-pressing on a file opens it in the editor
 * - clicking on a file starts the TdConfigActivity on it
 */
package com.topodroid.TdManager;

import java.io.File;
import java.io.IOException;
import java.io.FilenameFilter;

import java.util.ArrayList;

import android.content.res.Resources;
import android.content.pm.PackageManager;
// import android.app.Dialog;

import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;

import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
// import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.os.Bundle;
import android.app.Activity;
import android.net.Uri;

import android.view.Menu;
// import android.view.SubMenu;
import android.view.MenuItem;
// import android.view.MenuInflater;

import android.util.Log;

public class TdManagerActivity extends Activity
                       implements OnItemClickListener
                       , OnItemLongClickListener
                       , OnClickListener

{
  TdManagerApp mApp;
  TdConfigAdapter mTdConfigAdapter;

  // HorizontalListView mListView;
  // HorizontalButtonView mButtonView1;

  ListView mList;
  // Button   mImage;
  // ListView mMenu;
  // ArrayAdapter<String> mMenuAdapter;
  Button[] mButton1;

  private int mApp_mCheckPerms;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );
    setContentView(R.layout.tdmanager_activity);
    // getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

    mApp = (TdManagerApp) getApplication();
    mApp.mActivity = this;
    mApp_mCheckPerms = TdManagerApp.mCheckPerms;

    FeatureChecker.createPermissions( mApp, this );

    setTitle("TH PROJECT MANAGER");
    
    mList = (ListView) findViewById(R.id.th_list);
    mList.setOnItemClickListener( this );
    mList.setOnItemLongClickListener( this );
    mList.setDividerHeight( 2 );
    mList.setDescendantFocusability( ViewGroup.FOCUS_BEFORE_DESCENDANTS );

    // mImage = (Button) findViewById( R.id.handle );
    // mImage.setOnClickListener( this );
    // mMenu = (ListView) findViewById( R.id.menu );
    // mMenuAdapter = null;
    // setMenuAdapter( getResources() );
    // closeMenu();
    // mMenu.setOnItemClickListener( this );

    // mListView = (HorizontalListView) findViewById(R.id.listview);
    resetButtonBar();

  }
  
  // -------------------------------------------------
  // boolean onMenu;
  int mNrButton1 = 4;
  // int mNrMenus   = 3;
  private static int izons[] = { 
    R.drawable.iz_plus,
    R.drawable.iz_options,
    R.drawable.iz_help,
    R.drawable.iz_exit,
  };
  // private static int menus[] = { 
  //   R.string.menu_new,
  //   R.string.menu_options,
  //   R.string.menu_help
  // };

  private void resetButtonBar()
  {
    // mImage.setBackgroundDrawable( MyButton.getButtonBackground( mApp, getResources(), R.drawable.iz_menu ) );

    if ( mNrButton1 > 0 ) {
      // int size = mApp.setListViewHeight( mListView );
      int size = TdManagerApp.getScaledSize( this );
      LinearLayout layout = (LinearLayout) findViewById( R.id.list_layout );
      layout.setMinimumHeight( size + 40 );
      LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
      lp.setMargins( 10, 10, 10, 10 );
      lp.width  = size;
      lp.height = size;
      // MyButton.resetCache( size );

      // FIXME THMANAGER
      // mNrButton1 = 3 + ( TDSetting.mLevelOverAdvanced ? 2 : 0 );
      mButton1 = new Button[mNrButton1];

      for (int k=0; k<mNrButton1; ++k ) {
        mButton1[k] = MyButton.getButton( this, this, size, izons[k] );
        layout.addView( mButton1[k], lp );
	// Log.v("TdManager", "button size " + mButton1[k].getWidth() + "x" + mButton1[k].getHeight() );
      }

      // mButtonView1 = new HorizontalButtonView( mButton1 );
      // mListView.setAdapter( mButtonView1.mAdapter );
    }
  }

  // private void setMenuAdapter( Resources res )
  // {
  //   mMenuAdapter = new ArrayAdapter<String>( this, R.layout.menu );
  //   for ( int k=0; k<mNrMenus; ++k ) {
  //     mMenuAdapter.add( res.getString( menus[k] ) );  
  //   }
  //   mMenu.setAdapter( mMenuAdapter );
  //   mMenu.invalidate();
  // }

  // private void closeMenu()
  // {
  //   mMenu.setVisibility( View.GONE );
  //   onMenu = false;
  // }

  // private void handleMenu( int pos ) 
  // {
  //   closeMenu();
  //   int p = 0;
  //   if ( p++ == pos ) {        // NEW
  //     (new TdConfigDialog( this, this )).show();
  //   } else if ( p++ == pos ) { // OPTIONS
  //     Intent intent = new Intent( this, TdManagerPreferences.class );
  //     startActivity( intent );
  //   } else if ( p++ == pos ) { // HELP
  //     new TdManagerHelpDialog( this ).show();
  //   }
  // }

  // ----------------------------------------------

  @Override
  public void onStart()
  {
    super.onStart();
    // Log.v("TdManager", "TdManager on resume");
    if ( mApp_mCheckPerms >= 0 ) {
      updateTdConfigList();
    }  
  }

  @Override
  public void onResume()
  {
    super.onResume();
    if ( mApp_mCheckPerms > 0 ) {
      TdManagerPerms perms_dialog = null;
      perms_dialog = new TdManagerPerms( this, mApp_mCheckPerms );
      perms_dialog.show();
    } else if ( mApp_mCheckPerms < 0 ) {
      // if ( perms_dialog != null ) perms_dialog.dismiss();
      Toast.makeText( this, "PERMISSIONS ARE NOT GRANTED", Toast.LENGTH_LONG ).show();
      finish();
    }
  }
    
  void updateTdConfigList()
  {
    mTdConfigAdapter = new TdConfigAdapter( this, R.layout.row, new ArrayList<TdConfig>(),
      new View.OnClickListener() {
        @Override
        public void onClick( View v ) {
          String survey = ((TextView)v).getText().toString();
	  // Log.v("TdManager", "on click " + survey );
          TdConfig thconfig = mTdConfigAdapter.get( survey );
	  if ( thconfig != null ) startTdConfigActivity( thconfig );
	}
      }
    );
    File[] thconfigs = TdManagerPath.scanTdConfigDir();
    if ( thconfigs.length > 0 ) {
      for ( File file : thconfigs ) {
        // Log.v("TdManager", "TdManager activity update " + file.getAbsolutePath() );
        mTdConfigAdapter.add( new TdConfig( file.getAbsolutePath() ) );
      }
    } else {
      mTdConfigAdapter.add( new TdConfig( TdManagerPath.getTdConfigTestPath() ) );
    }
    mList.setAdapter( mTdConfigAdapter );
    // Log.v("TdManager", "set adapter: size " + mTdConfigAdapter.size() );
  }

  @Override
  public boolean onItemLongClick( AdapterView<?> parent, View view, int pos, long id )
  {
    onItemClick( parent, view, pos, id );
    return true;
  }

  @Override
  public void onItemClick( AdapterView<?> parent, View view, int pos, long id )
  {
    // CharSequence item = ((TextView) view).getText();
    // if ( mMenu == (ListView)parent ) {
    //   handleMenu( pos );
    //   return;
    // }
    // if ( onMenu ) {
    //   closeMenu();
    //   return;
    // }

    TdConfig thconfig = mTdConfigAdapter.getItem( pos );
    // Log.v("TdManager", "On Item Click: pos " + pos + " TdConfig " + thconfig.mFilepath );
    // TODO start TdConfigActivity or Dialog
    startTdConfigActivity( thconfig );
  }

  void startTdConfigActivity( TdConfig thconfig )
  {
    Intent intent = new Intent( this, TdConfigActivity.class );
    intent.putExtra( TdManagerApp.THCONFIG_PATH, thconfig.getFilepath() );
    try {
      startActivityForResult( intent, TdManagerApp.REQUEST_THCONFIG );
    } catch ( ActivityNotFoundException e ) {
      Toast.makeText( this, R.string.no_editor, Toast.LENGTH_LONG ).show();
    }
  }

  /** add a new thconfig file
   * @param name    thconfig name
   */
  void addTdConfig( String name )
  {
    String filename = name;
    name = name.trim();
    if ( name == null || name.length() == 0 || name.startsWith(".") || name.startsWith("/") ) {
      Toast.makeText( this, R.string.name_invalid, Toast.LENGTH_SHORT ).show();
      return;
    }
    if ( ! filename.endsWith(".thconfig") ) filename = filename + ".thconfig";
    String path = TdManagerPath.getTdConfigPath( filename );
    if ( (new File(path)).exists() ) {
      Toast.makeText( this, R.string.name_exists, Toast.LENGTH_SHORT ).show();
      return;
    }
    TdConfig thconfig = new TdConfig( path );
    // updateTdConfigList();
    mTdConfigAdapter.add( thconfig );
    // Log.v("TdManager", "path >" + path + "< size " + mTdConfigAdapter.size() );
    // mList.setAdapter( mTdConfigAdapter );
    mList.invalidate();
  }

  /** deletes a thconfig file
   * @param filename thconfig filename
   */
  // void deleteTdConfig( String filepath )
  // {
  //   File file = new File( filepath );
  //   file.delete();
  //   updateTdConfigList();
  // }
    
  public void onActivityResult( int request, int result, Intent intent ) 
  {
    Bundle extras = (intent != null )? intent.getExtras() : null;
    switch ( request ) {
      case TdManagerApp.REQUEST_THCONFIG:
        if ( result == TdManagerApp.RESULT_THCONFIG_OK ) {
          // nothing 
        } else if ( result == TdManagerApp.RESULT_THCONFIG_DELETE ) {
          // get TdConfig name and delete it
          String path = extras.getString( TdManagerApp.THCONFIG_PATH );
          mTdConfigAdapter.deleteTdConfig( path );
          mList.invalidate();
          // updateTdConfigList();
        } else if ( result == TdManagerApp.RESULT_THCONFIG_NONE ) {
          // nothing
        }
    }
  }


  // ---------------------------------------------------------------
  // OPTIONS MENU

  // private MenuItem mMInew;
  // private MenuItem mMIhelp;
  // private MenuItem mMIoptions;

  // @Override
  // public boolean onCreateOptionsMenu(Menu menu) 
  // {
  //   super.onCreateOptionsMenu( menu );

  //   mMInew     = menu.add( R.string.menu_new );
  //   mMIoptions = menu.add( R.string.menu_options );
  //   mMIhelp    = menu.add( R.string.menu_help );
  //   return true;
  // }

  // @Override
  // public boolean onOptionsItemSelected(MenuItem item) 
  // {
  //   if ( item == mMIoptions ) { // OPTIONS DIALOG
  //     Intent intent = new Intent( this, TdManagerPreferences.class );
  //     startActivity( intent );
  //   } else if ( item == mMIhelp ) { 
  //     new TdManagerHelpDialog( this ).show();
  //     // TODO
  //   } else if ( item == mMInew ) { 
  //     (new TdConfigDialog( this, this )).show();
  //   } else {
  //     return false;
  //   }
  //   return true;
  // }

  // ---------------------------------------------------------------

  @Override
  public void onClick(View view)
  { 
    // if ( onMenu ) {
    //   closeMenu();
    //   return;
    // }
    Button b0 = (Button)view;

    // if ( b0 == mImage ) {
    //   if ( mMenu.getVisibility() == View.VISIBLE ) {
    //     mMenu.setVisibility( View.GONE );
    //     onMenu = false;
    //   } else {
    //     mMenu.setVisibility( View.VISIBLE );
    //     onMenu = true;
    //   }
    //   return;
    // }
    int k1 = 0;
    if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) { // THCONFIG
      (new TdConfigDialog( this, this )).show();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // OPTIONS
      Intent intent = new Intent( this, TdManagerPreferences.class );
      startActivity( intent );
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) { // HELP
      new TdManagerHelpDialog( this ).show();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // EXIT
      finish();
    }
  }
    

  /* FIXME-23 */
  @Override
  public void onRequestPermissionsResult( int code, final String[] perms, int[] results )
  {
    if ( code == FeatureChecker.REQUEST_PERMISSIONS ) {
      if ( results.length > 0 ) {
	for ( int k = 0; k < results.length; ++ k ) {
	  FeatureChecker.GrantedPermission[k] = ( results[k] == PackageManager.PERMISSION_GRANTED );
	  // Log.v("TdManager", "MAIN perm " + k + " perms " + perms[k] + " result " + results[k] );
	}
      }
    }
    // Log.v("TdManager", "MAIN must restart " + FeatureChecker.MustRestart );
    // if ( ! FeatureChecker.MustRestart ) {
    //   TopoDroidAlertDialog.makeAlert( this, getResources(), R.string.perm_required,
    //     new DialogInterface.OnClickListener() {
    //       @Override public void onClick( DialogInterface dialog, int btn ) { finish(); }
    //     }
    //   );
    // }
  }
  /* */

}