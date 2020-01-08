/** @file TdConfigActivity.java
 *
 * @author marco corvi
 * @date may 2012
 *
 * @brief interface activity for a tdconfig file
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Dialog;
import android.widget.Button;
import android.widget.ArrayAdapter;

import android.view.View;
// import android.view.ViewGroup.LayoutParams;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.AsyncTask;
import android.app.Activity;

import android.content.res.Resources;

import android.view.Menu;
// import android.view.SubMenu;
import android.view.MenuItem;
// import android.view.MenuInflater;

import android.net.Uri;

import android.util.Log;

public class TdConfigActivity extends Activity
                              implements OnClickListener
                              // , OnItemClickListener
{
  TdInputAdapter mTdInputAdapter;
  TdManagerApp mApp;
  TdDataHelper mData;

  private FilenameFilter filterTd;

  private static String[] mExportTypes = { "Therion", "Survex" };

  // HorizontalListView mListView;
  // HorizontalButtonView mButtonView1;

  ListView mList;
  // Button   mImage;
  // ListView mMenu;
  // ArrayAdapter<String> mMenuAdapter;
  Button[] mButton1;

  @Override
  public void onCreate( Bundle savedInstanceState )
  {
    super.onCreate( savedInstanceState );

    mApp = (TdManagerApp) getApplication();

    String db_name = TdManagerPath.getBaseDB();
    int db_version = 42; // as in TopoDroid DataHelper

    mData = new TdDataHelper( this, db_name, db_version );

    mApp.mConfig = null;
    Bundle extras = getIntent().getExtras();
    if ( extras != null ) {
      String path = extras.getString( TdManagerApp.THCONFIG_PATH );
      if ( path != null ) {
        File file = new File(path);
        Log.v( TdManagerApp.TAG, "TdConfigActivity path <" + path + ">" );
        mApp.mConfig = new TdConfig( path );
        mApp.mConfig.readTdConfig();
        if ( file.exists() ) {
          setTitle( "PROJECT " + mApp.mConfig.toString() );
        } else {
          mApp.mConfig = null;
          Toast.makeText( this, R.string.no_file, Toast.LENGTH_LONG ).show();
        }
      } else {
        // Log.v("TdManager", "TdConfig activity missing TdConfig path");
        Toast.makeText( this, R.string.no_path, Toast.LENGTH_LONG ).show();
      }
    }
    if ( mApp.mConfig == null ) {
      doFinish( TdManagerApp.RESULT_THCONFIG_NONE );
    } else {
      setContentView(R.layout.tdconfig_activity);
      // getWindow().setLayout( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT );

      mList = (ListView) findViewById(R.id.th_list);
      // mList.setOnItemClickListener( this );
      mList.setDividerHeight( 2 );

      // mImage = (Button) findViewById( R.id.handle );
      // mImage.setOnClickListener( this );
      // mMenu = (ListView) findViewById( R.id.menu );
      // mMenuAdapter = null;
      // setMenuAdapter( getResources() );
      // closeMenu();
      // mMenu.setOnItemClickListener( this );

      // mListView = (HorizontalListView) findViewById(R.id.listview);
      resetButtonBar();

      updateList();
    }
  }

  @Override
  protected void onPause()
  {
    super.onPause();
    // Log.v("TdManager", "TdConfig activity on pause");
    if ( mApp.mConfig != null ) mApp.mConfig.writeTdConfig( false );
  }

  TdDataHelper getTdDataHelper() { return mData; }

  boolean hasSource( String name ) 
  {
    return mApp.mConfig.hasInput( name );
  }

  /** update surveys list
   */
  void updateList()
  {
    if ( mApp.mConfig != null ) {
      Log.v("TdManager", "TdConfig update list input nr. " + mApp.mConfig.mInputs.size() );
      mTdInputAdapter = new TdInputAdapter( this, R.layout.row, mApp.mConfig.mInputs );
      mList.setAdapter( mTdInputAdapter );
      mList.invalidate();
    } else {
      Toast.makeText( this, R.string.no_tdconfig, Toast.LENGTH_LONG ).show();
    }
  }

  
  // -------------------------------------------------
  // boolean onMenu;
  int mNrButton1 = 8;
  // int mNrMenus   = 5;
  private static int izons[] = { 
    R.drawable.iz_add,
    R.drawable.iz_drop,
    R.drawable.iz_view,
    R.drawable.iz_equates,
    R.drawable.iz_3d,
    R.drawable.iz_export,
    // R.drawable.iz_note,
    R.drawable.iz_delete,
    R.drawable.iz_exit,
  };
  // private static int menus[] = { 
  //   R.string.menu_add,
  //   R.string.menu_drop,
  //   R.string.menu_view,
  //   R.string.menu_equates,
  //   R.string.menu_delete
  // };

  private void resetButtonBar()
  {
    // mImage.setBackgroundDrawable( MyButton.getButtonBackground( mApp, getResources(), R.drawable.iz_menu ) );

    if ( mNrButton1 > 0 ) {
      // int size = mApp.setListViewHeight( mListView );
      // MyButton.resetCache( size );
      int size = TdManagerApp.getScaledSize( this );
      LinearLayout layout = (LinearLayout) findViewById( R.id.list_layout );
      layout.setMinimumHeight( size + 40 );
      LayoutParams lp = new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT );
      lp.setMargins( 10, 10, 10, 10 );
      lp.width  = size;
      lp.height = size;

      // FIXME THMANAGER
      mButton1 = new Button[mNrButton1];

      for (int k=0; k<mNrButton1; ++k ) {
        mButton1[k] = MyButton.getButton( this, this, size, izons[k] );
        layout.addView( mButton1[k], lp );
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
  //   if ( p++ == pos ) {        // ADD
  //     (new TdSourcesDialog(this, this)).show();
  //   } else if ( p++ == pos ) { // DROP
  //     dropSurveys();
  //   } else if ( p++ == pos ) { // VIEW
  //     startTdSurveysActivity();
  //   } else if ( p++ == pos ) { // EQUATES
  //     (new TdEquatesDialog( this, mApp.mConfig, null )).show();
  //   } else if ( p++ == pos ) { // DELETE
  //     askDelete();
  //   }
  // }

  // ----------------------------------------------

  // ---------------------------------------------------------------
  // OPTIONS MENU

  // private MenuItem mMIadd;      // add survey
  // private MenuItem mMIdrop;     // drop survey(s)
  // private MenuItem mMIview;     // open 2D view
  // private MenuItem mMIequates;
  // private MenuItem mMIdelete;   // delete
  // // private MenuItem mMIoptions;

  // @Override
  // public boolean onCreateOptionsMenu(Menu menu) 
  // {
  //   super.onCreateOptionsMenu( menu );

  //   mMIadd     = menu.add( R.string.menu_add );
  //   mMIdrop    = menu.add( R.string.menu_drop );
  //   mMIview    = menu.add( R.string.menu_view );
  //   mMIequates = menu.add( R.string.menu_equates );
  //   mMIdelete  = menu.add( R.string.menu_delete);
  //   // mMIoptions = menu.add( R.string.menu_options );
  //   return true;
  // }

  // @Override
  // public boolean onOptionsItemSelected(MenuItem item) 
  // {
  //   // if ( item == mMIoptions ) { // OPTIONS DIALOG
  //   //   // Intent optionsIntent = new Intent( this, TopoDroidPreferences.class );
  //   //   // optionsIntent.putExtra( TopoDroidPreferences.PREF_CATEGORY, TopoDroidPreferences.PREF_CATEGORY_ALL );
  //   //   // startActivity( optionsIntent );
  //   // } else 
  //   if ( item == mMIdelete ) { 
  //     askDelete();
  //   } else if ( item == mMIadd ) { 
  //     (new TdSourcesDialog(this, this)).show();
  //   } else if ( item == mMIdrop ) { 
  //     dropSurveys();
  //   } else if ( item == mMIview ) { 
  //     startTdSurveysActivity();
  //   } else if  ( item == mMIequates ) { 
  //     (new TdEquatesDialog( this, mApp.mConfig, null )).show();
  //   }
  //   return true;
  // }

  // ------------------------ DISPLAY -----------------------------
  private void startTdSurveysActivity()
  {
    TdSurvey mySurvey = new TdSurvey( "." );

    for ( TdInput input : mApp.mConfig.mInputs ) {
      if ( input.isChecked() ) {
        input.loadSurveyData ( mData );
        mySurvey.addSurvey( input );
        // Log.v("TdManager", "parse file " + input.getSurveyName() );
        // TdParser parser = new TdParser( mData, input.getSurveyName(), mySurvey );
      }
    }
    if ( mySurvey.mSurveys.size() == 0 ) {
      Toast.makeText( this, "no surveys", Toast.LENGTH_SHORT ).show();
      return;
    }
    mApp.mViewSurveys = new ArrayList< TdSurvey >(); // list of displayed surveys
    for ( TdSurvey survey : mySurvey.mSurveys ) {
      survey.reduce();
      mApp.mViewSurveys.add( survey );
    }
    // TODO start drawing activity with reduced surveys
    Intent intent = new Intent( this, TdViewActivity.class );
    startActivity( intent );
  }

  // ------------------------ ADD ------------------------------
  // called by TdSourcesDialog with a list of sources filenames
  //
  void addSources( List<String> surveynames )
  {
    for ( String name : surveynames ) {
      // Log.v("TdManager", "add  source " + name );
      mTdInputAdapter.add( new TdInput( name ) ) ;
    }
    updateList();
  }

  // ------------------------ DELETE ------------------------------
  private void askDelete()
  {
    new TdAlertDialog( this, getResources(), 
                             getResources().getString( R.string.ask_delete_tdconfig ),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick( DialogInterface dialog, int btn ) {
          doDelete();
        }
      }
    );
  }

  void doDelete()
  {
    // if ( ! TdManagerApp.deleteTdConfigFile( mApp.mConfig.getFilepath() ) ) { 
    //   Toast.makeText( this, "delete FAILED", Toast.LENGTH_LONG ).show();
    // } else {
      doFinish( TdManagerApp.RESULT_THCONFIG_DELETE );
    // }
  }

  void doFinish( int result )
  {
    Intent intent = new Intent();
    if ( mApp.mConfig != null ) {
      intent.putExtra( TdManagerApp.THCONFIG_PATH, mApp.mConfig.getFilepath() );
    } else {
      intent.putExtra( TdManagerApp.THCONFIG_PATH, "no_path" );
    }
    setResult( result, intent );
    finish();
  }
  // ---------------------- DROP SURVEYS ----------------------------
  void dropSurveys()
  {
    new TdAlertDialog( this, getResources(), getResources().getString( R.string.title_drop ), 
      new DialogInterface.OnClickListener() {
	@Override
	public void onClick( DialogInterface dialog, int btn ) {
          ArrayList< TdInput > inputs = new ArrayList< TdInput >();
          final Iterator it = mApp.mConfig.mInputs.iterator();
          while ( it.hasNext() ) {
            TdInput input = (TdInput) it.next();
            if ( ! input.isChecked() ) {
              inputs.add( input );
            } else {
              String survey = input.getSurveyName();
              // Log.v("TdManager", "drop survey >" + survey + "<" );
              mApp.mConfig.dropEquates( survey );
            }
          }
          mApp.mConfig.mInputs = inputs;
          updateList();
	} 
    } );
  }

  // ---------------------- SAVE -------------------------------------

  @Override
  public void onBackPressed()
  {
    // Log.v("TdManager", "TdConfig activity back pressed");
    // if ( mApp.mConfig != null ) mApp.mConfig.writeTdConfig( false );
    doFinish( TdManagerApp.RESULT_THCONFIG_OK );
  }

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
    if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // ADD
      (new TdSourcesDialog(this, this)).show();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // DROP
      dropSurveys();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // VIEW
      startTdSurveysActivity();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // EQUATES
      (new TdEquatesDialog( this, mApp.mConfig, null )).show();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // 3D
      try {
        Intent intent = new Intent( "Cave3D.intent.action.Launch" );
        intent.putExtra( "survey", mApp.mConfig.getFilepath() );
        startActivity( intent );
      } catch ( ActivityNotFoundException e ) {
        Toast.makeText( this, "Missing Cave3D", Toast.LENGTH_SHORT ).show();
      }
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // EXPORT
      if ( mApp.mConfig != null ) {
        new ExportDialog( this, this, mExportTypes, R.string.title_export ).show();
      }
    // } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // EXTERNAL EDIT
    //   if ( mApp.mConfig != null ) {
    //     try {
    //       Intent intent = new Intent( Intent.ACTION_EDIT );
    //       Uri uri = Uri.fromFile( new File( mApp.mConfig.getFilepath() ) );
    //       intent.setDataAndType( uri, "text/plain" );
    //       startActivity( intent );
    //     } catch ( ActivityNotFoundException e ) {
    //       Toast.makeText( this, "Missing edit apk", Toast.LENGTH_SHORT ).show();
    //     }
    //   }
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // DELETE
      askDelete();
    } else if ( k1 < mNrButton1 && b0 == mButton1[k1++] ) {  // EXIT
      onBackPressed();
    }
  }

  void doExport( String type, boolean overwrite )
  {
    String filepath = null;
    if ( type.equals("Therion") ) {
       filepath = mApp.mConfig.exportTherion( overwrite );
    } else if ( type.equals("Survex") ) {
       filepath = mApp.mConfig.exportSurvex( overwrite );
    }
    if ( filepath != null ) {
      Toast.makeText( this, String.format( getResources().getString(R.string.exported), filepath ),
            Toast.LENGTH_SHORT ).show();
    } else {
      Toast.makeText( this, R.string.export_failed, Toast.LENGTH_SHORT ).show();
    }
  }


  // @Override
  // public void onItemClick( AdapterView<?> parent, View view, int pos, long id )
  // {
  //   CharSequence item = ((TextView) view).getText();
  //   if ( mMenu == (ListView)parent ) {
  //     handleMenu( pos );
  //     return;
  //   }
  //   if ( onMenu ) {
  //     closeMenu();
  //     return;
  //   }
  // }

}
