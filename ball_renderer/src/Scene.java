import java.util.ArrayList;
import java.util.List;

public class Scene {
    public final List<LightSource> lightSourceList = new ArrayList<>();
    public Sphere sceneSphere;
    public ColorRGB ambientLightIntensityColor;
    public int renderedImageWidth;
    public int renderedImageHeight;
}