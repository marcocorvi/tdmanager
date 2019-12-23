/** @file TdSurvey.java
 *
 * @author marco corvi
 * @date nov 2011
 *
 * @brief Td survey object
 * --------------------------------------------------------
 *  Copyright This sowftare is distributed under GPL-3.0 or later
 *  See the file COPYING.
 */
package com.topodroid.TdManager;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;

import android.util.FloatMath;
import android.util.Log;

public class TdSurvey
{
  private static final String TAG = "TdManager";

  String mName; // survey name = db_name
  TdSurvey mParent;
  TdStation mStartStation;
  SurveyInfo mInfo = null;
  boolean    mLoadedData = false;

  ArrayList< TdShot >    mShots;
  ArrayList< TdStation > mStations;
  ArrayList< TdSurvey >  mSurveys;  // child surveys
  ArrayList< TdEquate >  mEquates;
  // ArrayList< TdFix >     mFixes;

  TdSurvey( String name )
  {
    mName   = name;
    mParent = null;
    mShots    = new ArrayList< TdShot >();
    mStations = null;
    mSurveys  = new ArrayList< TdSurvey >();
    mEquates  = new ArrayList< TdEquate >();
    // mFixes    = new ArrayList< TdFix >();
  }

  TdSurvey( String name, TdSurvey parent )
  {
    mName   = name;
    mParent = parent;
    mShots    = new ArrayList< TdShot >();
    mStations = null;
    mSurveys  = new ArrayList< TdSurvey >();
    mEquates  = new ArrayList< TdEquate >();
    // mFixes    = new ArrayList< TdFix >();
  }

  TdSurvey getSurvey( String[] ns, int pos )
  {
    String name = ns[pos];
    for ( TdSurvey s : mSurveys ) {
      if ( name.equals( s.mName ) ) {
        if ( pos == 0 ) return s;
        return s.getSurvey( ns, pos-1 );
      }
    }
    return null;
  }

  void loadSurveyData( TdDataHelper data ) 
  {
    if ( mLoadedData ) return;
    if ( mInfo == null ) {
      mInfo = data.getSurveyInfo( mName );
    }
    if ( mInfo != null ) {
      List<DBlock> blks = data.getSurveyData( mInfo.id );
      for ( DBlock blk : blks ) {
        addShot( blk.mFrom, blk.mTo, blk.mLength, blk.mBearing, blk.mClino, blk.mExtend );
      }
      mLoadedData = true;
    } else {
      Log.v("TdManager", "Survey " + mName + ": unable to get survey info");
    }
  }

  // void addFix( TdFix fix ) { mFixes.add( fix ); }

  void addEquate( TdEquate equate ) { mEquates.add( equate ); }

  void addSurvey( TdSurvey survey )
  {
    mSurveys.add( survey );
    survey.mParent = this;
  }

  String getName()   { return mName; }

  String getFullName() 
  { 
    if ( mParent != null ) {
      return mName + "." + mParent.getFullName();
    }
    return mName;
  }

  long getId() 
  {
    if ( mInfo == null ) return -1;
    return mInfo.id;
  }

  /** data reduction
   * data reduction consumes the equates that are resolved inside the survey stations
   * without considering the child surveys
   */
  void reduce()
  {
    computeStations();
    for ( TdSurvey s : mSurveys ) s.reduce();
  }

  // void addShot( TdShot shot ) { mShots.add( shot ); }

  void addShot( String from, String to, float d, float b, float c, int e )
  {
    mShots.add( new TdShot( from, to, d, b, c, e, this ) );
  }

  /** get a station by the the name
   * @param name    station name
   */
  TdStation getStation( String name )
  {
    for ( TdStation st : mStations ) {
      if ( st.mName.equals( name ) ) return st;
    }
    return null;
  }

  // ---------------------------------------------------------------

  private void computeStations()
  {
    mStations = new ArrayList< TdStation >();
    mStartStation = null;
    if ( mShots.size() == 0 ) return;

    // reset shots stations
    for ( TdShot sh : mShots ) sh.setStations( null, null );

    TdStation fs=null, ts=null;
    boolean repeat = true;
    while ( repeat ) {
      repeat = false;
      for ( TdShot sh : mShots ) {
        if ( sh.mFromStation != null ) continue; // shot already got stations
        if ( mStartStation == null ) {
          fs = new TdStation( sh.mFrom, 0, 0, 0, 0, this );
	  mStartStation = fs;
          mStations.add( mStartStation );
          // angles are already in radians
          float h = (float)Math.cos( sh.mClino ) * sh.mLength;
          float v = (float)Math.sin( sh.mClino ) * sh.mLength;
          float e =   h * (float)Math.sin( sh.mBearing );
          float s = - h * (float)Math.cos( sh.mBearing );
          ts = new TdStation( sh.mTo, e, s, h*sh.mExtend, v, this );
          mStations.add( ts );
          sh.setStations( fs, ts );
          repeat = true;
        } else {
          fs = getStation( sh.mFrom );
          ts = getStation( sh.mTo );
          if ( fs != null ) {
            if ( ts == null ) {  // FROM --> TO 
              float h = (float)Math.cos( sh.mClino ) * sh.mLength;
              float v = (float)Math.sin( sh.mClino ) * sh.mLength;
              float e =   h * (float)Math.sin( sh.mBearing );
              float s = - h * (float)Math.cos( sh.mBearing );
              ts = new TdStation( sh.mTo, fs.e+e, fs.s+s, fs.h+h*sh.mExtend, fs.v+v, this );
              mStations.add( ts );
              repeat = true;
            } else {
	      // skip: both shot stations exist
	    }
            sh.setStations( fs, ts );
          } else if ( ts != null ) {
            float h = (float)Math.cos( sh.mClino ) * sh.mLength;
            float v = (float)Math.sin( sh.mClino ) * sh.mLength;
            float e =   h * (float)Math.sin( sh.mBearing );
            float s = - h * (float)Math.cos( sh.mBearing );
            fs = new TdStation( sh.mFrom, ts.e-e, ts.s-s, ts.h-h*sh.mExtend, ts.v-v, this );
            mStations.add( fs );
            sh.setStations( fs, ts );
            repeat = true;
          } else { 
	    // the two shot stations do not exist: check equates
	    boolean skip_equate = false;
	    for ( TdEquate eq : mEquates ) {
	      if ( skip_equate ) break;
	      if ( eq.contains( sh.mFrom ) ) {
		for ( String st : eq.mStations ) if ( ! st.equals( sh.mFrom  ) ) {
		  if ( ( fs = getStation( st ) ) != null ) {
                    float h = (float)Math.cos( sh.mClino ) * sh.mLength;
                    float v = (float)Math.sin( sh.mClino ) * sh.mLength;
                    float e =   h * (float)Math.sin( sh.mBearing );
                    float s = - h * (float)Math.cos( sh.mBearing );
                    ts = new TdStation( sh.mTo, fs.e+e, fs.s+s, fs.h+h*sh.mExtend, fs.v+v, this );
                    mStations.add( ts );
                    sh.setStations( fs, ts );
		    skip_equate = true;
		    break;
		  }
		}
              } else if ( eq.contains( sh.mTo ) ) {
	        for ( String st : eq.mStations ) if ( ! st.equals( sh.mTo ) ) {
		  if ( ( ts = getStation( st ) ) != null ) {
                    float h = (float)Math.cos( sh.mClino ) * sh.mLength;
                    float v = (float)Math.sin( sh.mClino ) * sh.mLength;
                    float e =   h * (float)Math.sin( sh.mBearing );
                    float s = - h * (float)Math.cos( sh.mBearing );
                    fs = new TdStation( sh.mFrom, ts.e-e, ts.s-s, ts.h-h*sh.mExtend, ts.v-v, this );
                    mStations.add( fs );
                    sh.setStations( fs, ts );
		    skip_equate = true;
		    break;
                  }
                }
              }
            }
	    if ( skip_equate ) repeat = true;
	  }
        }
      }
    }
  }

}


