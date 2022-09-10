import java.util.List;

public class Ray {

    static RayTracer rayTracer = new RayTracer();

    Vector3d origin, direction, intersectionPoint, hitNormal;
    private static final double EPSILON = 0.0000001;

    public Ray() {
    }

    public Ray(Vector3d origin, Vector3d direction) {
        this.origin = origin;
        this.direction = direction;
    }


    public static boolean rayIntersectsTriangle(Ray ray, Triangle inTriangle) {
        Vector3d vertex0 = new Vector3d(inTriangle.A.getX(), inTriangle.A.getY(), inTriangle.A.getZ());
        Vector3d vertex1 = new Vector3d(inTriangle.B.getX(), inTriangle.B.getY(), inTriangle.B.getZ());
        Vector3d vertex2 = new Vector3d(inTriangle.C.getX(), inTriangle.C.getY(), inTriangle.C.getZ());

        Vector3d h;
        Vector3d s;
        Vector3d q;

        double a, f, u, v;
        Vector3d edge1 = vertex1.subtract(vertex0);
        Vector3d edge2 = vertex2.subtract(vertex0);

        h = ray.direction.cross(edge2);
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s = ray.origin.subtract(vertex0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return false;
        }

        q = s.cross(edge1);
        v = f * ray.direction.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) { // ray intersection
            RayTracer.currRay.intersectionPoint = ray.origin.add(ray.direction.multiply(new Vector3d(t, t, t)));
            return true;
        } else { // This means that there is a line intersection but not a ray intersection.
            return false;
        }
    }

    public static boolean rayIntersectsTriangle(Ray ray, List<Triangle> list) {
        double infinity = Double.MAX_VALUE;
        Vector3d outsideIntersectionPoint = new Vector3d(infinity, infinity, infinity);

        for (Triangle t : list) {
            if (rayIntersectsTriangle(ray, t)) {
                if (Vector3d.length(RayTracer.currRay.intersectionPoint.subtract(ray.origin)) < Vector3d.length(outsideIntersectionPoint.subtract(ray.origin))) {
                    outsideIntersectionPoint = RayTracer.currRay.intersectionPoint;
                    RayTracer.currRay.hitNormal = t.getNormal();
                }
            }
        }
        if (outsideIntersectionPoint.x != infinity) {
            RayTracer.currRay.intersectionPoint = outsideIntersectionPoint;
            return true;
        }
        return false;
    }


}
