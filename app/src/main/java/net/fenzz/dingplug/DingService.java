package net.fenzz.dingplug;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DingService extends AccessibilityService {
	
	private String TAG = getClass().getSimpleName();
	
	private  boolean  isFinish = false;
	
	public static DingService instance;
	private int index = 1;
	
	/**
	 * 获取到短信通知
	 * 	0.唤醒屏幕
	 * 	1.打开钉钉
	 * 	2.确保当前页是主页界面
	 * 	3.找到“工作”tab并且点击
	 * 	4.确保到达签到页面
	 * 	5.找到签到按钮，并且点击
	 * 	6.判断签到是否成功
	 * 		1.成功，退出程序
	 * 		2.失败，返回到主页，重新从1开始签到
	 */
	
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
//		 final int eventType = event.getEventType();
		 ArrayList<String> texts = new ArrayList<String>();
	        Log.i(TAG, "事件---->" + event.getEventType());
	        
	        
		 if(isFinish){
			return; 
		 }
		
		 AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		 if(nodeInfo == null) {
	            Log.w(TAG, "rootWindow为空");
	            return ;
	      }
//		 nodeInfo.
		 
//		 System.out.println("nodeInfo"+nodeInfo);
		 
	      
		 
		 System.out.println("index:"+index);
		 switch (index) {
		 
		case 1: //进入主页
			 OpenHome(event.getEventType(),nodeInfo);
			break;
		case 2: //进入签到页
			OpenQianDao(event.getEventType(),nodeInfo);
			break;
		case 3:
//			execShellCmd("input tap 500 1000");
			doQianDao(event.getEventType(),nodeInfo);
			System.out.print("点击签到,进入提交页面");
			break;

	    case 4:
			doTijiao(event.getEventType(),nodeInfo);
			break;

		default:
			break;
		}

	}
	private boolean openLuckyMoney(AccessibilityNodeInfo Anode) {
		for (int i = 0; i < Anode.getChildCount(); i++) {
			AccessibilityNodeInfo node = Anode.getChild(i);
			Log.i("ssss","sssss"+node.getClassName());
			if(node.getClassName().equals("android.widget.RelativeLayout")){
				Log.i("ssss","sssss");
				node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
				openLuckyMoney(node);
			}

		}



		return false;
	}
	
	private ArrayList<String> getTextList(AccessibilityNodeInfo node,ArrayList<String> textList){
		if(node == null) {
            Log.w(TAG, "rootWindow为空");
            return null;
        }
		if(textList==null){
			textList = new ArrayList<String>();
		}
		String text = node.getText().toString();
	      if(text!=null&&text.equals("")){
	    	  textList.add(text);
	      }
//	      node.get
		return null;
		
	}


	private void OpenHome(int type,AccessibilityNodeInfo nodeInfo) {
		if(type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
			//判断当前是否是钉钉主页
			List<AccessibilityNodeInfo> homeList = nodeInfo.findAccessibilityNodeInfosByText("工作");
			if(!homeList.isEmpty()){
				//点击
				 boolean isHome = click( "工作");
				 System.out.println("---->"+isHome);
				index = 2;
				System.out.println("点击进入主页签到");
			}
		}
		
	}
	
	private void OpenQianDao(int type,AccessibilityNodeInfo nodeInfo) {
		if(type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
			//判断当前是否是主页的签到页
			List<AccessibilityNodeInfo> qianList = nodeInfo.findAccessibilityNodeInfosByText("工作");
			if(!qianList.isEmpty()){
				 boolean ret = click( "签到");
				 index = 3;
				 System.out.println("点击进入签到页面详情");
			}
			
//			 index = ret?3:1;	
		}
		
	}
	
	
	private void doQianDao(int type,AccessibilityNodeInfo nodeInfo) {
		if(type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
			//判断当前页是否是签到页
//			List<AccessibilityNodeInfo> case1 = nodeInfo.findAccessibilityNodeInfosByText("开启我的签到之旅");
//			if(!case1.isEmpty()){
//				click("开启我的签到之旅");
//				System.out.println("点击签到之旅");
//			}
//
//			List<AccessibilityNodeInfo> case2 = nodeInfo.findAccessibilityNodeInfosByText("我知道了");
//			if(!case2.isEmpty()){
//				click("我知道了");
//				System.out.println("点击我知道对话框");
//			}
//			List<AccessibilityNodeInfo> case3 = nodeInfo.findAccessibilityNodeInfosByText("签到");
//			if(!case3.isEmpty()){
//				Toast.makeText(getApplicationContext(), "发现目标啦！！~~，。，，，", 1).show();
//				System.out.println("发现目标啦。。。。！");
//				click("签到");
//				index=4;
//			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			execShellCmd("input tap 800 800");
			index=4;
		}
	}

	/**
	 * 执行shell命令
	 *
	 * @param cmd
	 */
	private void execShellCmd(String cmd) {

		try {
			// 申请获取root权限，这一步很重要，不然会没有作用
			Process process = Runtime.getRuntime().exec("su");
			// 获取输出流
			OutputStream outputStream = process.getOutputStream();
			DataOutputStream dataOutputStream = new DataOutputStream(
					outputStream);
			dataOutputStream.writeBytes(cmd);
			dataOutputStream.flush();
			dataOutputStream.close();
			outputStream.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	/**
	 * 模拟点击某个指定坐标作用在View上
	 * @param view
	 * @param x
	 * @param y
	 */
	public void clickView(View view, float x, float y)
	{
		long downTime = SystemClock.uptimeMillis();
		final MotionEvent downEvent = MotionEvent.obtain(
				downTime, downTime, MotionEvent.ACTION_DOWN, x, y, 0);
		downTime+=10;
		final MotionEvent upEvent = MotionEvent.obtain(
				downTime, downTime, MotionEvent.ACTION_UP, x, y, 0);
		view.onTouchEvent(downEvent);
		view.onTouchEvent(upEvent);
		downEvent.recycle();
		upEvent.recycle();
	}
	private void doTijiao(int type,AccessibilityNodeInfo nodeInfo) {
		if(type == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			execShellCmd("input tap 0 2468");
			isFinish = true;

		}
	}

	//通过文字点击
	private boolean click(String viewText){
		 AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
		if(nodeInfo == null) {
	            Log.w(TAG, "点击失败，rootWindow为空");
	            return false;
	    }
		List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(viewText);
		if(list.isEmpty()){
			//没有该文字的控件
			 Log.w(TAG, "点击失败，"+viewText+"控件列表为空");
			 return false;
		}else{
			//有该控件
			//找到可点击的父控件
			Log.w(TAG, "点击成功，"+viewText+"控件列表为空");
			AccessibilityNodeInfo view = list.get(0);
			return onclick(view);  //遍历点击
		}
		
	}
	
	private boolean onclick(AccessibilityNodeInfo view){
		if(view.isClickable()){
			view.performAction(AccessibilityNodeInfo.ACTION_CLICK);
			 Log.w(TAG, "点击成功");
			 return true;
		}else{
			
			AccessibilityNodeInfo parent = view.getParent();
			if(parent==null){
				return false;
			}
			onclick(parent);
		}
		return false;
	}
	
	//点击返回按钮事件
	private void back(){
		 performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onServiceConnected() {
		// TODO Auto-generated method stub
		super.onServiceConnected();
		Log.i(TAG, "service connected!");
		Toast.makeText(getApplicationContext(), "连接成功！", 1).show();
		instance = this;
	}
	
	public void setServiceEnable(){
		isFinish = false;
		Toast.makeText(getApplicationContext(), "服务可用开启！", 1).show();
		index = 1;
	}

}
