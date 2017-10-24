#!/usr/bin/python
#coding: UTF-8
import sys, os

#1.删除旧jar
os.system("rm -rf bin/WXFriends.jar")
print("1.删除 WXFriends.jar")

#2.编译jar
os.system("ant build")
print("2.调用ant，编译WXFriends.jar")

#3.push  WXFriends.jar到sdcard
os.system("adb push bin/WXFriends.jar /sdcard")
print("3.拷贝jar到sdcard")

#4.运行脚本
#自动搜索手机号码添加好友
#os.system("adb shell uiautomator runtest /sdcard/WXFriends.jar -c com.wx.friends.PhoneFriend")
#将好友保存到文件
#os.system("adb shell uiautomator runtest /sdcard/WXFriends.jar -c com.wx.friends.FriendsSave")
#从文件中添加好友
#os.system("adb shell uiautomator runtest /sdcard/WXFriends.jar -c com.wx.friends.AccountFriend")
#群聊里面添加好友
os.system("adb shell uiautomator runtest /sdcard/WXFriends.jar -c com.wx.friends.GroupFriend")

print("4.开始运行测试脚本")
