package ax.std.render.shadow;

import ax.commons.func.Callback;
import ax.commons.func.Callback0D;
import ax.engine.core.*;
import ax.std.render.Light;

import java.util.HashMap;
import java.util.List;

public class ShadowModule extends Attachment {

    Callback0D onAppLoad;
    Callback<Scene> onSceneUpdate;
    Callback<Camera> preRenderCamera;

    Service service;

    List<Light> lights;
    HashMap<ShadowMapKey,ShadowMap> shadowMapHashMap;

    ShadowControl shadowControl = new ShadowControl();

    public ShadowModule() {
        super("shadow-module");

        shadowMapHashMap = new HashMap<>();

        onAppLoad = () -> {
            onAppLoad();
        };

        onSceneUpdate = (scene) -> {
            onSceneUpdate(scene);
        };

        preRenderCamera = (camera) -> {
            preRender(camera);
        };
    }

    @Override
    public void add(Delegate delegate, Service service) {
        this.service = service;
        delegate.onLoad().add(onAppLoad);
        delegate.onSceneUpdate().add(onSceneUpdate);
        delegate.onPreRenderCamera().add(preRenderCamera);
    }

    @Override
    public void remove(Delegate delegate, Service service) {
        delegate.onLoad().remove(onAppLoad);
        delegate.onSceneUpdate().remove(onSceneUpdate);
        delegate.onPreRenderCamera().remove(preRenderCamera);
    }

    private void onAppLoad() {
        shadowControl.create(service);
    }

    private void onSceneUpdate(Scene scene) {
        lights = scene.findComponents(Light.class);
    }

    private void preRender(Camera camera) {
        for (Light light : lights) {
            ShadowMap map = getOrCreateShadowMap(camera, light);
            map.render(service);
        }
    }

    public ShadowMap getOrCreateShadowMap(Camera camera, Light light) {
        ShadowMapKey key = new ShadowMapKey(camera,light);
        ShadowMap map = shadowMapHashMap.get(key);
        if (map == null) {
            map = new ShadowMap(shadowControl, camera, light);
            shadowMapHashMap.put(key,map);
        }
        return map;
    }

    static class ShadowMapKey {
        final Camera camera;
        final Light light;
        ShadowMapKey(Camera camera, Light light) {
            this.camera = camera;
            this.light = light;
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof ShadowMapKey) {
                ShadowMapKey key = (ShadowMapKey) o;
                return key.camera==camera && key.light==light;
            }
            return false;
        }
        @Override
        public int hashCode() {
            return camera.hashCode() + light.hashCode();
        }
    }

}
