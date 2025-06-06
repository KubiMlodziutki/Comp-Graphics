import java.awt.image.BufferedImage;

public class Renderer {
    private static final double k_a = 1.0;
    private static final double k_d = 1.0;
    private static final double k_s = 1.0;

    public static BufferedImage render(Scene scene) {

        int W = scene.width;
        int H = scene.height;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        double aspect = (double) W / H;

        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++) {
                double sx = (2 * (x + 0.5) / W - 1) * aspect;
                double sy = 1 - 2 * (y + 0.5) / H;

                Vector3D P0 = new Vector3D(sx, sy, 5);
                Vector3D d = new Vector3D(0, 0, -1);
                double[] tHit = new double[1];

                if (!scene.sphere.hitParallel(P0, d, tHit)) {
                    img.setRGB(x, y, 0xFF000000);
                    continue;
                }

                Vector3D Phit = P0.add(d.scale(tHit[0]));
                Vector3D N = scene.sphere.normal(Phit);
                Vector3D V = d.scale(-1).normalize();

                ColorRGB L = scene.sphere.emissive;
                ColorRGB ambient = scene.ambient.multiplyByScalar(k_a);
                L = L.add(ambient);

                for (LightSource Ls : scene.lightSources) {

                    Vector3D Ldir = Ls.position.subtract(Phit);
                    double r = Ldir.length();
                    Ldir = Ldir.normalize();

                    double fatt = Ls.attenuation(r);
                    if (fatt == 0) continue;

                    double NdotL = Math.max(N.dot(Ldir), 0);
                    ColorRGB diff = Ls.intensity.multiplyByColor(scene.sphere.diff).multiplyByScalar(k_d * fatt * NdotL);

                    Vector3D Rdir = N.scale(2 * NdotL).subtract(Ldir).normalize();
                    double VdotR = Math.max(V.dot(Rdir), 0);
                    double specPow = Math.pow(VdotR, scene.sphere.shininess);
                    ColorRGB spec = Ls.intensity.multiplyByColor(scene.sphere.spec).multiplyByScalar(k_s * fatt * specPow);

                    L = L.add(diff).add(spec);
                }

                img.setRGB(x, y, L.toARGB());
            }
        return img;
    }
}
