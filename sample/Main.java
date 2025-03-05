import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.collision.*;
import com.jme3.font.BitmapText;
import com.jme3.input.*;
import com.jme3.input.controls.*;
import com.jme3.light.*;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.*;
import com.jme3.scene.shape.*;
 
public class Main extends SimpleApplication {
 
	public static void main(String[] args) {
 
        Main app = new Main();
		app.setShowSettings(false);
        app.setDisplayStatView(false);
        app.setDisplayFps(true);

        AppSettings settings = new AppSettings(true);
    	settings.setTitle("Cube");
        app.setSettings(settings);
 
        app.start();
    }

    private Box b;
    private Geometry geom;
    public void simpleInitApp() {
 
        initKeys();

        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);

        b = new Box(1.5f, 1.5f, 1.5f);
        geom = new Geometry("Box", b);
 
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        //mat.setColor("Ambient", ColorRGBA.Blue);
        mat.setColor("Diffuse", ColorRGBA.Blue);
        //mat.setColor("Specular", ColorRGBA.Blue);
        mat.setFloat("Shininess", 32f);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-0.7f, -0.5f, -0.5f));
        rootNode.addLight(sun);
    }

    private void initKeys() {
        inputManager.addMapping("rclick", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addListener(actionListener, "rclick");
    }

    private float[] convertv2(Vector2f v) {
        return new float[] {v.getX(), v.getY()};
    }

    private boolean rotating = false;
    private float[] lastmpos = new float[] {-1, -1};
    private float rotmultiplier = 0.02f;
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean pressed, float tpf) {
            if (name.equals("rclick")) {
                rotating = pressed;
                if (!pressed) {
                    lastmpos = new float[] {-1, -1};
                }
            }
        }
    };

    public void simpleUpdate(float tpf) {
        if (rotating) {
            float[] mpos = convertv2(inputManager.getCursorPosition());
            if (lastmpos[0] == -1) {
                lastmpos = mpos;
            }
            float[] diff = new float[] {lastmpos[0] - mpos[0], lastmpos[1] - mpos[1]};
            float[] angles = new float[3];
            geom.getLocalRotation().toAngles(angles);
            geom.setLocalRotation(new Quaternion().fromAngles(angles[0] + (diff[1] * rotmultiplier), angles[1] + (-(diff[0]) * rotmultiplier), angles[2]));
            lastmpos = mpos;
        }
    }
 
}