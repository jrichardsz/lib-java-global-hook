/*
 * Copyright 2011 Kristian Kraljic, Johannes Sch�th 2008. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY KRISTIAN KRALJIC AND JOHANNES SCHLUETH ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL KRISTIAN KRALJIC AND JOHANNES SCHLUETH OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and should not be interpreted as representing official policies, either expressed
 * or implied, of Kristian Kraljic and Johannes Sch�th.
 */

package de.ksquared.system.mouse;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class MouseHook{
	private static final int WM_LBUTTONDOWN=513,WM_LBUTTONUP=514,WM_RBUTTONDOWN=516,WM_RBUTTONUP=517;

	private int buttons,x,y;
	private static Dimension size;
	static{
		Rectangle bounds=new Rectangle();
		for(GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices())
			bounds.add(device.getDefaultConfiguration().getBounds());
		size=new Dimension(bounds.width,bounds.height);
	}

	List<MouseEvent> buffer=Collections.synchronizedList(new LinkedList<MouseEvent>());
	private EventProcedure procedure=new EventProcedure(this);

	public MouseHook() throws Exception{
		if(Native.load())
			procedure.start();
	}

	void processButton(int parameter,GlobalMouseListener listener){
		switch(parameter){
		case WM_LBUTTONDOWN:
			buttons|=MouseEvent.BUTTON_LEFT;
			buffer.add(new MouseEvent(this,listener,MouseEvent.TRANSITION_STATE_DOWN,MouseEvent.BUTTON_LEFT,buttons,x,y));
			break;
		case WM_LBUTTONUP:
			buttons&=(~MouseEvent.BUTTON_LEFT);
			buffer.add(new MouseEvent(this,listener,MouseEvent.TRANSITION_STATE_UP,MouseEvent.BUTTON_LEFT,buttons,x,y));
			break;
		case WM_RBUTTONDOWN:
			buttons|=MouseEvent.BUTTON_RIGHT;
			buffer.add(new MouseEvent(this,listener,MouseEvent.TRANSITION_STATE_DOWN,MouseEvent.BUTTON_RIGHT,buttons,x,y));
			break;
		case WM_RBUTTONUP:
			buttons&=(~MouseEvent.BUTTON_RIGHT);
			buffer.add(new MouseEvent(this,listener,MouseEvent.TRANSITION_STATE_UP,MouseEvent.BUTTON_RIGHT,buttons,x,y));
			break;
		}
	}

	void processMove(int x,int y,GlobalMouseListener listener){
		this.x=Math.min(Math.max(0,x),size.width);
		this.y=Math.min(Math.max(0,y),size.height);
		buffer.add(new MouseEvent(this,listener,MouseEvent.TRANSITION_STATE_MOVE,MouseEvent.BUTTON_NO,buttons,this.x,this.y));
	}

	native void registerHook(GlobalMouseListener listener);

	native void unregisterHook();

	public void startResume(){
		procedure.startResume();
	}

	public void pause(){
		procedure.pause();
	}

}

class PoolHook extends Thread{
	private MouseHook hook;
	private GlobalMouseListener listener;

	PoolHook(GlobalMouseListener listener){
		this.setDaemon(true);
		this.listener=listener;
	}

	public void run(){
		try{
			hook=new MouseHook();
		}
		catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		hook.registerHook(listener);
	}

	public MouseHook getMouseHook(){
		return hook;
	}

}

class EventProcedure extends Thread{
	private MouseHook hook;
	private boolean isRunning=true;

	EventProcedure(MouseHook hook){
		this.setDaemon(true);
		this.hook=hook;
	}
	
	public void pause(){
		isRunning = false;
	}
	
	public void startResume(){
		isRunning = true;
	}

	@Override
	public void run(){
		while(true){

			if(isRunning){
				if(!hook.buffer.isEmpty()){
					MouseEvent event=hook.buffer.remove(0);
					GlobalMouseListener listener=event.listener;
					switch(event.transitionState){
					case MouseEvent.TRANSITION_STATE_DOWN:
						listener.mousePressed(event);
						break;
					case MouseEvent.TRANSITION_STATE_UP:
						listener.mouseReleased(event);
						break;
					case MouseEvent.TRANSITION_STATE_MOVE:
						listener.mouseMoved(event);
						break;
					}
				}
				else
					try{
						Thread.sleep(10);
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
			}

		}
	}
}
