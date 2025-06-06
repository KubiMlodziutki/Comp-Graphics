import java.io.*;

public class SceneFileParser {

    private static LightSource readLight(BufferedReader br) throws IOException {
        String[] posInt = br.readLine().split("\\{")[1].replace("}", "").split(",");
        double px = Double.parseDouble(posInt[0]);
        double py = Double.parseDouble(posInt[1]);
        double pz = Double.parseDouble(posInt[2]);
        double Er = Double.parseDouble(posInt[3]);
        double Eg = Double.parseDouble(posInt[4]);
        double Eb = Double.parseDouble(posInt[5]);

        String[] att = br.readLine().split("\\{")[1].replace("}", "").split(",");
        double c0 = Double.parseDouble(att[0]);
        double c1 = Double.parseDouble(att[1]);
        double c2 = Double.parseDouble(att[2]);

        return new LightSource(new Vector3D(px,py,pz), new ColorRGB(Er,Eg,Eb), c0,c1,c2);
    }

    public static Scene parse(String path) throws Exception {

        Scene s = new Scene();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String ln;
            Vector3D c = null;
            double r = 1.0;
            ColorRGB diff = null, spec = null, emis = new ColorRGB(0,0,0);
            double shin = 20;

            while ((ln = br.readLine()) != null) {
                ln = ln.trim();
                if (ln.startsWith("LIGHT_SOURCES")) {
                    int n = Integer.parseInt(ln.split("\\{")[1].replace("}",""));
                    for (int i=0; i < n; i++) s.lightSources.add(readLight(br));
                }
                else if (ln.startsWith("CENTER:")) {
                    String[] xyz = ln.split(":")[1].split(",");
                    c = new Vector3D(Double.parseDouble(xyz[0]), Double.parseDouble(xyz[1]), Double.parseDouble(xyz[2]));
                }
                else if (ln.startsWith("RADIUS:")) {
                    r = Double.parseDouble(ln.split(":")[1]);
                }
                else if (ln.startsWith("DIFFUSE:")) {
                    String[] rgb = ln.split(":")[1].split(",");
                    diff = new ColorRGB(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
                }
                else if (ln.startsWith("SPECULAR:")) {
                    String[] rgb = ln.split(":")[1].split(",");
                    spec = new ColorRGB(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
                }
                else if (ln.startsWith("EMISSIVE:")) {          //  <<<<<  NOWE!
                    String[] rgb = ln.split(":")[1].split(",");
                    emis = new ColorRGB(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
                }
                else if (ln.startsWith("SHININESS:")) {
                    shin = Double.parseDouble(ln.split(":")[1]);
                }
                else if (ln.startsWith("AMBIENT_LIGHT_INTENSITY")) {
                    String[] rgb = ln.split("\\{")[1].replace("}","").split(",");
                    s.ambient = new ColorRGB(Double.parseDouble(rgb[0]), Double.parseDouble(rgb[1]), Double.parseDouble(rgb[2]));
                }
                else if (ln.startsWith("RENDERED_IMAGE_RESOLUTION")) {
                    String[] wh = ln.split("\\{")[1].replace("}","").split(",");
                    s.width = Integer.parseInt(wh[0]);
                    s.height = Integer.parseInt(wh[1]);
                }
                else if (ln.startsWith("RESULT_FILENAME")) {
                    s.outputName = ln.split("\\{")[1].replace("}","").trim();
                }
            }
            s.sphere = new Sphere(c, r, diff, spec, emis, shin);
        }
        return s;
    }
}
