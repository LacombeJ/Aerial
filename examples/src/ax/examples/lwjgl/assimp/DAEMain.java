package ax.examples.lwjgl.assimp;

import ax.commons.io.Console;
import ax.commons.misc.ArrayUtils;
import ax.engine.core.*;
import ax.engine.core.geometry.BoxGeometry;
import ax.engine.core.geometry.GeometryOperation;
import ax.engine.core.geometry.PlaneGeometry;
import ax.engine.core.material.ShaderLanguage;
import ax.engine.core.material.SolidMaterial;
import ax.engine.core.material.TextureMaterial;
import ax.engine.utils.Loader;
import ax.graphics.GL;
import ax.math.vector.*;
import ax.std.StandardApplication;
import ax.std.Std;
import ax.std.misc.CanvasObject;
import ax.std.misc.FirstPersonControl;
import ax.std.misc.MouseGrabToggle;
import ax.std.render.Light;
import ax.std.render.StandardMaterial;
import ax.std.render.StandardMaterialBuilder;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class DAEMain {

    public static void main(String[]args) {
        new DAEMain().run();
    }

    String modelFileLoc = "C:\\Users\\Jonathan\\Main\\Programming\\BlenderWorkplace\\draft\\myObject.dae";
    String textureFileLoc = "C:\\Users\\Jonathan\\Main\\Programming\\BlenderWorkplace\\draft\\objectColor.png";

    void run() {

        Application app = new StandardApplication();

        Scene scene = new Scene();

        add(scene);

        app.addScene(scene);

        app.start();

    }

    void add(Scene scene) {

        scene.add(player());
        scene.add(cube());
        scene.add(model());
        scene.add(display());
        scene.add(sun());

    }

    SceneObject player() {
        SceneObject so = new SceneObject();

        Camera camera = new Camera();
        camera.setClearColor(Color.SLATE.toVector());

        FirstPersonControl fpc = new FirstPersonControl();
        MouseGrabToggle mgt = new MouseGrabToggle();

        so.addComponent(camera);
        so.addComponent(fpc);
        so.addComponent(mgt);

        so.transform().translation = new Vector3(0,0,10);

        return so;
    }

    SceneObject cube() {
        SceneObject so = new SceneObject();

        Geometry geometry = new BoxGeometry(1,1,1);
        Material material = new SolidMaterial(Color.MANGO.toVector());
        material = standard(Color.GRAY.toVector(), Color.MANGO.toVector(), Color.GRAY.toVector());

        Mesh mesh = new Mesh(geometry, material);

        so.addComponent(mesh);

        so.transform().translation = new Vector3(3,1,3);

        return so;
    }

    SceneObject sun() {

        SceneObject so = new SceneObject();

        Light light = new Light();
        light.setType(Light.DIRECTIONAL);
        light.setDirection(new Vector3(1.1f,-0.9f,0.8f).norm());

        light.setAmbient(new Vector3(0.1f, 0.1f, 0.1f));

        so.addComponent(light);

        return so;

    }

    SceneObject display() {

        Texture texture = Loader.loadTexture(textureFileLoc);

        CanvasObject canvas = new CanvasObject();

        SceneObject so = new SceneObject();
        Geometry box = new PlaneGeometry();
        Material mat = new TextureMaterial(texture);
        Mesh mesh = new Mesh(box,mat);
        so.addComponent(mesh);
        so.transform().scale.set(150,150,1);
        so.transform().translation.set(100,450,0);
        mesh.setDepthTest(false);

        canvas.addChild(so);

        return canvas.get();

    }

    SceneObject model() {
        SceneObject so = new SceneObject();

        Mesh[] meshes = load();

        SceneObject[] children = new SceneObject[meshes.length];

        for (int i=0; i<meshes.length; i++) {
            SceneObject child = new SceneObject();

            child.addComponent(meshes[i]);

            so.addChild(child);

            children[i] = child;
        }

        Console.log(meshes.length);

        return so;
    }

    Mesh[] load() {

        int flags = Assimp.aiProcess_JoinIdenticalVertices |
                Assimp.aiProcess_Triangulate |
                Assimp.aiProcess_FixInfacingNormals;

        AIScene aiScene = Assimp.aiImportFile(modelFileLoc, flags);

        if (aiScene == null) {
            Console.log("Error loading model");
        }

        int numMaterials = aiScene.mNumMaterials();
        PointerBuffer aiMaterials = aiScene.mMaterials();
        List<Material> materials = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
            processMaterial(aiMaterial, materials, textureFileLoc);
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh, materials);
            meshes[i] = mesh;
        }

        return meshes;

    }

    private static Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
        List<Float> vertices = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList();

        processVertices(aiMesh, vertices);
        processNormals(aiMesh, normals);
        processTextCoords(aiMesh, textures);
        processIndices(aiMesh, indices);

        Geometry geometry = new Geometry(
                ArrayUtils.toFloatArray(vertices),
                ArrayUtils.toFloatArray(normals),
                ArrayUtils.toFloatArray(textures),
                ArrayUtils.toIntArray(indices));

        Vector3[] vertexArray = geometry.getVertexArray();
        Vector3[] normalArray = geometry.getNormalArray();
        Std.coordinateTranslate(Std.BLENDER_COORDINATES,Std.STANDARD_COORDINATES,vertexArray);
        Std.coordinateTranslate(Std.BLENDER_COORDINATES,Std.STANDARD_COORDINATES,normalArray);
        geometry.setVectorArray(vertexArray);
        geometry.setNormalArray(normalArray);

        Material material;
        int materialIdx = aiMesh.mMaterialIndex();
        if (materialIdx >= 0 && materialIdx < materials.size()) {
            material = materials.get(materialIdx);
        } else {
            material = new SolidMaterial();
        }

        Mesh mesh = new Mesh(geometry,material);

        return mesh;
    }

    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x());
            vertices.add(aiVertex.y());
            vertices.add(aiVertex.z());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x());
            normals.add(aiNormal.y());
            normals.add(aiNormal.z());
        }
    }

    private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        //TODO figure out why we are indexing at 0 here
        AIVector3D.Buffer aiTextCoords = aiMesh.mTextureCoords(0);
        while (aiTextCoords.remaining() > 0) {
            AIVector3D aiTextCoord = aiTextCoords.get();
            textures.add(aiTextCoord.x());
            textures.add(aiTextCoord.y());
            //textures.add(aiTextCoord.z());
        }
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        while (aiFaces.remaining() > 0) {
            AIFace aiFace = aiFaces.get();
            IntBuffer aiFaceBuffer = aiFace.mIndices();
            indices.add(aiFaceBuffer.get(0));
            indices.add(aiFaceBuffer.get(1));
            indices.add(aiFaceBuffer.get(2));
        }
    }

    private static void processMaterial(AIMaterial aiMaterial, List<Material> materials, String texturesDir) {
        AIColor4D colour = AIColor4D.create();

        AIString path = AIString.calloc();
        Assimp.aiGetMaterialTexture(aiMaterial, Assimp.aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, null, null, null, null, null);
        String textPath = path.dataString();
        Texture texture = null;
        if (textPath != null && textPath.length() > 0) {
            texture = Loader.loadTexture(texturesDir, GL.RGBA16, GL.CLAMP, GL.NEAREST);
        }

        Vector4 ambient = Color.GRAY.toVector();
        int result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            ambient = new Vector4(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4 diffuse = Color.GRAY.toVector();
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            diffuse = new Vector4(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Vector4 specular = Color.GRAY.toVector();
        result = Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, colour);
        if (result == 0) {
            specular = new Vector4(colour.r(), colour.g(), colour.b(), colour.a());
        }

        Material material = new TextureMaterial(texture);
        //material = standard(ambient, diffuse, specular);
        material = standard(texture,specular);

        materials.add(material);
    }

    private static StandardMaterial standard(Vector4 ambient, Vector4 diffuse, Vector4 specular) {

        StandardMaterialBuilder sl = new StandardMaterialBuilder();

        sl.diffuse = sl.vec3(diffuse.xyz());
        sl.specular = sl.vec3(specular.xyz());

        return sl.build();

    }

    private static StandardMaterial standard(Texture texture, Vector4 specular) {

        StandardMaterialBuilder sl = new StandardMaterialBuilder();

        ShaderLanguage.SLTexU texU = sl.texture("texture",texture);

        sl.diffuse = sl.sample(texU);
        sl.specular = sl.vec3(specular.xyz());

        return sl.build();

    }

}
