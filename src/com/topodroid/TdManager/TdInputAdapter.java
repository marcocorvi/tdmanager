/** @file TdInputAdapter.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief adapter for the surveys input objetcs
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 *
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

// import android.util.Log;

class TdInputAdapter extends ArrayAdapter< TdInput >
{
  private ArrayList< TdInput > mItems;
  private Context mContext;
  private LayoutInflater mLayoutInflater;

  public TdInputAdapter( Context ctx, int id, ArrayList< TdInput > items )
  {
    super( ctx, id, items );
    mContext = ctx;
    mLayoutInflater = (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    if ( items != null ) {
      mItems = items;
    } else {
      mItems = new ArrayList< TdInput >();
    }
  }

  public TdInput get( int pos ) { return mItems.get(pos); }

  public TdInput get( String name ) 
  {
    for ( TdInput input : mItems ) {
      if ( input.getSurveyName().equals( name ) ) return input;
    }
    return null;
  }

  public void add( TdInput input ) { mItems.add( input ); }

  public void drop( TdInput input ) { mItems.remove( input ); }

  public void dropChecked( ) 
  {
    final Iterator it = mItems.iterator();
    while ( it.hasNext() ) {
      TdInput input = (TdInput) it.next();
      if ( input.isChecked() ) {
        mItems.remove( input );
      }
    }
  }

  public int size() { return mItems.size(); }


  private class ViewHolder
  { 
    CheckBox checkBox;
    TextView textView;
  }

  @Override
  public View getView( int pos, View convertView, ViewGroup parent )
  {
    TdInput b = mItems.get( pos );
    if ( b == null ) return convertView;

    ViewHolder holder = null; 
    if ( convertView == null ) {
      convertView = mLayoutInflater.inflate( R.layout.tdinput_adapter, null );
      holder = new ViewHolder();
      holder.checkBox = (CheckBox) convertView.findViewById( R.id.tdinput_checked );
      holder.textView = (TextView) convertView.findViewById( R.id.tdinput_name );
      convertView.setTag( holder );
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.checkBox.setOnClickListener( b );
    holder.checkBox.setChecked( b.isChecked() );
    holder.textView.setText( b.getSurveyName() );
    return convertView;
  }

}

