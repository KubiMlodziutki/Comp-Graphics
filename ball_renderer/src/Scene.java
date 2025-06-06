import java.util.ArrayList;
import java.util.List;

public class Scene {
    public final List<LightSource> lightSources = new ArrayList<>();
    public Sphere sphere;
    public ColorRGB ambient;
    public int width, height;
    public String outputName = "result.png";
}
