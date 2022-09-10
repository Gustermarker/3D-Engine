import java.util.Vector;

public class Vector3d {

    double x, y, z;

    public Vector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d add(Vector3d other) {
        return new Vector3d(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vector3d subtract(Vector3d other) {
        return new Vector3d(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public double dot(Vector3d other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3d cross(Vector3d other) {
        return new Vector3d(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
        );
    }

    public Vector3d multiply(Vector3d other) {
        return new Vector3d(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public static double length(Vector3d v) {
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static Vector3d normalize(Vector3d v) {
        double len = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
        return new Vector3d(v.getX() / len, v.getY() / len, v.getZ() / len);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    static double[][] lookAt(Vector3d from, Vector3d to) {
        Vector3d temp = new Vector3d(0, 0 , 1);

        Vector3d forward = normalize(from.subtract(to));
        Vector3d right = normalize(temp).cross(forward);
        Vector3d up = forward.cross(right);

        double[][] camToWorld = new double[4][4];

        camToWorld[0][0] = right.getX();
        camToWorld[0][1] = right.getY();
        camToWorld[0][2] = right.getZ();
        camToWorld[1][0] = forward.getX();
        camToWorld[1][1] = forward.getY();
        camToWorld[1][2] = forward.getZ();
        camToWorld[2][0] = up.getX();
        camToWorld[2][1] = up.getY();
        camToWorld[2][2] = up.getZ();

        camToWorld[3][0] = from.getX();
        camToWorld[3][1] = from.getY();
        camToWorld[3][2] = from.getZ();

        return camToWorld;
    }
}
