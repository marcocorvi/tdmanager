/** @file TdConfigAdapter.java
 *
 * @author marco corvi
 * @date may 2012
 *
 * @brief th-config activity for a thconfig project-file
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
 */
package com.topodroid.TdManager;

import java.util.ArrayList;
import java.io.File;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.util.Log;

class TdConfigAdapter extends ArrayAdapter< TdConfig >
{
  private ArrayList< TdConfig > mItems;
  private Context mContext;
  private OnClickListener mOnClick;

  public TdConfigAdapter( Context ctx, int id, ArrayList< TdConfig > items, OnClickListener onClick )
  {
    super( ctx, id, items );
    mContext = ctx;
    mItems = items;
    mOnClick = onClick;
    // Log.v( TdManagerApp.TAG, "TdConfigAdapter nr. items " + items.size() );
  }

  public TdConfig get( int pos ) { return mItems.get(pos); }

  public TdConfig get( String survey ) 
  {
    // Log.v("TdManager", "TdConfig get survey >" + survey + "< size " + mItems.size() );
    if ( survey == null || survey.length() == 0 ) return null;
    for ( TdConfig thconfig : mItems ) {
      // Log.v("TdManager", "TdConfig item >" + thconfig.mName + "<" );
      if ( thconfig.getSurveyName().equals( survey ) ) return thconfig;
    }
    return null;
  }

  boolean deleteTdConfig( String filepath )
  {
    File file = new File( filepath );
    boolean ret = file.delete();
    for ( TdConfig thconfig : mItems ) {
      if ( thconfig.getFilepath().equals( filepath ) ) {
        mItems.remove( thconfig );
        break;
      }
    }
    return ret;
  }

  @Override
  public View getView( int pos, View convertView, ViewGroup parent )
  {
    View v = convertView;
    if ( v == null ) {
      LayoutInflater li = (LayoutInflater)mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
      v = li.inflate( R.layout.row, null );
    }

    TdConfig b = mItems.get( pos );
    if ( b != null ) {
      TextView tw = (TextView) v.findViewById( R.id.row_text );
      tw.setText( b.toString() );
      // tw.setTextColor( b.color() );
    }
    v.setOnClickListener( mOnClick );
    return v;
  }

  public int size()
  {
    return mItems.size();
  }

}
