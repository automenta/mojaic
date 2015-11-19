package ai.cogmission.mosaic.refimpl.pivot;

import ai.cogmission.mosaic.*;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.wtk.*;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Mouse.Button;
import org.apache.pivot.wtk.Window;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Random;

import org.apache.pivot.wtk.Mouse.Button;

public class MosaicPaneRefImpl extends Application.Adapter {

	private Window window;
	private ComponentKeyListener listener;
	private String shiftKey = "";
	
	private int addId = 1000; 
	
	private java.util.Map<String, Component> clientMap = new java.util.HashMap<String, Component>();
	
	/** Holds colors to use for component illustration */
	private Color[] colors = new Color[] { Color.blue, Color.red, Color.green, Color.yellow, Color.orange };
	/** Used to randomize ui element colors */
	private Random random;
	
	public MosaicPaneRefImpl() {
		window = new Window();
		random = new Random();
	}
	
	
	@Override
    public void startup(final Display display, Map<String, String> map) throws Exception {
		MosaicPane mosaicPane = new MosaicPane();
		mosaicPane.getComponentKeyListeners().add(getKeyListener());
		
		ModelLoader loader = new ModelLoader(map.get("file"));
		String[] model = loader.getModel(map.get("surface"));
		
		System.out.println("model.length = " + model.length);
		
		int i = 0;
		for(String def : model) {
			String[] args = def.split("[\\s]*\\,[\\s]*");
			int offset = args.length > 4 ? args.length - 4 : 0;
			String id = args.length == 4 ? "" + (i++) : args[0];
			Label l = getLabel(i > 4 ? colors[random.nextInt(5)] : colors[i], id);
			mosaicPane.add(l, id, 
				Double.parseDouble(args[offset + 0]), 
				Double.parseDouble(args[offset + 1]),
				Double.parseDouble(args[offset + 2]),
				Double.parseDouble(args[offset + 3]));
			clientMap.put(id, l);
		}
		
		//Now that we've added the definitions and components to the Surface we can add it to the Engine.
		mosaicPane.getEngine().addSurface(mosaicPane.getSurface());
		
		window.setContent(mosaicPane);
        window.setTitle("Mosaic Layout Engine Demo (Pivot)");
        window.setMaximized(true);
        window.open(display);
        
        addMouseHandler(mosaicPane);
    }
	
	public void addMouseHandler(final MosaicPane mp) {
		final Surface<Component> surface = mp.getSurface();
		final MosaicEngine<Component> engine = mp.getEngine();
		
		mp.getContainerMouseListeners().add(new ContainerMouseListener.Adapter() {
			@Override
			public boolean mouseDown(Container arg0, Button arg1, int x, int y) {
				if(x < 50 && y < 50) {
					String json = surface.serialize();
					final Surface<Component> serializedSurface = surface.deSerialize(json);
					engine.addSurface(serializedSurface);
					
					final MosaicPane mosaicPane = new MosaicPane(engine, serializedSurface);
					mosaicPane.getComponentKeyListeners().add(getKeyListener());
					mosaicPane.getContainerMouseListeners().add(new ContainerMouseListener.Adapter() {
						public boolean mouseDown(Container c, Button b, int x, int y) {
							if(x < 50 && y < 50) {
								LayoutImpl<Component> l = ((SurfaceImpl<Component>)serializedSurface).copyLayout();//Make sure we don't leave this as public
								surface.setLayout(l);
								((SurfaceImpl<Component>)surface).updateLayoutSerializables(false);
								
								mp.removeAll();
								Sequence<Component> sequence = mosaicPane.removeAll();
								int len = sequence.getLength();
								for(int i = 0;i < len;i++) {
									mp.add(sequence.get(i));
								}
								mosaicPane.getWindow().close();
								mp.invalidate();
							}
							return false;
						}
					});
					
					for(String id : clientMap.keySet()) {
						mosaicPane.addSerialized(getLabel(colors[random.nextInt(5)], id), id);
					}
					
					Dialog window2 = new Dialog();
			        window2.setContent(mosaicPane);
			        window2.setSize(new Dimensions(1600, 1700));
			        window2.setWidthLimits(1000, 5000);
			        window2.setHeightLimits(500, 5000);
			        window2.open(window);
				}else{
					if(!arg1.equals(Mouse.Button.RIGHT)) {
						if(shiftKeyPressed()) {
							Component c = arg0.getComponentAt(x, y);
							System.out.println("calling requestAdd --> c = " + c);
							Position p = getClickQuadrant(c, x, y);
							if(p != null && c != null) {
								Component source = getLabel(colors[random.nextInt(5)], "" + addId);
								System.out.println("calling surface.requestAdd --> c = " + c + ", p = " + p);
								surface.requestAdd(source, "" + addId, c, p);
								clientMap.put("" + addId, source);
								++addId;
							}
						}
					}
				}
				return false;
			}

			@Override
			public boolean mouseUp(Container arg0, Button arg1, int arg2, int arg3) {
				releaseShift();
				return false;
			}
			
			@Override
			public boolean mouseMove(Container container, int x, int y) {
				mp.requestFocus();
				return false;
			}
		});
	}
	
	private Position getClickQuadrant(Component c, int x, int y) {
		if(c == null) return null; //probably a divider
		
		Bounds b = c.getBounds();
		x -= b.x;
		y -= b.y;
		if(x < b.width / 4 && y > b.height / 3 && y < b.height - (b.height / 3)) {
			return Position.WEST;
		}else if(x > b.width - (b.width / 4) && y > b.height / 3 && y < b.height - (b.height / 3)) {
			return Position.EAST;
		}else if(y < b.height / 4 && x > b.width / 3 && x < b.width - (b.width / 3)) {
			return Position.NORTH;
		}else if(y > b.height - (b.height / 4) && x > b.width / 3 && x < b.width - (b.width / 3)) {
			return Position.SOUTH;
		}
		System.out.println("c = " + c + ", x = " + x + ", y = " + y + ",  c bounds = " + b);
		return null;
	}
	
	private ComponentKeyListener getKeyListener() {
		if(listener == null) {
			listener = new ComponentKeyListener.Adapter() {
				Component source = null;
				
				@Override
				public boolean keyTyped(Component component, char character) {
					releaseShift();
					return false;
				}
				
				@Override
				public boolean keyPressed(Component paramComponent, int paramInt,
						KeyLocation paramKeyLocation) {
					
					System.out.println("keyPressed paramComp = " + paramComponent);
					System.out.println("keyPressed paramInt = " + paramInt);
					System.out.println("keyPressed paramKeyLocation " + paramKeyLocation);
					if(paramKeyLocation == KeyLocation.LEFT && paramInt == 16) {
						System.out.println("press shift");
						pressShift();
					}else if(paramKeyLocation == KeyLocation.STANDARD && paramInt == 9) {
						System.out.println("press tab");
						MosaicPane pane = (MosaicPane)paramComponent;
						Label l = (Label)clientMap.get(pane.getSurface().getCursor());
						source = l;
						pane.getSurface().requestMoveBegin(l);
					}else if(paramKeyLocation == KeyLocation.STANDARD && paramInt == 10) {
						MosaicPane pane = (MosaicPane)paramComponent;
						pane.getSurface().requestMoveCommit(source, null, null);
					}else if(paramKeyLocation == KeyLocation.STANDARD && paramInt == 27) {
						MosaicPane pane = (MosaicPane)paramComponent;
						pane.getSurface().requestMoveCancel(source);
					}else if(paramKeyLocation == KeyLocation.STANDARD && paramInt >= 37 && paramInt <= 40) {
						moveCursor((MosaicPane)paramComponent, source, paramInt);
					}else{
						System.out.println("release shift");
						releaseShift();
					}
					return false;
				}	
				@Override
				public boolean keyReleased(Component paramComponent, int paramInt,
						KeyLocation paramKeyLocation) {
					
					if(paramKeyLocation == KeyLocation.LEFT && paramInt == 16) {
						System.out.println("release shift");
						releaseShift();
					}else if(paramKeyLocation == KeyLocation.STANDARD && paramInt == 9) {
						System.out.println("release tab");
						
					}else{
						System.out.println("release shift");
						releaseShift();
					}
					
					return false;
				}
			};
		}
		
		return listener;
	}
	
	private void moveCursor(MosaicPane pane, Component source, int dir) {
		switch(dir) {
			case 37 : pane.getSurface().cursorLeft();break;
			case 38 : pane.getSurface().cursorUp();break;
			case 39 : pane.getSurface().cursorRight();break;
			case 40 : pane.getSurface().cursorDown();break;
		}
		Label l = (Label)clientMap.get(pane.getSurface().getCursor());
		System.out.println("component at cursor = " + l);
		pane.getSurface().requestMoveCancel(source);
		pane.getSurface().requestMoveBegin(l);
	}
	
	private void pressShift() {
		this.shiftKey = "pressed";
	}
	
	private void releaseShift() {
		this.shiftKey = "";
	}
	
	public boolean shiftKeyPressed() {
		return shiftKey.equals("pressed");
	}
	
	public Label getLabel(Color c, String text) {
		Label label = new Label();
        label.setText(text);
        label.getStyles().put("font", new Font("Arial", Font.BOLD, 24));
        label.getStyles().put("color", Color.WHITE);
        label.getStyles().put("backgroundColor", c);
        label.getStyles().put("horizontalAlignment", HorizontalAlignment.CENTER);
        label.getStyles().put("verticalAlignment", VerticalAlignment.CENTER);
        
        return label;
	}
	public static void main(String[] args) throws MalformedURLException {
		if(args == null || args.length < 1 || args[0] == null) {
            //URL url = MosaicPaneRefImpl.class.getResource("testModel.txt");
			URL url = new URL("file:///tmp/model.txt");;
            String path = Paths.get(url.toExternalForm()).toAbsolutePath().toString();
            try{
                path = Paths.get(url.toURI()).toAbsolutePath().toString();
            }catch(Exception e) { e.printStackTrace(); }
            
            args = new String[] { "--file="+path, "--surface=test"};
        }
		
		DesktopApplicationContext.main(MosaicPaneRefImpl.class, args);
    }
}

