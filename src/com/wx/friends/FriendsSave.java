package com.wx.friends;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class FriendsSave extends UiAutomatorTestCase {
	
	private UiDevice mDevice;
	private int searchNumber;
	private int successNumber;
	private boolean running;
	
	private List<String> mAccounts = new ArrayList<String>();
	
	private BufferedWriter mBufferedWriter;
	
	protected void setUp() throws Exception {
		super.setUp();
		mDevice = getUiDevice();
		running = true;
		
		// 设置键盘输入速度
//		Configurator config= Configurator.getInstance();
//		config.setKeyInjectionDelay(40);
		File rootFile = new File("/sdcard/wx");
		rootFile.mkdirs();
		File file = new File("/sdcard/wx/account.txt");
		System.out.println("文件路径：" + file.getAbsolutePath());

		if (file.exists()) {
			file.delete();
		} else {
			file.createNewFile();
		}
	
		mBufferedWriter = new BufferedWriter(new FileWriter(file));
	}

	public void testDemo() throws UiObjectNotFoundException {
		mDevice.pressHome();
		// 打开微信
        UiObject wxApp = new UiObject(new UiSelector().text("微信"));
        wxApp.clickAndWaitForNewWindow();
        
        // 如果不在首界面，返回到首界面
        while (toBack()) {}
        
        // 点击通讯录
        UiObject fvObj = new UiObject(new UiSelector().text("通讯录"));
        fvObj.clickAndWaitForNewWindow();
        
        fetchAccount();
	}
	
    // 获取通讯好友的list
	private void fetchAccount() throws UiObjectNotFoundException {
		UiScrollable listObj = new UiScrollable(new UiSelector().resourceId("com.tencent.mm:id/i9"));
		UiObject obj = null;
        for (int i= 0; i < listObj.getChildCount(); i++) {
        	obj = listObj.getChildByInstance(new UiSelector().resourceId("com.tencent.mm:id/ir"), i);
        	if (obj.exists()) {
            	obj.clickAndWaitForNewWindow();
            	String account = getAccount();
            	System.out.println(account);
            	if (mAccounts.contains(account)) {
                	toBack();
            		continue;
            	}
        		mAccounts.add(account);
            	try {
					mBufferedWriter.write(account + "\n");
					mBufferedWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
            	toBack();
        	}
        }
        
        // 滚动到下一页
        try {
			mBufferedWriter.write("###\n");
			mBufferedWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        // 滚动到下一页
        UiObject toolBarObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/by2"));
        Point srcP = new Point(mDevice.getDisplayWidth() / 2, 
        		mDevice.getDisplayHeight() - toolBarObj.getBounds().height() - listObj.getBounds().top - 20);
        Point destP = new Point(mDevice.getDisplayWidth() / 2,
        		listObj.getBounds().top);
        mDevice.swipe(new Point[]{srcP, destP}, 100);

        sleep(2000);
        
        fetchAccount();
	}
	
	private String getAccount() {
        UiObject nameObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/nj"));
        UiObject acObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/ahw"));
        
        if (acObj.exists()) {
        	try {
				return nameObj.getText().trim() + ":" + acObj.getText().split(":")[1].trim();
			} catch (UiObjectNotFoundException e) {
				e.printStackTrace();
			}
        }
		return null;
	}
	
	private boolean toBack() {
		UiObject backObj = new UiObject(new UiSelector().description("返回"));
		if(!backObj.exists()) {
			return false;
		}
		try {
			backObj.clickAndWaitForNewWindow();
		} catch (UiObjectNotFoundException e) {
			return false;
		}
		return true;
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		mDevice = null;
		running = false;
		if (null != mBufferedWriter) {
			mBufferedWriter.close();
		}
	}
}
