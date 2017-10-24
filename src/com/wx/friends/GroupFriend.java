package com.wx.friends;

import android.graphics.Point;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;

/**
 * 添加群聊里面的好友.
 */
public class GroupFriend extends UiAutomatorTestCase {
	
	private UiDevice mDevice;
	private int groupCount = -1;
	private int index;
	
	protected void setUp() throws Exception {
		super.setUp();
		mDevice = getUiDevice();
	}

	public void testDemo() throws UiObjectNotFoundException {
		mDevice.pressHome();
		// 打开微信
        UiObject wxApp = new UiObject(new UiSelector().text("微信"));
        wxApp.clickAndWaitForNewWindow();
        
        // 如果不在首界面，返回到首界面
        while (toBack()) {}
        
        fetchGroupList(index);
	}
	
	// 群聊列表
	private void fetchGroupList(int index) throws UiObjectNotFoundException {
		
		 // 点击通讯录
        UiObject fvObj = new UiObject(new UiSelector().text("通讯录"));
        fvObj.clickAndWaitForNewWindow();
        
		// 点击群聊按钮
        UiObject groupObj = new UiObject(new UiSelector().text("群聊"));
        groupObj.clickAndWaitForNewWindow();
        
		UiScrollable listObj = new UiScrollable(new UiSelector().resourceId("com.tencent.mm:id/i9"));
		if (groupCount == -1) {
			groupCount = listObj.getChildCount();
		}
		if (index > (groupCount - 1)) {
			return;
		}
		UiObject obj = listObj.getChildByInstance(new UiSelector().resourceId("com.tencent.mm:id/a5k"), index);
		if (obj.exists()) {
			obj.clickAndWaitForNewWindow();
			UiObject toolBarObj = new UiObject(new UiSelector().description("聊天信息"));
			toolBarObj.clickAndWaitForNewWindow();

			UiObject codeObj = new UiObject(new UiSelector().text("群聊名称"));
			if(codeObj.exists()) {
				// 好友全部在这里面了
				addFriends();
				toBack();
				toBack();
			} else {
				// 向下滚动一点
				scrollLittle(true);
				UiObject codeObj1 = new UiObject(new UiSelector().text("群聊名称"));
				if(codeObj1.exists()) {
					// 向上滚动一点
					scrollLittle(false);
					
					// 好友全部在这里面了
					addFriends();
					toBack();
					toBack();
					return;
				} else {
					// 滚动到下面
					scrollToAllButton();
			        
					// 点查看全部
					UiObject allObj = new UiObject(new UiSelector().text("查看全部群成员"));
					allObj.clickAndWaitForNewWindow();
					addFriends();
					toBack();
					toBack();
					toBack();
				}
			}	
		} else {
			return;
		}
		fetchGroupList(++index);
	}
	
	// 滚动显示查看全部按钮
	private void scrollToAllButton() {
        Point srcP = new Point(mDevice.getDisplayWidth() / 2, 600);
        Point destP = new Point(mDevice.getDisplayWidth() / 2, 200);
        mDevice.swipe(new Point[]{srcP, destP}, 100);
		UiObject allObj = new UiObject(new UiSelector().text("查看全部群成员"));
		if (!allObj.exists()) {
			scrollToAllButton();
		}
	}
	
	private void scrollLittle(boolean down) {
		Point srcP = new Point(mDevice.getDisplayWidth() / 2, 1100);
        Point destP = new Point(mDevice.getDisplayWidth() / 2, 300);
        if (down) {
        	mDevice.swipe(new Point[]{srcP, destP}, 100);
        } else {
        	mDevice.swipe(new Point[]{destP, srcP}, 100);
        }
	}
	
	// 添加好友
	private void addFriends() throws UiObjectNotFoundException {
		UiScrollable listObj = new UiScrollable(new UiSelector().resourceId("android:id/list"));
		UiObject obj = null;
		UiObject childObj = null;
        for (int i= 0; i < listObj.getChildCount(); i++) {
        	obj = listObj.getChildByInstance(new UiSelector().resourceId("android:id/list"), i);
        	if (obj.exists()) {
        		for (int j = 0; j < obj.getChildCount(); j++) {
//        			childObj = obj.getChild()
				}
        	}
        }
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
            	toBack();
        	}
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
	}
}
