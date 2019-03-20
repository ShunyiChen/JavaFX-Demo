package com.maxtree.screenshot;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

public class EnumerateWindows {
	
	static interface User32 extends StdCallLibrary {
		User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

		interface WNDENUMPROC extends StdCallCallback {
			boolean callback(Pointer hWnd, Pointer arg);
		}

		boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

		int GetWindowTextA(Pointer hWnd, byte[] lpString, int nMaxCount);

		int GetWindowInfo(Pointer handle, int[] wininfo);
		
		int IsWindowVisible(Pointer hwnd);
	}

	public static List<javafx.geometry.Rectangle2D> GetWindowsRect() {
		
		List<javafx.geometry.Rectangle2D> rects = new ArrayList<javafx.geometry.Rectangle2D>();
		final User32 user32 = User32.INSTANCE;
		
		user32.EnumWindows(new User32.WNDENUMPROC() {

			@Override
			public boolean callback(Pointer hWnd, Pointer arg) {
				
				int isVisible = user32.IsWindowVisible(hWnd);
				if (isVisible == 1) {
					byte[] windowText = new byte[512];
					user32.GetWindowTextA(hWnd, windowText, 512);
					String wText = Native.toString(windowText).trim();
					if (!wText.isEmpty()) {
 
						int[] wininfo = new int[60];
						wininfo[0] = 60;
						user32.GetWindowInfo(hWnd, wininfo);
						
						if (check(wininfo[7], wininfo[8], new String[]{wText})) {
							javafx.geometry.Rectangle2D r = new javafx.geometry.Rectangle2D(wininfo[5], wininfo[6], (wininfo[7] - wininfo[5]), (wininfo[8] - wininfo[6]));
							rects.add(r);
							
//							System.out.println(wText+"   "+r + "  ");
						}
					}
				}
				
				return true;
			}
		}, null);

		return rects;
	}
	
	private static boolean check(int w, int h, String[] limitedWindowTxts) {
		if (w <= 0 || h <= 0) {
			return false;
		}
		for (String winTxt : limitedWindowTxts) {
			if ("Mode Indicator".equals(winTxt)
					 ) {
				return false;
			} 
		}
		
		return true;
	}
	
	public static void main(String[] args)  {
		
		 GetWindowsRect();
 
	}
}
