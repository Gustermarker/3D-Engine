import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class RayTracer {

    static List<Triangle> triangleList = new ArrayList<Triangle>();
    static BufferedImage image = new BufferedImage(600, 600, BufferedImage.TYPE_BYTE_GRAY);
    public static Ray currRay = new Ray();

    public static void main(String[] args) {
        Vertex cameraOrigin = new Vertex(0, 25, -150);
        parseSTL();
        render();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        JPanel pane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };

        frame.setTitle("Ray Tracing 3D Engine");
        frame.add(pane);
        frame.setVisible(true);
    }

    /*
     * Creates a list of Triangles from an STL. Each Triangle is defined by three vertices and a normal vector
     */
    public static void parseSTL() {
        try {
            Scanner scanner = new Scanner(new File("/Users/gustavoestermarker/IdeaProjects/3D Engine/mustang.stl"));
            while (scanner.hasNextLine()) {
                Vertex[] points = new Vertex[3];
                String line = scanner.nextLine();
                Vector3d temp;
                if (line.contains("facet normal")) { // facet normal x y z
                    String[] xyz = line.split("\\s+"); // xyz of one point
                    temp = new Vector3d(Double.parseDouble(xyz[3]), Double.parseDouble(xyz[4]), Double.parseDouble(xyz[5]));
                    scanner.nextLine();

                    for (int i = 0; i < 3; i++) {
                        String vertex = scanner.nextLine(); // vertex x y z
                        xyz = vertex.split("\\s+"); // xyz of one point
                        points[i] = new Vertex(Double.parseDouble(xyz[2]), Double.parseDouble(xyz[3]), Double.parseDouble(xyz[4]));
                    }
                    triangleList.add(new Triangle(points[0], points[1], points[2], temp));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * shoots a ray through each pixel of the frame and shades pixel if it hits something
     */
    public static void render() {
        double frameWidth = 600, frameHeight = 600;
        double invWidth = 1 / frameWidth, invHeight = 1 / frameHeight;
        double fov = 90;
        double aspectRatio = frameWidth / frameHeight;
        double angle = Math.tan(Math.PI * 0.5 * fov / 180.0);

        double [][] camMatrix = Vector3d.lookAt(new Vector3d(75, 75, -150), new Vector3d(0, 0, 0));
        System.out.println(camMatrix[0][0]);

        for (int x = 0; x < frameWidth; x++) {
            for (int y = 0; y < frameHeight; y++) {
                Vector3d camForward = new Vector3d(camMatrix[1][0], camMatrix[1][1], camMatrix[1][2]);
                Vector3d u = new Vector3d(camMatrix[0][0], camMatrix[0][1], camMatrix[0][2]);
                Vector3d v = new Vector3d(camMatrix[2][0], camMatrix[2][1], camMatrix[2][2]);
                Vector3d w = camForward.multiply(new Vector3d(-1, -1, -1));

                double xx = (2 * ((x + 0.5) * invWidth) - 1) / 2 * angle * aspectRatio;
                double yy = (1 - 2 * ((y + 0.5) * invHeight)) / 2 * angle;
                Vector3d xp = new Vector3d(xx, xx, xx);
                Vector3d yp = new Vector3d(yy, yy, yy);

                Vector3d dir = w.add(xp.multiply(u)).add(yp.multiply(v));
                dir = Vector3d.normalize(dir);
                double rgb = shade(x, y, dir, camMatrix);

                rgb = Math.abs(Math.round(rgb));
                Color color = new Color((int) rgb, (int) rgb, (int) rgb);
                image.setRGB(x, y, color.getRGB());
            }
        }
    }

    /*
     * calculates angle between camera ray and triangle normal. the angle determines
     * the color of the pixel from 000 to 255. angle is essentially just a scalar
     */
    public static double shade(int px, int py, Vector3d dir, double[][] camMatrix) {
        Ray cameraRay = new Ray();
        cameraRay.origin = new Vector3d(camMatrix[3][0], camMatrix[3][1], camMatrix[3][2]);
        cameraRay.direction = dir;
        double shade = 255; // no hit = white background

        if (Ray.rayIntersectsTriangle(cameraRay, triangleList)) {
            shade = currRay.hitNormal.dot(dir.multiply(new Vector3d(-1, -1, -1))) * 255.0;
        }

        return shade;
    }

}
