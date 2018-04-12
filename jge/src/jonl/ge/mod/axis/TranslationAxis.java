package jonl.ge.mod.axis;

import jonl.ge.core.Camera;
import jonl.ge.core.GameObject;
import jonl.ge.core.Geometry;
import jonl.ge.core.Input;
import jonl.ge.core.Mesh;
import jonl.ge.core.Property;
import jonl.ge.core.Mesh.Mode;
import jonl.ge.core.geometry.ConeGeometry;
import jonl.ge.core.geometry.GeometryBuilder;
import jonl.ge.core.geometry.GeometryOperation;
import jonl.ge.core.material.SolidMaterial;
import jonl.ge.mod.misc.RayHit;
import jonl.ge.mod.misc.ScreenRayTracer;
import jonl.ge.mod.misc.ScreenSpaceScale;
import jonl.vmath.Color;
import jonl.vmath.Mathf;
import jonl.vmath.Matrix4;
import jonl.vmath.Vector2;
import jonl.vmath.Vector3;
import jonl.vmath.Vector4;

public class TranslationAxis {

    Camera camera;
    GameObject axis;
    
    GameObject xaxis;
    GameObject yaxis;
    GameObject zaxis;
    
    GameObject overlay; //For debugging
    
    public TranslationAxis(Camera camera) {
        
        this.camera = camera;
        
        axis = new GameObject();
        
        ScreenSpaceScale sss = new ScreenSpaceScale(camera, 0.05f);
        axis.addComponent(sss);
        
        AxisHandler ah = new AxisHandler();
        axis.addComponent(ah);
        
        xaxis = axis("_xaxis",new Vector3(1,0,0));
        yaxis = axis("_yaxis",new Vector3(0,1,0));
        zaxis = axis("_zaxis",new Vector3(0,0,1));
        
        axis.addChild(xaxis);
        axis.addChild(yaxis);
        axis.addChild(zaxis);
        
        overlay = new GameObject();
        createOverlay();
        
    }
    
    public GameObject get() {
        return axis;
    }
    
    public GameObject overlay() {
        return overlay;
    }

    GameObject axis(String name, Vector3 axis) {
        
        GameObject go = new GameObject();
        go.setName(name);
        
        SolidMaterial material = new SolidMaterial(new Vector4(axis,1f));
        
        // Some geometry operations
        GeometryOperation scaleOp = GeometryOperation.transform(Matrix4.scaled(0.1f,0.2f,0.1f));
        GeometryOperation translateOp = GeometryOperation.transform(Matrix4.translation(0,1,0));
        GeometryOperation rotateOp = null;
        if (axis.equals(new Vector3(0,0,1))) {
            rotateOp = GeometryOperation.transform(Matrix4.rotation(Mathf.PI_OVER_2,0,0));
        } else if (axis.equals(new Vector3(1,0,0))) {
            //rotateOp = GeometryOperation.transform(Matrix4.rotation(axis.get().scale(Mathf.PI_OVER_2)));
            rotateOp = GeometryOperation.transform(Matrix4.rotation(0,0,-Mathf.PI_OVER_2));
        }
        
        // Line
        GameObject goLine = new GameObject();
        GeometryBuilder gb = new GeometryBuilder();
        gb.addVertex(new Vector3(0,0,0));
        gb.addVertex(new Vector3(0,1,0));
        Geometry lineGeometry = gb.build();
        if (rotateOp!=null) {
            lineGeometry.modify(rotateOp);
        }
        Mesh lineMesh = new Mesh(lineGeometry,material);
        lineMesh.setMode(Mode.LINES);
        lineMesh.setThickness(3f);
        lineMesh.setDepthTest(false);
        goLine.addComponent(lineMesh);
        
        // Arrow / Cone
        GameObject goArrow = new GameObject();
        ConeGeometry arrow = new ConeGeometry();
        arrow.modify(scaleOp);
        arrow.modify(translateOp);
        if (rotateOp!=null) {
            arrow.modify(rotateOp);
        }
        Mesh arrowMesh = new Mesh(arrow,material);
        arrowMesh.setDepthTest(false);
        goArrow.addComponent(arrowMesh);
        
        go.addChild(goArrow);
        go.addChild(goLine);
        
        return go;
    }
    
    
    class AxisHandler extends Property {
        
        Vector3 highlight = Color.YELLOW.toVector().xyz();
        
        GameObject moving = null;
        Vector3 movingAxis = new Vector3(0,0,0);
        
        Vector2 start = new Vector2();
        Vector2 src = new Vector2();
        Vector2 dst = new Vector2();
        
        AxisHandler() {
            
        }
        
        @Override
        public void create() {
            
        }

        @Override
        public void update() {
            
            if (!input().isButtonDown(Input.MB_LEFT)) {
                moving = null;
            }
            
            boolean clicked = input().isButtonPressed(Input.MB_LEFT);
            Vector2 mouse = window().toUnitSpace(input().getXY());
            ScreenRayTracer srt = new ScreenRayTracer(camera,mouse.x,mouse.y);
            
            float scale = xaxis.parent().transform().scale.x;
            
            //TODO is it okay to use local transform?
            Vector3 translate = transform().translation.get();
            
            float r = 0.1f;
            float d = 0.1f * scale; //To get tip of arrow
            float len = 1 + d;
            
            RayHit x = null;
            RayHit y = null;
            RayHit z = null;
            
            {
                Vector3 min = new Vector3(0,-r,-r).scale(scale).add(translate);
                Vector3 max = new Vector3(len,r,r).scale(scale).add(translate);
                x = srt.boundingBox(min,max);
            }
            {
                Vector3 min = new Vector3(-r,0,-r).scale(scale).add(translate);
                Vector3 max = new Vector3(r,len,r).scale(scale).add(translate);
                y = srt.boundingBox(min,max);
            }
            {
                Vector3 min = new Vector3(-r,-r,0).scale(scale).add(translate);
                Vector3 max = new Vector3(r,r,len).scale(scale).add(translate);
                z = srt.boundingBox(min,max);
            }
            
            setColor(xaxis,new Vector3(1,0,0));
            setColor(yaxis,new Vector3(0,1,0));
            setColor(zaxis,new Vector3(0,0,1));
            
            String hit = hit(x,y,z);
            if (hit!=null) {
                if (hit.equals("x")) {
                    setColor(xaxis,highlight);
                    if (clicked) {
                        moving = xaxis;
                        movingAxis = new Vector3(1,0,0);
                    }
                } else if (hit.equals("y")) {
                    setColor(yaxis,highlight);
                    if (clicked) {
                        moving = yaxis;
                        movingAxis = new Vector3(0,1,0);
                    }
                } else if (hit.equals("z")) {
                    setColor(zaxis,highlight);
                    if (clicked) {
                        moving = zaxis;
                        movingAxis = new Vector3(0,0,1);
                    }
                } 
            }
            
            Matrix4 VP = camera.computeViewProjectionMatrix();
            
            if (clicked) {
                
                Vector3 srcVector = new Vector3(0,0,0).add(translate);
                Vector3 dstVector = movingAxis.get().scale(len*scale).add(translate);
                
                src = Matrix4.toScreenSpace(VP,srcVector).xy();
                dst = Matrix4.toScreenSpace(VP,dstVector).xy();
            }
            
            if (moving!=null) {
                
                Vector2 ray = dst.get().sub(src);
                Vector2 mouseRay = mouse.get().sub(src);
                
                Vector2 proj = mouseRay.proj(ray);
                
                Vector2 v = proj.get().add(src);
                
                if (clicked) {
                    start = v.get();
                }
                
                Vector2 startRay = start.get().sub(src);
                
                Vector2 newOrig = v.get().sub(startRay);
                
                //TODO find actual plane from axis orientation
                //TODO handle finding proper x and z plane normals and non-standard orientations
                Vector3 planeNormal = new Vector3();
                float distance = 0;
                if (moving==xaxis || moving==zaxis) {
                    // TODO why does this only work when negative ? (maybe its ray tracer?)
                    planeNormal = new Vector3(0,-1,0);
                    distance = translate.y;
                } else if (moving==yaxis) {
                    // TODO Calculate the plane that faces the camera ? Or does this work fine?
                    planeNormal = new Vector3(1,0,0);
                    // TODO why does this only work when negative ?
                    distance = -translate.x;
                }
                
                ScreenRayTracer newOrigRT = new ScreenRayTracer(camera,newOrig.x,newOrig.y);
                RayHit rayHit = newOrigRT.plane(planeNormal,distance);
                
                // Only set axis we are translating
                if (rayHit != null) {
                    if (moving==xaxis) {
                        transform().translation.x = rayHit.hit().x;
                    } else if (moving==yaxis) {
                        transform().translation.y = rayHit.hit().y;
                    } else if (moving==zaxis) {
                        transform().translation.z = rayHit.hit().z;
                    }
                }
            }
        }
        
        private String hit(RayHit x, RayHit y, RayHit z) {
            float shortest = Float.MAX_VALUE;
            String hit = "";
            if (x!=null) {
                if (x.distance()<shortest) {
                    hit = "x";
                    shortest = x.distance();
                }
            }
            if (y!=null) {
                if (y.distance()<shortest) {
                    hit = "y";
                    shortest = y.distance();
                }
            }
            if (z!=null) {
                if (z.distance()<shortest) {
                    hit = "z";
                    shortest = z.distance();
                }
            }
            if (shortest!=Float.MAX_VALUE) {
                return hit;
            }
            return null;
        }
        
        private void setColor(GameObject axis, Vector3 color) {
            GameObject line = axis.getChildAt(0);
            Mesh mesh = line.getComponent(Mesh.class);
            SolidMaterial material = mesh.getMaterial().asSolidMaterial();
            material.setColor(color);
        }
        
    }
    
    private void createOverlay() {
        
    }
    
    Point createDebugPoint(Vector2 point, boolean visible, Vector3 color) {
        
        GameObject go = new GameObject();
        
        GeometryBuilder gb = new GeometryBuilder();
        gb.addVertex(new Vector3(point.x,point.y,0));
        
        Geometry geometry = gb.build();
        SolidMaterial material = new SolidMaterial(color);
        Mesh mesh = new Mesh(geometry, material);
        mesh.setMode(Mode.POINTS);
        mesh.setDepthTest(false);
        mesh.setThickness(10f);
        mesh.setVisible(visible);
        
        Point pc = new Point();
        
        go.addComponent(mesh);
        go.addComponent(pc);
        
        overlay.addChild(go);
        
        return pc;
    }
    
    class Point extends Property {
        
        Mesh mesh;
        
        @Override
        public void create() {
            mesh = getComponent(Mesh.class);
        }

        @Override
        public void update() {
            
        }
        
        public void setPoint(Vector2 v) {
            Vector3 point = new Vector3(v,0);
            mesh.getGeometry().setVectorArray(new Vector3[] {point});
        }
        
        public void setVisible(boolean enable) {
            mesh.setVisible(enable);
        }
        
    }
    
}
