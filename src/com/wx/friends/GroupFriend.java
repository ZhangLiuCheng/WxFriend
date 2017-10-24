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
	private int index = 1;
	
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
		if (index > (groupCount - 3)) {
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
					
					
					// 滚动到顶部
					UiScrollable gridObj = new UiScrollable(new UiSelector().resourceId("com.tencent.mm:id/cns"));
			        Point srcP = new Point(mDevice.getDisplayWidth() / 2, 
			        		mDevice.getDisplayHeight() - 20);
			        Point destP = new Point(mDevice.getDisplayWidth() / 2,
			        		gridObj.getBounds().top + 20);
			        mDevice.swipe(new Point[]{destP, srcP}, 100);
			        
					addFriendsMore();
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
	
	// 添加好友(没有查看更多)
	private void addFriends() throws UiObjectNotFoundException {
		UiScrollable listObj = new UiScrollable(new UiSelector().resourceId("android:id/list"));
		UiObject obj = null;
		UiObject childObj = null;
        for (int i= 0; i < listObj.getChildCount(); i++) {
        	obj = listObj.getChildByInstance(new UiSelector().resourceId("com.tencent.mm:id/byn"), i);
        	if (obj.exists()) {
        		for (int j = 0; j < obj.getChildCount(); j++) {
        			childObj = obj.getChild(new UiSelector().className("android.widget.RelativeLayout").index(j));
        			System.out.println("childObj  "  + childObj.getBounds());
        			childObj.clickAndWaitForNewWindow();
        			realAddFriend();
        			System.out.println("33333");
				}
        	} else {
        		break;
        	}
        }
	}

	int count = 0;
	// 添加好友(点击查看更多)
	private void addFriendsMore() throws UiObjectNotFoundException {
		UiScrollable listObj = new UiScrollable(new UiSelector().resourceId("com.tencent.mm:id/cns"));
		System.out.println("============>   " + listObj.getChildCount());
		UiObject obj = null;
        for (int i= 0; i < listObj.getChildCount(); i++) {
        	System.out.println("aaaaaaa  " + i);

        	obj = listObj.getChildByInstance(new UiSelector().resourceId("com.tencent.mm:id/acz"), i);
        	if (obj.exists()) {
        		
        		UiObject obj1 = listObj.getChildByInstance(new UiSelector().resourceId("com.tencent.mm:id/ad1"), i);
        		if (obj1.exists()) {
        			System.out.println(obj.getBounds()+ "  ----   " + obj1.getText());
        		}
        		count++;

//            	obj.clickAndWaitForNewWindow();
//    			realAddFriend();
        	} else {
        		System.out.println("count  " + count);
        		return;
        	}
        	System.out.println("bbbb " + i);
        }
        
        System.out.println("111111");
        // 滚动到下一页
        Point srcP = new Point(mDevice.getDisplayWidth() / 2, 
        		mDevice.getDisplayHeight() - 20);
        Point destP = new Point(mDevice.getDisplayWidth() / 2,
        		listObj.getBounds().top);
        mDevice.swipe(new Point[]{srcP, destP}, 100);

        System.out.println("22222222");
        sleep(3000);
        
        System.out.println("3333333");
        addFriendsMore();
	}
	
	private void realAddFriend() throws UiObjectNotFoundException {
		// 点击添加到通讯录
        UiObject makeFriendObj = new UiObject(new UiSelector().text("添加到通讯录"));
        if (!makeFriendObj.exists()) {
        	toBack();
        	return;
        }
        makeFriendObj.clickAndWaitForNewWindow();
        
        // 不需要验证直接添加成功
        UiObject makeFriendObjAgain = new UiObject(new UiSelector().text("添加到通讯录"));
        if (makeFriendObjAgain.exists()) {
        	toBack();
        	return;
        }
        
        // 永无无法通过群来添加好友
        UiObject infoObj = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/jf"));
        if (infoObj.exists()) {
            UiObject confirmObj = new UiObject(new UiSelector().text("确定"));
            confirmObj.clickAndWaitForNewWindow();
            toBack();
            return;
        }
        
        // 点击发送按钮
        UiObject sendObj = new UiObject(new UiSelector().text("发送"));
        sendObj.clickAndWaitForNewWindow();
        System.out.println("11111");
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        UiObject makeFriendObjAgain1 = new UiObject(new UiSelector().text("添加到通讯录"));
        if (makeFriendObjAgain1.exists()) {
        	toBack();
        }
        System.out.println("2222");
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
