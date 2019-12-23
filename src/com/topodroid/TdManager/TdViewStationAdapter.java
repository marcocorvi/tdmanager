/** @file TdViewStationAdapter.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief survey station display adapter
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
import android.view.View.OnClickListener;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.util.Log;

class TdViewStationAdapter extends ArrayAdapter< TdViewStation >
                           implements OnClickListener
{
  private ArrayList< TdViewStation > mItems;
  private Context mContext;
  private LayoutInflater mLayoutInflater;
  private TextView mTextView;
  private TdViewCommand mCommand;

  public TdViewStationAdapter( Context ctx, int id, ArrayList< TdViewStation > items, TextView text, TdViewCommand command )
  {
    super( ctx, id, items );
    mContext = ctx;
    mLayoutInflater = (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    mItems = items;
    mTextView = text;
    mCommand = command;
  }

  public TdStation getCheckedStation( ) 
  { 
    for ( TdViewStation tv : mItems ) {
      if ( tv.isChecked() ) return tv.mStation;
    }
    return null;
  }

  public String getStationName() 
  {
    if ( mTextView.getText() != null ) return mTextView.getText().toString();
    return null;
  }

  public TdViewStation get( int pos ) { return mItems.get(pos); }

  public int size() { return mItems.size(); }

  private class ViewHolder
  { 
    CheckBox cb;
    TdViewStation st;
  }

  @Override
  public View getView( int pos, View convertView, ViewGroup parent )
  {
    TdViewStation b = mItems.get( pos );
    if ( b == null ) return convertView;

    ViewHolder holder = null; 
    if ( convertView == null ) {
      convertView = mLayoutInflater.inflate( R.layout.tdviewstation_adapter, null );
      holder = new ViewHolder();
      holder.cb = (CheckBox) convertView.findViewById( R.id.station );
      holder.cb.setOnClickListener( this );
      convertView.setTag( holder );
    } else {
      holder = (ViewHolder) convertView.getTag();
      if ( holder.st != null ) holder.st.setCheckBox( null );
    }
    holder.st = b;
    b.setCheckBox( holder.cb );
    holder.cb.setText( b.name() );
    // holder.cb.setChecked( b.mChecked );
    return convertView;
  }

  // @Override 
  // public void onItemClick(AdapterView<?> parent, View view, int pos, long id)
  // {
  //   CharSequence item = ((TextView) view).getText();
  //   for ( TdViewStation station : mItems ) {
  //     if ( station.name().equals( item ) ) {
  //       station.setChecked( true );
  //     } else {
  //       station.setChecked( false );
  //     }
  //   }
  // }

  @Override
  public void onClick( View v )
  {
    CheckBox cb = (CheckBox)v;
    CharSequence item = cb.getText();
    for ( TdViewStation station : mItems ) {
      if ( station.name().equals( item ) ) {
        station.setChecked( true );
        if ( mTextView != null ) mTextView.setText( station.name() + "@" + mCommand.name() );
      } else {
        station.setChecked( false );
      }
    }
  }

   

}

