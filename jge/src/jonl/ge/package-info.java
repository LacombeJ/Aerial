
/*

- Application
  - Every variable that is modified by the AppRenderer
    should not be visible outside this package and should not effect the application logic
    - An exception to this is an Updater
    - This is to ensure that all applications with the same updater run the same

//TODO @@@@@@@@@@@@@@@@@@@@@
- GPU gems : map, reduce, sort, search using shaders
  - floating point textures and pixel buffers
- Reduction (GPU gems)
  - Send multiple information to shader to calculate a single value
  - more efficient than reading every pixel to find a certain value
    - ex: max(rgb)
  - use gpu to compute
  - Try reduction for SS culling

//TODO
- Add audio
- Lighting
- Shadows


- Info and References
  - http://ruh.li/GraphicsCookTorrance.html
  - GPU gems reduction

 */

/**
 * Game Engine
 * <p>
 * @author Jonathan Lacombe
 *
 */
package jonl.ge;