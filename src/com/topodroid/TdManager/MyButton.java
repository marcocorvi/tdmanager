/** @file MyButton.java
 *
 * @author marco corvi
 * @date may 2012
 *
 * @brief buttons factory (copied from TopoDroid)
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import android.content.Context;
import android.content.res.Resources;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
// import android.view.View.OnLongClickListener;
// import android.view.MotionEvent;

// import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import android.util.SparseArray;
import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

import java.util.Random;

public class MyButton
{
  // static private int mSize = 48;

  // static Random rand = new Random();

  // CACHE : using a cache for the BitmapDrawing does not dramatically improve perfoormanaces
  // static SparseArray<BitmapDrawable> mBitmapCache = new SparseArray<BitmapDrawable>();

  // called with context = mApp
  // static void resetCache( /* Context context, */ int size )
  // {
  //   mSize = size;
  //   // mBitmapCache.clear();
  //   // Log.v("TdManager", "set size " + size );
  // }

  static Button getButton( Context ctx, OnClickListener click, int size, int res_id )
  {
    Button ret = new Button( ctx );
    ret.setPadding(0,0,0,0);
    ret.setOnClickListener( click );
    ret.setBackgroundDrawable( getButtonBackground( ctx, ctx.getResources(), size, res_id ) );
    return ret;
  }

  static Bitmap getButtonBitmap( Resources res, int size, int res_id )
  {
    Bitmap ret = null;
    try {
      Bitmap bm1 = BitmapFactory.decodeResource( res, res_id );
      ret = Bitmap.createScaledBitmap( bm1, size, size, false );
    } catch ( OutOfMemoryError err ) {
      Log.e("TdManager", "out of memory: " + err.getMessage() );
    }
    return ret;
  }

  static BitmapDrawable getButtonBackground( Context ctx, Resources res, int size, int res_id )
  {
    // Log.v("TdManager", "get bkgnd " + size );
    BitmapDrawable ret = null;
    // ret = mBitmapCache.get( res_id );
    // if ( ret == null ) {    
      try {
        Bitmap bm1 = BitmapFactory.decodeResource( res, res_id );
        Bitmap bmx = Bitmap.createScaledBitmap( bm1, size, size, false );
        ret = new BitmapDrawable( res, bmx );
        // Log.v("TdManager", "bm1 " + bm1.getWidth() + " bmx " + bmx.getWidth() + " size " + size );
        // mBitmapCache.append( res_id, ret );
      } catch ( OutOfMemoryError err ) {
        Log.e("TdManager", "out of memory: " + err.getMessage() );
        Toast toast = Toast.makeText( ctx, "WARNING. Out Of Memroy", Toast.LENGTH_LONG );
        TextView tv = (TextView)toast.getView().findViewById( android.R.id.message );
        tv.setTextColor( 0xffff0000 );
        toast.show();
        // try { 
        //   InputStream is = ctx.getAssets().open("iz_oom.png");
        //   ret = new BitmapDrawable( res, is );
        // } catch ( IOException e ) {
        // }
      }
    // }
    return ret;
  }

}
