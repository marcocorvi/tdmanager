/** @file TdSourceAdapter.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief adapter for source files
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.util.Log;

class TdSourceAdapter extends ArrayAdapter< TdSource >
{
  private ArrayList< TdSource > mItems;
  private Context mContext;
  private LayoutInflater mLayoutInflater;

  public TdSourceAdapter( Context ctx, int id, ArrayList< TdSource > items )
  {
    super( ctx, id, items );
    mContext = ctx;
    mLayoutInflater = (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    if ( items != null ) {
      mItems = items;
    } else {
      mItems = new ArrayList< TdSource >();
    }
  }

  public TdSource get( int pos ) { return mItems.get(pos); }

  public TdSource get( String name ) 
  {
    for ( TdSource source : mItems ) {
      if ( source.getSurveyName().equals( name ) ) return source;
    }
    return null;
  }

  void addTdSource( TdSource source ) { mItems.add( source ); }

  public int size() { return mItems.size(); }

  private class ViewHolder
  { 
    CheckBox checkBox;
    TextView textView;
  }

  /**
   * @return list of filename of checked sources
   */
  ArrayList< String > getCheckedSources()
  {
    ArrayList< String > ret = new ArrayList< String >();
    for ( TdSource source : mItems ) {
      if ( source.isChecked() ) {
        ret.add( source.getSurveyName() );
      }
    }
    return ret;
  }


  @Override
  public View getView( int pos, View convertView, ViewGroup parent )
  {
    // Log.v("TdManager", "get source pos " + pos + "/" + mItems.size() );
    TdSource b = mItems.get( pos );
    if ( b == null ) return convertView;

    ViewHolder holder = null; 
    if ( convertView == null ) {
      convertView = mLayoutInflater.inflate( R.layout.tdsource_adapter, null );
      holder = new ViewHolder();
      holder.checkBox = (CheckBox) convertView.findViewById( R.id.tdsource_checked );
      holder.textView = (TextView) convertView.findViewById( R.id.tdsource_name );
      convertView.setTag( holder );
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.checkBox.setOnClickListener( null );
    holder.checkBox.setChecked( b.isChecked() );
    holder.checkBox.setOnClickListener( b );
    holder.textView.setText( b.getSurveyName() );
    return convertView;
  }

}

