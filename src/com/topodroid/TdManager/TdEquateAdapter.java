/** @file TdEquateAdapter.java
 *
 * @author marco corvi
 * @date nov 2019
 *
 * @brief adapter of TdEquate items
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.util.Log;

class TdEquateAdapter extends ArrayAdapter< TdEquate >
{
  private ArrayList< TdEquate > mItems;
  private Context mContext;
  private LayoutInflater mLayoutInflater;

  public TdEquateAdapter( Context ctx, int id, ArrayList< TdEquate > items )
  {
    super( ctx, id, items );
    mContext = ctx;
    mLayoutInflater = (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    if ( items != null ) {
      mItems = items;
    } else {
      mItems = new ArrayList< TdEquate >();
    }
  }

  public TdEquate get( int pos ) { return mItems.get(pos); }

  // void addTdEquate( TdEquate equate ) { mItems.add( equate ); }

  public int size() { return mItems.size(); }


  @Override
  public View getView( int pos, View view, ViewGroup parent )
  {
    TdEquate b = mItems.get( pos );
    if ( b == null ) return view;

    TdEquateViewHolder holder = null; 
    if ( view == null ) {
      view = mLayoutInflater.inflate( R.layout.tdequate_adapter, null );
      holder = new TdEquateViewHolder();
      holder.textView = (TextView) view.findViewById( R.id.tdequate );
      view.setTag( holder );
    } else {
      holder = (TdEquateViewHolder) view.getTag();
    }
    holder.equate = b;
    holder.textView.setText( b.stationsString() );
    return view;
  }

  

}

