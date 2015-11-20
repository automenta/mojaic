package ai.cogmission.mosaic;

import org.junit.Test;

import static org.junit.Assert.*;


public class SurfaceImplTest {

	@Test
		public void testAddLayoutContentsAbsolute() {
			//Test normal adding
			Surface<Object> si = new SurfaceImpl<Object>();
			si = si.addAbsolute("test", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
			assertNotNull(si);
			
			//Test abnormal adding of absolute to a surface in relative mode
			si = new SurfaceImpl<Object>();
			si = si.addRelative("test", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
			assertNotNull(si);
			try {
				si.addAbsolute("test2", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
				fail();
			}catch(Exception e) {
				assertEquals("Cannot add absolute once a relative specification has been added.", e.getMessage());
			}
		}
	
	@Test
		public void testAddLayoutContentsRelative() {
			//Test normal adding
			Surface<Object> si = new SurfaceImpl<Object>();
			si = si.addRelative("test", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
			assertNotNull(si);
			
			//Test abnormal adding of relative to a surface in absolute mode
			si = new SurfaceImpl<Object>();
			si = si.addAbsolute("test", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
			assertNotNull(si);
			try {
				si.addRelative("test2", new Object(), 0, 0, 0, 0, 0, 0 , 0 , 0);
				fail();
			}catch(Exception e) {
				assertEquals("Cannot add relative once an absolute specification has been added.", e.getMessage());
			}
			
			
		}
	
}
