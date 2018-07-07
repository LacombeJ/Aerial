package ax.engine.mod.axis;

import ax.commons.func.Function;
import ax.commons.func.Wrapper;
import ax.engine.core.Camera;
import ax.engine.core.Geometry;
import ax.engine.core.Input;
import ax.engine.core.Mesh;
import ax.engine.core.Property;
import ax.engine.core.SceneObject;
import ax.engine.core.Transform;
import ax.engine.core.Mesh.Mode;
import ax.engine.core.geometry.ConeGeometry;
import ax.engine.core.geometry.GeometryBuilder;
import ax.engine.core.geometry.GeometryOperation;
import ax.engine.core.material.SolidMaterial;
import ax.engine.mod.misc.ScreenSpaceScale;
import ax.engine.mod.ray.RayHit;
import ax.engine.mod.ray.ScreenRayTracer;
import ax.math.vector.Color;
import ax.math.vector.Mathf;
import ax.math.vector.Matrix4;
import ax.math.vector.Vector2;
import ax.math.vector.Vector3;
import ax.math.vector.Vector4;

public class TranslationAxis {
    
    SnapPolicy snapPolicy = SnapPolicy.DISABLED;
    float snapDistance = 1f;
    boolean hover = false;
    
    Camera camera;
    SceneObject axis;
    
    SceneObject xaxis;
    SceneObject yaxis;
    SceneObject zaxis;
    
    SceneObject overlay; //For debugging
    
    public TranslationAxis(Camera camera) {
        
        this.camera = camera;
        
        axis = new SceneObject();
        
        AxisHandler ah = new AxisHandler();
        axis.addComponent(ah);
        
        ScreenSpaceScale sss = new ScreenSpaceScale(camera, 0.03f);
        axis.addComponent(sss);
        
        xaxis = axis("_xaxis",new Vector3(1,0,0));
        yaxis = axis("_yaxis",new Vector3(0,1,0));
        zaxis = axis("_zaxis",new Vector3(0,0,1));
        
        axis.addChild(xaxis);
        axis.addChild(yaxis);
        axis.addChild(zaxis);
        
        overlay = new SceneObject();
        createOverlay();
        
    }
    
    public SceneObject get() {
        return axis;
    }
    
    public SceneObject overlay() {
        return overlay;
    }
    
    public SnapPolicy getPolicy() {
        return snapPolicy;
    }
    
    public void setPolicy(SnapPolicy policy) {
        snapPolicy = policy;
    }
    
    public float getSnapDistance() {
        return snapDistance;
    }
    
    public void setSnapDistance(float distance) {
        snapDistance = distance;
    }
    
    public boolean isHovered() {
        return hover;
    }

    SceneObject axis(String name, Vector3 axis) {
        
        SceneObject so = new SceneObject();
        so.setName(name);
        
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
        SceneObject soLine = new SceneObject();
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
        soLine.addComponent(lineMesh);
        
        // Arrow / Cone
        SceneObject soArrow = new SceneObject();
        ConeGeometry arrow = new ConeGeometry();
        arrow.modify(scaleOp);
        arrow.modify(translateOp);
        if (rotateOp!=null) {
            arrow.modify(rotateOp);
        }
        Mesh arrowMesh = new Mesh(arrow,material);
        arrowMesh.setDepthTest(false);
        soArrow.addComponent(arrowMesh);
        
        so.addChild(soArrow);
        so.addChild(soLine);
        
        return so;
    }
    
    
    class AxisHandler extends Property {
        
        Vector3 highlight = Color.YELLOW.toVector().xyz();
        
        SceneObject dragObject = null;
        Vector3 dragAxis = new Vector3(0,0,0);
        
        Vector3 translationOnClick = null;
        
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
            
            boolean clicked = input().isButtonPressed(Input.MB_LEFT);
            boolean down = input().isButtonDown(Input.MB_LEFT);
            
            Vector2 mouse = window().toUnitSpace(input().getXY());
            
            Transform transform = transform().get();
            
            Vector3 origin = transform.translation.get();
            
            float scale = xaxis.parent().transform().scale.x;
            
            float tip = 0.1f * scale; //To get tip of arrow
            float len = 1 + tip;
            
            Matrix4 VP = camera.computeViewProjectionMatrix();
            
            Wrapper<RayHit> x = new Wrapper<>(null);
            Wrapper<RayHit> y = new Wrapper<>(null);
            Wrapper<RayHit> z = new Wrapper<>(null);
            
            getRayHits(mouse,origin,len,scale,x,y,z);
            
            if (x.x!=null || y.x!=null || z.x!=null) {
                hover = true;
            } else {
                hover = false;
            }
            
            if (!down) {
                dragObject = null;
                start = null;
            }
              
            if (clicked) {
                handleClick(origin, len, scale, VP, x.x, y.x, z.x);
            }
            
            if (dragObject!=null) {
                handleDrag(mouse, origin, len, scale, VP);
            } else {
                handleMove(x.x, y.x, z.x);
            }
            
        }
        
        private void getRayHits(Vector2 mouse, Vector3 origin, float len, float scale, Wrapper<RayHit> x, Wrapper<RayHit> y, Wrapper<RayHit> z) {
            ScreenRayTracer srt = new ScreenRayTracer(camera,mouse.x,mouse.y);
            
            float r = 0.1f; // arrow bounding box "radius"
            
            {
                Vector3 min = new Vector3(0,-r,-r).scale(scale).add(origin);
                Vector3 max = new Vector3(len,r,r).scale(scale).add(origin);
                x.x = srt.boundingBox(min,max);
            }
            {
                Vector3 min = new Vector3(-r,0,-r).scale(scale).add(origin);
                Vector3 max = new Vector3(r,len,r).scale(scale).add(origin);
                y.x = srt.boundingBox(min,max);
            }
            {
                Vector3 min = new Vector3(-r,-r,0).scale(scale).add(origin);
                Vector3 max = new Vector3(r,r,len).scale(scale).add(origin);
                z.x = srt.boundingBox(min,max);
            }
        }
        
        private void handleMove(RayHit x, RayHit y, RayHit z) {
            
            setColor(xaxis,new Vector3(1,0,0));
            setColor(yaxis,new Vector3(0,1,0));
            setColor(zaxis,new Vector3(0,0,1));
            
            String hit = hit(x,y,z);
            if (hit!=null) {
                if (hit.equals("x")) {
                    setColor(xaxis,highlight);
                } else if (hit.equals("y")) {
                    setColor(yaxis,highlight);
                } else if (hit.equals("z")) {
                    setColor(zaxis,highlight);
                }
            }
        }
        
        private void handleClick(Vector3 origin, float len, float scale, Matrix4 VP, RayHit x, RayHit y, RayHit z) {
            
            String hit = hit(x,y,z);
            if (hit!=null) {
                if (hit.equals("x")) {
                    dragObject = xaxis;
                    dragAxis = new Vector3(1,0,0);
                } else if (hit.equals("y")) {
                    dragObject = yaxis;
                    dragAxis = new Vector3(0,1,0);
                } else if (hit.equals("z")) {
                    dragObject = zaxis;
                    dragAxis = new Vector3(0,0,1);
                } 
                
                Vector3 srcVector = new Vector3(0,0,0).add(origin);
                Vector3 dstVector = dragAxis.get().scale(len*scale).add(origin);
                
                src = Matrix4.toScreenSpace(VP,srcVector).xy();
                dst = Matrix4.toScreenSpace(VP,dstVector).xy();
                
                translationOnClick = origin.get();
            }
        }
        
        private void handleDrag(Vector2 mouse, Vector3 origin, float len, float scale, Matrix4 VP) {
            
            Vector2 ray = dst.get().sub(src);
            Vector2 mouseRay = mouse.get().sub(src);
            
            Vector2 proj = mouseRay.proj(ray);
            
            Vector2 v = proj.get().add(src);
            
            if (start==null) {
                start = v.get();
            }
            
            Vector2 startRay = start.get().sub(src);
            
            Vector2 newOrig = v.get().sub(startRay);
            
            //TODO find actual plane from axis orientation
            //TODO handle finding proper x and z plane normals and non-standard orientations
            Vector3 planeNormal = new Vector3();
            float distance = 0;
            if (dragObject==xaxis || dragObject==zaxis) {
                // TODO why does this only work when negative ? (maybe its ray tracer?)
                planeNormal = new Vector3(0,-1,0);
                distance = origin.y;
            } else if (dragObject==yaxis) {
                // TODO Calculate the plane that faces the camera ? Or does this work fine?
                planeNormal = new Vector3(1,0,0);
                // TODO why does this only work when negative ?
                distance = -origin.x;
            }
            
            ScreenRayTracer newOrigRT = new ScreenRayTracer(camera,newOrig.x,newOrig.y);
            RayHit rayHit = newOrigRT.plane(planeNormal,distance);
            
            // Only set axis we are translating
            float s = 0;
            if (rayHit != null) {
                if (dragObject==xaxis) {
                    s = translationOnClick.x;
                } else if (dragObject==yaxis) {
                    s = translationOnClick.y;
                } else if (dragObject==zaxis) {
                    s = translationOnClick.z;
                }
            }
            
            Function<Float,Float> snap = (f) -> f; //default, no snapping
            
            if (snapPolicy==SnapPolicy.GLOBAL) {
                snap = (f) -> Mathf.round(f,0,snapDistance);
            } else if (snapPolicy==SnapPolicy.RELATIVE) {
                final float start = s;
                snap = (f) -> Mathf.round(f,start,snapDistance);
            }
            
            if (rayHit != null) {
                if (dragObject==xaxis) {
                    transform().translation.x = snap.f(rayHit.hit().x);
                } else if (dragObject==yaxis) {
                    transform().translation.y = snap.f(rayHit.hit().y);
                } else if (dragObject==zaxis) {
                    transform().translation.z = snap.f(rayHit.hit().z);
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
        
        private void setColor(SceneObject axis, Vector3 color) {
            SceneObject line = axis.getChildAt(0);
            Mesh mesh = line.getComponent(Mesh.class);
            SolidMaterial material = mesh.getMaterial().asSolidMaterial();
            material.setColor(color);
        }
        
    }
    
    private void createOverlay() {
        
    }
    
    Point createDebugPoint(Vector2 point, boolean visible, Vector3 color) {
        
        SceneObject so = new SceneObject();
        
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
        
        so.addComponent(mesh);
        so.addComponent(pc);
        
        overlay.addChild(so);
        
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
