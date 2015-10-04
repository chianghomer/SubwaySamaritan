package samaritan.wearhacks.ca.subwaysamaritan;

/**
 * Created by HenryChiang on 15-10-04.
 */

import android.net.Uri;

/**
 * All NDEV configuration parameters can be set here.
 *
 * Copyright (c) 2015 Nuance Communications. All rights reserved.
 */
public class Config {

    //All fields are required.
    //Your credentials can be found in your NDEV portal, under "Manage My Apps".
    public static byte[] APP_KEY = {(byte)0xaf, (byte)0x59, (byte)0x60, (byte)0xdb, (byte)0x16, (byte)0x85, (byte)0xdc, (byte)0x2a, (byte)0xe8, (byte)0xf5, (byte)0xdb, (byte)0x68, (byte)0x0d, (byte)0xee, (byte)0xad, (byte)0x02, (byte)0x38, (byte)0x65, (byte)0xef, (byte)0xe8, (byte)0x09, (byte)0xd4, (byte)0xa6, (byte)0x76, (byte)0xa5, (byte)0x84, (byte)0x22, (byte)0xb1, (byte)0x54, (byte)0x4b, (byte)0x78, (byte)0x03, (byte)0xa5, (byte)0xd8, (byte)0xde, (byte)0xbb, (byte)0x5d, (byte)0xbb, (byte)0xe5, (byte)0x45, (byte)0xb0, (byte)0xc1, (byte)0xae, (byte)0xbd, (byte)0xaf, (byte)0x5b, (byte)0xbf, (byte)0xe8, (byte)0xc0, (byte)0xaf, (byte)0xd0, (byte)0xc9, (byte)0x83, (byte)0x76, (byte)0x3f, (byte)0x29, (byte)0xfa, (byte)0x61, (byte)0xda, (byte)0x4e, (byte)0xad, (byte)0x52, (byte)0x52, (byte)0x52};
    public static final String APP_ID = "NMDPTRIAL_chianghomer_hotmail_com20151003001841";
    public static final String SERVER_HOST = "nmsp.dev.nuance.com";
    public static final String SERVER_PORT = "443";

    public static final Uri SERVER_URI = Uri.parse("nmsp://" + APP_ID + "@" + SERVER_HOST + ":" + SERVER_PORT);

    //Only needed if using NLU/Bolt
    public static final String BOLT_MODEL_TAG = "V8_Project783_App362";
}

