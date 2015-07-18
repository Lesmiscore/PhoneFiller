package com.nao20010128nao.PhoneFiller;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.util.*;

public class MainActivity extends Activity
{
	ProgressBar iss,scs;
	Button start,stop;
	File system,sd;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		iss=(ProgressBar)findViewById(R.id.pbIss);
		scs=(ProgressBar)findViewById(R.id.pbScs);
		start=(Button)findViewById(R.id.startFill);
		stop=(Button)findViewById(R.id.stopFill);
		system=Environment.getDataDirectory();
		sd=Environment.getExternalStorageDirectory();
		
		final long all=getTotalSize(system+"");
		iss.setMax(((Long)all).intValue());
		
		new Thread(){
			public void run(){
				while(true){
					update();
					try{
						Thread.sleep(100);
					}catch (InterruptedException e){
						
					}
				}
			}
			public void update(){
				runOnUiThread(new Runnable(){
					public void run(){
						long used=all-getAvailableSize(system+"");
						Log.d("update",""+((double)used/all));
						iss.setProgress(((Long)used).intValue());
						getWindow().getDecorView().invalidate();
					}
				});
			}
		}.start();
    }
	// 総容量(トータルサイズ)を取得する
	public static long getTotalSize(String path){
		long size = -1;

		if( path != null ){
			StatFs fs = new StatFs(path);

			long bkSize = fs.getBlockSize();
			long bkCount = fs.getBlockCount();

			size = bkSize * bkCount;
		}
		return size;
	}

	// 空き容量(利用可能)を取得する
	public static long getAvailableSize(String path){
		long size = -1;

		if( path != null ){
			StatFs fs = new StatFs(path);

			long blockSize = fs.getBlockSize();
			long availableBlockSize = fs.getAvailableBlocks();

			size = blockSize * availableBlockSize;
		}
		return size;
	}
}
