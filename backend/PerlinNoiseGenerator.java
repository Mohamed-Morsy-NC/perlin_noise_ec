package backend;

import java.awt.Color;
import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;

// Helpful link: https://rtouti.github.io/graphics/perlin-noise-algorithm
// Perlin Noise Library (FastNoiseLite for Java): https://github.com/Auburn/FastNoiseLite
public class PerlinNoiseGenerator {

    private Color[][] pixels = new Color[1920][1080];
    private TerrainType[][] pixelTerrainTypes = new TerrainType[1920][1080];

    // Grass is between 0 and x (starts at 0.25), then sand, then water, then snow
    // increase x to make grass more likely?

    private static float frequency = 0.1f;
    private static BiomeType biome = BiomeType.VARIED;

    private enum TerrainType {
        SAND,
        WATER,
        SNOW,
        GRASS,
        MOUNTAIN,
        LAVA,
        ASH,
        HILL
    }

    private enum BiomeType {
        LAVA,
        PLAINS,
        VARIED
    }

    public void cycleBiomeType() {
        switch(biome) {
            case LAVA -> {
                biome = BiomeType.PLAINS;
            }
            case PLAINS -> {
                biome = BiomeType.VARIED;
            }
            case VARIED -> {
                biome = BiomeType.LAVA;
            }
            default -> {
                biome = BiomeType.VARIED;
            }
        }
    }

    public void start() {
        int seed = (int) (Math.random() * 255);

        FastNoiseLite noise = new FastNoiseLite(seed + 1000);
        noise.SetNoiseType(FastNoiseLite.NoiseType.Perlin);
        noise.SetFrequency(frequency); // low frequency = larger parts, higher = smaller parts (more dotted looking)

        for (int rows = 0; rows < pixels.length; rows++) {
            for (int cols = 0; cols < pixels[0].length; cols++) {

                float noiseValue = (noise.GetNoise(rows, cols) + 1.0f) / 2.0f;

                decideTerrainType(biome, noiseValue, rows, cols);
                Color[] range = decideColorRange(pixelTerrainTypes[rows][cols]);

                pixels[rows][cols] = interpolateColor(range[0], range[1], noiseValue);
            }
        }

    }

    public static void setFrequency(float f) {
        frequency = f;
    }

    private Color interpolateColor(Color min, Color max, float noiseVal) {
        int r = (int) (min.getRed() + noiseVal * (max.getRed() - min.getRed()));
        int g = (int) (min.getGreen() + noiseVal * (max.getGreen() - min.getGreen()));
        int b = (int) (min.getBlue() + noiseVal * (max.getBlue() - min.getBlue()));
        return new Color(r, g, b);
    }

    // AI helped me figure out how to, instead of mapping a certain blocked region
    // to a terrain type, mapping a range to a terrain type
    // would be more effective in generating "biome layers"
    private void decideTerrainType(BiomeType b, float noiseVal, int rows, int cols) {
        if (noiseVal < 0.2 && (b == BiomeType.PLAINS || b == BiomeType.VARIED)) {
            pixelTerrainTypes[rows][cols] = TerrainType.GRASS;
        } else if (noiseVal < 0.4 && b != BiomeType.LAVA && b != BiomeType.PLAINS) {
            pixelTerrainTypes[rows][cols] = TerrainType.SAND;
        } else if (noiseVal < 0.6 && b != BiomeType.PLAINS) {
            if (b == BiomeType.LAVA) {
                pixelTerrainTypes[rows][cols] = TerrainType.LAVA;
            } else {
                pixelTerrainTypes[rows][cols] = TerrainType.WATER;
            }
        } else if (noiseVal < 0.8 && b != BiomeType.PLAINS) {
            pixelTerrainTypes[rows][cols] = TerrainType.MOUNTAIN;
        } else {
            if (b == BiomeType.LAVA) {
                pixelTerrainTypes[rows][cols] = TerrainType.ASH;
            } else if (b == BiomeType.VARIED) {
                pixelTerrainTypes[rows][cols] = TerrainType.SNOW;
            } else {
                pixelTerrainTypes[rows][cols] = TerrainType.HILL;
            }
        }
    }

    // AI helped me refine this method
    // to use a Color array with a light/dark range
    // rather than using an integer clamping range
    // that I found online
    private Color[] decideColorRange(TerrainType t) {
        switch (t) {
            case SAND -> {
                return new Color[]{
                    new Color(236, 204, 162),
                    new Color(255, 235, 190)
                };
            }
            case WATER -> {
                return new Color[]{
                    new Color(29, 88, 121),
                    new Color(57, 162, 216)
                };
            }
            case GRASS -> {
                return new Color[]{
                    new Color(46, 139, 87),
                    new Color(192, 240, 152)
                };
            }
            case SNOW -> {
                return new Color[]{
                    new Color(248, 248, 248),
                    new Color(255, 250, 250)
                };
            }
            case MOUNTAIN -> {
                return new Color[]{
                    new Color(121, 114, 113),
                    new Color(136, 127, 113)
                };
            }
            case LAVA -> {
                return new Color[]{
                    new Color(207, 16, 32),
                    new Color(255, 102, 0)
                };
            }
            case ASH -> {
                return new Color[]{
                    new Color(165, 156, 140),
                    new Color(178, 190, 181)
                };
            }
            case HILL -> {
                return new Color[]{
                    new Color(18, 79, 0),
                    new Color(43, 184, 25)
                };
            }
            default -> {
                return new Color[]{
                    new Color(0, 0, 0),
                    new Color(0, 0, 0)
                };
            }
        }
    }

    public Color[][] getPixelGrid() {
        return pixels;
    }
}
