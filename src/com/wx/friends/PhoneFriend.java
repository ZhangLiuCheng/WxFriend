package com.wx.friends;

import java.util.Random;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

public class PhoneFriend extends UiAutomatorTestCase {
	
	private UiDevice mDevice;
	private int searchNumber;
	private int successNumber;
	private boolean running;
	
	private final String message = "问世间情为何物，直叫人以身相许";
	
	protected void setUp() throws Exception {
		super.setUp();
		mDevice = getUiDevice();
		running = true;
		
		// 设置键盘输入速度
//		Configurator config= Configurator.getInstance();
//		config.setKeyInjectionDelay(40);
	}

	public void testDemo() throws UiObjectNotFoundException {
		mDevice.pressHome();
		
		// 打开微信
        UiObject wxApp = new UiObject(new UiSelector().text("微信"));
        wxApp.clickAndWaitForNewWindow();
        
        // 如果不在首界面，返回到首界面
        while (toBack()) {}
        
        // 点击右上角+
        UiObject fuObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/fu"));
        fuObj.click();
        
        // 点击添加朋友
        UiObject fvObj = new UiObject(new UiSelector().text("添加朋友"));
        fvObj.clickAndWaitForNewWindow();
       
        while (running) {
        	try {
        		searchNumber++;
        		boolean success = addFriend(getRandomPhone());
        		if (success) {
        			successNumber++;
        			System.out.println("成功添加好友： "+ successNumber + "人");
        		}
        		if (successNumber > 100 || searchNumber > 10) {
        			break;
        		}
        	} catch (UiObjectNotFoundException e) {
        		System.out.println("添加好友失败");
        	}
        }
	}
	
	private String getRandomPhone() {
		String number = "139";
        Random random = new Random();
        for (int j = 0; j < 8; j++) {
            number += random.nextInt(9);
        }
        return number;
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
	
	private boolean addFriend(String phone) throws UiObjectNotFoundException {
		// 输入号码
        UiObject serachTextObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/hb"));
        serachTextObj.setText(phone);
        
        // 点击搜索
        UiObject serachObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/jv"));
        serachObj.clickAndWaitForNewWindow();
        
        // 点击添加到通讯录
        UiObject makeFriendObj = new UiObject(new UiSelector().text("添加到通讯录"));
        if (!makeFriendObj.exists()) {
        	toBack();
        	return false;
        }
        makeFriendObj.clickAndWaitForNewWindow();
        
        // 输入验证信息
//        UiObject msgTextObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cl9"));
//        msgTextObj.setText(message);
        
        // 点击发送按钮
        UiObject sendObj = new UiObject(new UiSelector().text("发送"));
        sendObj.clickAndWaitForNewWindow();
        toBack();
        return true;
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
		mDevice = null;
		running = false;
	}
}
