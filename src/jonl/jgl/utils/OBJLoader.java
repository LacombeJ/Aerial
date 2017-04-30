package jonl.jgl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import jonl.jutils.io.FileUtils;
import jonl.jutils.misc.TypeUtils;

/**
 * Utility for loading wavefront .obj files
 * 
 * @author Jonathan Lacombe
 *
 */
public class OBJLoader {

    /**
     * Loads a wavefront .obj file and returns the MeshData
     * <p>
     * This currently only works for files that have both
     * vertex normal and texture coordinate data.
     * </p>
     * @param file
     * @return
     */
    public static MeshData load(String file) {
        
        List<Float> vertices        = new ArrayList<>();
        List<Float> normals         = new ArrayList<>();
        List<Float> texCoords       = new ArrayList<>();
        List<VertexData> vertexData = new LinkedList<>();
        List<VertexData> uniqueData = new ArrayList<>();
        HashSet<VertexData> vertexSet = new HashSet<>();
        
        String[] lines = FileUtils.linesFromFile(file);
        
        for (String line : lines) {
            if (line.startsWith("#")) {
                continue; //do nothing (comment)
            } else if (line.startsWith("mtllib ")) {
                //TODO
            } else if (line.startsWith("o ")) {
                //TODO
            } else if (line.startsWith("v ")) {
                addToList(vertices,line);
            } else if (line.startsWith("vt ")) {
                addToList(texCoords,line);
            } else if (line.startsWith("vn ")) {
                addToList(normals,line);
            } else if (line.startsWith("usemtl ")) {
                //TODO
            } else if (line.startsWith("s ")) {
                //TODO
            } else if (line.startsWith("f ")) {
                
                String[] tokens = line.split(" ");
                
                for (int i=1; i<=3; i++) {
                    String[] token = tokens[i].split("/");
                    int v   = Integer.parseInt(token[0]);
                    int vt  = Integer.parseInt(token[1]);
                    int vn  = Integer.parseInt(token[2]);
                    
                    //.obj files start at index 1
                    VertexData data = new VertexData(v-1,vt-1,vn-1);
                    if (!vertexSet.contains(data)) {
                        uniqueData.add(data);
                        vertexSet.add(data);
                    }
                    vertexData.add(data);
                }
                
            } else {
                System.err.println("Error reading line: "+line);
            }
        }
        
        List<Float> v = new ArrayList<>();
        List<Float> n = new ArrayList<>();
        List<Float> t = new ArrayList<>();
        List<Integer> i = new ArrayList<>();
        
        for (VertexData d : uniqueData) {
            v.add(vertices.get(d.v*3));
            v.add(vertices.get(d.v*3+1));
            v.add(vertices.get(d.v*3+2));
            
            n.add(normals.get(d.vn*3));
            n.add(normals.get(d.vn*3+1));
            n.add(normals.get(d.vn*3+2));
            
            t.add(texCoords.get(d.vt*2));
            t.add(texCoords.get(d.vt*2+1));
        }
        
        for (VertexData d : vertexData) {
            i.add(uniqueData.indexOf(d));
        }
        
        return new MeshData(TypeUtils.toFloatArray(v),TypeUtils.toFloatArray(n),
                TypeUtils.toFloatArray(t),TypeUtils.toIntArray(i));
    }
    
    public static void addToList(List<Float> list, String line) {
        String[] tokens = line.split(" ");
        for (int i=1; i<tokens.length; i++) {
            list.add(Float.parseFloat(tokens[i]));
        }
    }
    
    /**
     * Class representing a vertex, it's texture coordinates and normals
     * with values specified by the indices
     * @author Jonathan Lacombe
     */
    private static class VertexData {
        int v;
        int vt;
        int vn;
        VertexData(int v, int vt, int vn) {
            this.v = v;
            this.vt = vt;
            this.vn = vn;
        }
        @Override
        public int hashCode() {
            return v*vt + 2*v*vn + 3*vt*vn;
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof VertexData) {   
                VertexData f = (VertexData)o;
                return (f.v==v && f.vt==vt && f.vn==vn);
            }
            return false;
        }
    }
    
    
    
    
    
}
