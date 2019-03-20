package com.maxtree.screenshot.hotkey;

import java.util.List;

import com.maxtree.screenshot.EditStatus;
import com.maxtree.screenshot.EnumerateWindows;
import com.maxtree.screenshot.Screenshot;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.IntellitypeListener;
import com.melloware.jintellitype.JIntellitype;

import javafx.application.Platform;

public class RegisterHotkey implements HotkeyListener, IntellitypeListener {

	public RegisterHotkey() {
		// Loads dll
		String osArch = System.getProperty("os.arch");
		if (osArch.endsWith("64")) {
			JIntellitype.setLibraryLocation("JIntellitype64.dll");
		} else {
			JIntellitype.setLibraryLocation("JIntellitype32.dll");
		}
	}
	
	/**
	 * Initialize the JInitellitype library making sure the DLL is located.
	 */
	public void initJIntellitype() {
		try {
			// initialize JIntellitype with the frame so all windows commands
			// can
			// be attached to this window
			JIntellitype.getInstance().addHotKeyListener(this);
			JIntellitype.getInstance().addIntellitypeListener(this);
			output("JIntellitype initialized");
		} catch (RuntimeException ex) {
			output("Either you are not on Windows, or there is a problem with the JIntellitype library!");
		}
	}
	
	public void register() {
		JIntellitype.getInstance().registerHotKey(CTRL_ALT_A, JIntellitype.MOD_CONTROL + JIntellitype.MOD_ALT, 'Q');
		JIntellitype.getInstance().registerHotKey(ESC, "ESCAPE");
	}
	
	public void unregister() {
		JIntellitype.getInstance().unregisterHotKey(CTRL_ALT_A);
		JIntellitype.getInstance().unregisterHotKey(ESC);
	}

	/**
	 * Send the output to the log and the text area.
	 * <p>
	 * 
	 * @param text
	 *            the text to output
	 */
	private void output(String text) {
	}

	@Override
	public void onIntellitype(int aCommand) {
		// TODO Auto-generated method stub
		switch (aCommand) {
	      case JIntellitype.APPCOMMAND_BROWSER_BACKWARD:
	         output("BROWSER_BACKWARD message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_FAVOURITES:
	         output("BROWSER_FAVOURITES message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_FORWARD:
	         output("BROWSER_FORWARD message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_HOME:
	         output("BROWSER_HOME message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_REFRESH:
	         output("BROWSER_REFRESH message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_SEARCH:
	         output("BROWSER_SEARCH message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_BROWSER_STOP:
	         output("BROWSER_STOP message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_LAUNCH_APP1:
	         output("LAUNCH_APP1 message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_LAUNCH_APP2:
	         output("LAUNCH_APP2 message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_LAUNCH_MAIL:
	         output("LAUNCH_MAIL message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_MEDIA_NEXTTRACK:
	         output("MEDIA_NEXTTRACK message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_MEDIA_PLAY_PAUSE:
	         output("MEDIA_PLAY_PAUSE message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_MEDIA_PREVIOUSTRACK:
	         output("MEDIA_PREVIOUSTRACK message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_MEDIA_STOP:
	         output("MEDIA_STOP message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_VOLUME_DOWN:
	         output("VOLUME_DOWN message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_VOLUME_UP:
	         output("VOLUME_UP message received " + Integer.toString(aCommand));
	         break;
	      case JIntellitype.APPCOMMAND_VOLUME_MUTE:
	         output("VOLUME_MUTE message received " + Integer.toString(aCommand));
	         break;
	      default:
	         output("Undefined INTELLITYPE message caught " + Integer.toString(aCommand));
	         break;
	      }
	}

	@Override
	public void onHotKey(int identifier) {
		Platform.runLater(new Runnable() {
	        @Override
	        public void run() {
	        	output("WM_HOTKEY message received " + Integer.toString(identifier));
	        	if (CTRL_ALT_A == identifier) {
	        		if (Screenshot.STATUS == EditStatus.Ended) {
	        			List<javafx.geometry.Rectangle2D> windowRects = EnumerateWindows.GetWindowsRect();
	        			sc.snapshot(windowRects);
	        		}
	        	} else if (ESC == identifier) {
	        		if (sc != null) {
	        			sc.quit();
	        		}
	        	}
	        }
	   });
		
	}
	
	public static final int ESC = 86;
	public static final int CTRL_ALT_A = 87;
	private Screenshot sc = new Screenshot();
}
