package ax.examples.box.testbed.tests;

import ax.box.Body2dType;
import ax.box.BoxCollider2d;
import ax.box.RigidBody2d;
import ax.engine.core.Scene;
import ax.engine.core.SceneObject;
import ax.examples.box.testbed.TestbedTest;
import ax.math.vector.Quaternion;
import ax.math.vector.Vector3;

public class DominoTest extends TestbedTest {

    @Override
    public String name() {
        return "Dominos";
    }

    @Override
    public void init(Scene scene) {

        // Floor
        {
            SceneObject so = new SceneObject();
            float width = 100;
            float height = 20;
            boxMesh(so, width, height, STATIC);
            BoxCollider2d collider = new BoxCollider2d(width,height);
            RigidBody2d body = new RigidBody2d();
            so.transform().translation = new Vector3(0, -10, 0);
            add(so, collider, body);
            scene.add(so);
        }

        // Platforms
        {
            for (int i=0; i<4; i++) {
                SceneObject so = new SceneObject();
                float width = 30;
                float height = 0.25f;
                boxMesh(so, width, height, STATIC);
                BoxCollider2d collider = new BoxCollider2d(width, height);
                RigidBody2d body = new RigidBody2d();
                so.transform().translation = new Vector3(0, 5 + (5*i), 0);
                add(so, collider, body);
                scene.add(so);
            }
        }

        // Dominos
        {
            float width = 0.25f;
            float height = 4.0f;
            float density = 25.0f;
            float friction = 0.5f;

            BoxCollider2d collider = new BoxCollider2d(width, height);

            int numPerRow = 25;

            for (int i=0; i<4; i++) {
                for (int j=0; j<numPerRow; j++) {
                    SceneObject so = new SceneObject();
                    boxMesh(so, width, height, DYNAMIC);

                    RigidBody2d body = new RigidBody2d();
                    body.setFriction(friction);
                    body.setBodyType(Body2dType.DYNAMIC);
                    body.setDensity(density);

                    float x = -14.75f + j*(29.5f / (numPerRow - 1));
                    float y = 7.3f + (5f*i);
                    so.transform().translation = new Vector3(x,y,0);

                    if (i==2 && j==0) {
                        so.transform().rotation = Quaternion.eulerRad(0, 0, -0.1f);
                        so.transform().translation.x += 0.1f;
                    } else if (i==3 && j==numPerRow-1) {
                        so.transform().rotation = Quaternion.eulerRad(0, 0, 0.1f);
                        so.transform().translation.x -= 0.1f;
                    } else {
                        so.transform().rotation = Quaternion.eulerRad(0, 0, 0);
                    }

                    add(so, collider, body);
                    scene.add(so);
                }
            }

        }

    }

}
