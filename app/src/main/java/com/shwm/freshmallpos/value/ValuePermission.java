package com.shwm.freshmallpos.value;

import android.Manifest;

public class ValuePermission {

	public static final String PermissionGroupCAMERA = Manifest.permission.CAMERA;
	public static final String PermissionGroupLOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
	public static final String PermissionGroupSTORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
	public static final String PermissionGroupSETTINGS = Manifest.permission.WRITE_SETTINGS;
	public static final int PermissionRequest_CAMERA = 21;
	public static final int PermissionRequest_LOCATION = 22;
	public static final int PermissionRequest_STORAGE = 23;
	public static final int PermissionRequest_SETTINGS = 24;
}
