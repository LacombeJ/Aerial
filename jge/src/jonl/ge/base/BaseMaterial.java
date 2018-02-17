package jonl.ge.base;

import java.util.List;

public abstract class BaseMaterial {

	protected abstract String vertexShader(int version);
	
	protected abstract String geometryShader(int version);
	
	protected abstract String fragmentShader(int version);
	
	protected abstract List<Uniform> uniforms();
	
	protected abstract String shaderKey();
	
	protected class Uniform {
        String name;
        Object data;
        public Uniform(String name, Object data) {
            this.name = name;
            this.data = data;
        }
    }
	
}
