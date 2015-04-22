package com.example.web.util;

import android.os.Environment;

public class Config {
	public static final int FAILED_TO_SEND = 0;
	public static final int SENT_SUCCESSFULLY = 1;
	public static final int SEND_TIMEOUT = 2;

	public static final String SDCARDPATH= Environment.getExternalStorageDirectory()+"/";
}
