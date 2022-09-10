public class Triangle {

    Vertex A, B, C;
    Vector3d normal;

    public Triangle(Vertex A, Vertex B, Vertex C, Vector3d normal) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.normal = normal;
    }

    public Vector3d getNormal() {
        return normal;
    }

}
