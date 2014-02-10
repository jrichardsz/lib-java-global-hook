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

import java.util.List;
import java.util.Vector;

public class GlobalMouseListener{
	protected PoolHook hook;

	public GlobalMouseListener(){
		(hook=new PoolHook(this)).start();
	}

	protected List<MouseListener> listeners=new Vector<MouseListener>();

	public void addMouseListener(MouseListener listener){
		listeners.add(listener);
	}

	public void removeMouseListener(MouseListener listener){
		listeners.remove(listener);
	}

	void mouseMoved(MouseEvent event){
		try{
			for(MouseListener listener : listeners)
				listener.mouseMoved(event);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	void mousePressed(MouseEvent event){
		try{
			for(MouseListener listener : listeners)
				listener.mousePressed(event);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	void mouseReleased(MouseEvent event){
		try{
			for(MouseListener listener : listeners)
				listener.mouseReleased(event);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void start(){
		hook.getMouseHook().startResume();
	}

	public void stop(){
		hook.getMouseHook().pause();
	}

}
