package jonl.ge.mod.misc;

import jonl.vmath.Mathf;
import jonl.vmath.Vector3;

public class RayTracer {

    private Ray ray;
    
    public RayTracer(Ray ray) {
        this.ray = ray;
    }
    
    public Ray ray() {
        return ray;
    }
    
    public RayHit sphere(Vector3 center, float radius) {
        Vector3 offset = ray.origin().sub(center);
        float a = ray.direction().mag2();
        float b = 2 * ray.direction().dot(offset);
        float c = offset.mag2() - radius*radius;
        float discriminant = b*b - 4*a*c;
        if (discriminant > 0) {
            float t = (-b - Mathf.sqrt(discriminant)) / (2 * a);
            Vector3 hit = ray.origin().add(ray.direction().multiply(t));
            return new RayHit(t, hit, hit.get().sub(center).divide(radius));
        }
        return null;
    }
    
    public RayHit triangle(Vector3 a, Vector3 b, Vector3 c) {
        Vector3 origin = ray.origin();
        Vector3 dir = ray.direction();
        
        Vector3 ab = b.get().sub(a);
        Vector3 ac = c.get().sub(a);
        Vector3 normal = Vector3.cross(ab, ac).norm();
        float t = normal.dot(a.get().sub(origin)) / normal.dot(dir);
        
        if (t > 0) {
            Vector3 hit = origin.add(dir.multiply(t));
            Vector3 toHit = hit.sub(a);
            float dot00 = ac.dot(ac);
            float dot01 = ac.dot(ab);
            float dot02 = ac.dot(toHit);
            float dot11 = ab.dot(ab);
            float dot12 = ab.dot(toHit);
            float divide = dot00 * dot11 - dot01 * dot01;
            float u = (dot11 * dot02 - dot01 * dot12) / divide;
            float v = (dot00 * dot12 - dot01 * dot02) / divide;
            if (u >= 0 && v >=0 && u + v <= 1) {
                return new RayHit(t,hit,normal);
            }
        }
        
        return null;
    }
    
    // http://www.siggraph.org/education/materials/HyperGraph/raytrace/rtinter3.htm
    public RayHit boundingBox(Vector3 min, Vector3 max) {
        Vector3 origin = ray.origin();
        Vector3 dir = ray.direction();
        
        float tNear = Mathf.NEGATIVE_INFINITY;
        float tFar = Mathf.POSITIVE_INFINITY;
        
        for (int i=0; i<3; i++) {
            
            float o = origin.get(i);
            float d = dir.get(i);
            float lo = min.get(i);
            float hi = max.get(i);
            
            if (d==0) {
                if (o < lo || o > hi) {
                    return null;
                } 
            } else {
                float t1 = (lo - o) / d;
                float t2 = (hi - o) / d;
                if (t1 > t2) {
                    float temp = t1;
                    t1 = t2;
                    t2 = temp;
                }
                if (t1 > tNear) {
                    tNear = t1;
                }
                if (t2 < tFar) {
                    tFar = t2;
                }
                if (tNear > tFar) {
                    return null;
                }
                if (tFar < 0) {
                    return null;
                }
            }
            
        }
        
        Vector3 hit = ray.origin().add(ray.direction().multiply(tNear));
        
        // TODO why is normal null, is there a way to calculate normal?
        return new RayHit(tNear,hit,null);
    }
    
    // https://www.siggraph.org//education/materials/HyperGraph/raytrace/rayplane_intersection.htm
    public RayHit plane(Vector3 unitNormal, float dist) {
        
        Vector3 origin = ray.origin();
        Vector3 dir = ray.direction();
        
        float incident = unitNormal.dot(dir);
        
        // Ray is parallel to the plane (or on the plane)
        if (incident==0) {
            return null;
        }
        
        // If incident > 0 , normal of ray is pointing away
        // Do we ignore or add an option for one-sided intersections?
        
        float t = - (unitNormal.dot(origin) + dist) / incident;
        
        // Plane is behind origin, need ray to go backwards for intersection
        if (t < 0) {
            return null;
        }
        
        Vector3 hit = origin.get().add(dir.get().scale(t));
        
        return new RayHit(t,hit,unitNormal.get());
        
    }
    
    
    
}
