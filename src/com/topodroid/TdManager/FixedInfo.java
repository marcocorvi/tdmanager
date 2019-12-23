/* @file FixedInfo.java
 *
 * @author marco corvi
 * @date apr 2012
 *
 * @brief TopoDroid fixed stations (GPS-localized stations)
 * --------------------------------------------------------
 *  Copyright This software is distributed under GPL-3.0 or later
 *  See the file COPYING.
 * --------------------------------------------------------
 */
package com.topodroid.TdManager;

import java.util.Locale;

/** fixed (GPS) point
 * Note the order of data: LONGITUDE - LATITUDE - ALTITUDE
 *
 * MagLatLong is a struct with two fields: lat and lon
 */
class FixedInfo extends MagLatLong
{
  final static long SRC_UNKNOWN    = 0L;
  final static long SRC_TOPODROID  = 1L;
  final static long SRC_MANUAL     = 2L;
  final static long SRC_MOBILE_TOP = 3L;
  long   id;       // fixed id
  long   source;   // 0: unknown,  1: topodroid,  2: manual,   3: mobile-topographer
  String name;     // station name, or whatever
  // double lat;      // latitude [decimal deg]
  // double lng;      // longitude [decimal deg]
  double alt;      // wgs84 altitude [m]
  double asl;      // geoid altitude [m] 
  String comment;
  String cs;
  private double cs_lng;
  private double cs_lat;
  private double cs_alt;
  private long   cs_n_dec;

  FixedInfo( long _id, String n, double longitude, double latitude, double h_ellip, double h_geoid,
                    String cmt, long src )
  {
    id = _id;
    name = n;
    lat = latitude;
    lng = longitude;
    alt = h_ellip;
    asl = h_geoid;
    comment = cmt;
    source  = src;
    cs = null;
    cs_lng = 0;
    cs_lat = 0;
    cs_alt = 0;
    cs_n_dec = 2L;
  }

  FixedInfo( long _id, String n, double longitude, double latitude, double h_ellip, double h_geoid,
                    String cmt, long src,
                    String name_cs, double lng_cs, double lat_cs, double alt_cs, long n_dec )
  {
    id = _id;
    name = n;
    lat = latitude;
    lng = longitude;
    alt = h_ellip;
    asl = h_geoid;
    comment = cmt;
    source  = src;
    cs      = name_cs;
    cs_lng  = lng_cs;
    cs_lat  = lat_cs;
    cs_alt  = alt_cs;
    cs_n_dec = (n_dec >= 0)? n_dec : 0;
  }

  // void setCSCoords( String name_cs, double lng_cs, double lat_cs, double alt_cs, long n_dec )
  // {
  //   cs = name_cs;
  //   if ( cs != null && cs.length() > 0 ) {
  //     cs_lng = lng_cs;
  //     cs_lat = lat_cs;
  //     cs_alt = alt_cs;
  //     cs_n_dec = (n_dec >= 0)? n_dec : 0;
  //   }
  // }

  // boolean hasCSCoords() { return ( cs != null && cs.length() > 0 ); }

  // public FixedInfo( long _id, String n, double longitude, double latitude, double h_ellip, double h_geoid )
  // {
  //   id = _id;
  //   name = n;
  //   lat = latitude;
  //   lng = longitude;
  //   alt = h_ellip;
  //   asl = h_geoid;
  //   comment = "";
  // }

  // get the string "name long lat alt" for the exports
  // String csName() { return cs; }

}
