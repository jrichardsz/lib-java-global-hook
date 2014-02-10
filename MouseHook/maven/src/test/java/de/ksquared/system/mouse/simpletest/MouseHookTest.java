package de.ksquared.system.mouse.simpletest;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import com.linet.util.file.FileUtil;
import de.ksquared.system.mouse.GlobalMouseListener;
import de.ksquared.system.mouse.MouseAdapter;
import de.ksquared.system.mouse.MouseEvent;

public class MouseHookTest{

	@Before
	public void setUp() throws Exception{

		String libPath=null;

		try{
			libPath=FileUtil.getPathFromWhereApplicationIsRunning() + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "lib";
		}
		catch(Exception e){
			throw new Exception("Error when try to get path of execution",e);
		}

		// register properties
		System.setProperty("de.ksquared.system.mouse.lib.path",libPath);
		System.setProperty("de.ksquared.system.mouse.lib.name","mousehook");

	}

	@Test
	public void test(){
		new GlobalMouseListener().addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent event){
				System.out.println(event);
			}

			@Override
			public void mouseReleased(MouseEvent event){
				System.out.println(event);
			}

			@Override
			public void mouseMoved(MouseEvent event){
				System.out.println(event);
				if((event.getButtons() & MouseEvent.BUTTON_LEFT) != MouseEvent.BUTTON_NO && (event.getButtons() & MouseEvent.BUTTON_RIGHT) != MouseEvent.BUTTON_NO)
					System.out.println("Both mouse buttons are currenlty pressed!");
			}
		});
		
		while(true)
			try{
				Thread.sleep(100);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
	}
}
