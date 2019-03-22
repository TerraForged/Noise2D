package me.dags.noise.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Points {

    private static final float SQRT2 = (float) Math.sqrt(2);
    private static final float PI2 = (float) (Math.PI * 2F);

    public static List<Vec2f> poisson(int seedX, int seedZ, int width, int height, float radius, int samples) {
        return poisson(new Random(NoiseUtil.seed(seedX, seedZ)), width, height, radius, samples);
    }

    public static List<Vec2f> poisson(Random random, int width, int height, float radius, int samples) {
        float cellSize = radius / SQRT2;
        float maxDistance = radius * 2F;
        int w = (int) Math.ceil(width / cellSize);
        int h = (int) Math.ceil(height / cellSize);

        int[][] grid = new int[w][h];
        List<Vec2f> points = new ArrayList<>();
        List<Vec2f> spawns = new ArrayList<>();
        spawns.add(new Vec2f(random.nextInt(width), random.nextInt(height)));

        while (spawns.size() > 0) {
            int index = random.nextInt(spawns.size());
            Vec2f spawn = spawns.get(index);
            boolean valid = false;

            for (int i = 0; i < samples; i++) {
                float angle = random.nextFloat() * PI2;
                float distance = radius + (random.nextFloat() * maxDistance);
                float x = spawn.x + NoiseUtil.sin(angle) * distance;
                float z = spawn.y + NoiseUtil.cos(angle) * distance;
                if (valid(x, z, width, height, cellSize, radius, points, grid)) {
                    valid = true;

                    Vec2f vec = new Vec2f(x, z);
                    points.add(vec);
                    spawns.add(vec);

                    int cellX = (int) (x / cellSize);
                    int cellZ = (int) (z / cellSize);
                    grid[cellZ][cellX] = points.size();
                    break;
                }
            }

            if (!valid) {
                spawns.remove(index);
            }
        }

        return points;
    }

    private static boolean valid(float x, float z, int width, int height, float cellSize, float radius, List<Vec2f> points, int[][] grid) {
        if (x < 0 || x >= width || z < 0 || z >= height) {
            return false;
        }

        int cellX = (int) (x / cellSize);
        int cellZ = (int) (z / cellSize);
        int searchRadius = 2;
        float radius2 = radius * radius;

        int minX = Math.max(0, cellX - searchRadius);
        int maxX = Math.min(grid[0].length - 1, cellX + searchRadius);
        int minZ = Math.max(0, cellZ - searchRadius);
        int maxZ = Math.min(grid.length - 1, cellZ + searchRadius);

        for (int dz = minZ; dz <= maxZ; dz++) {
            for (int dx = minX; dx <= maxX; dx++) {
                int index = grid[dz][dx] - 1;
                if (index == -1) {
                    continue;
                }
                Vec2f point = points.get(index);
                if (point.dist2(x, z) < radius2) {
                    return false;
                }
            }
        }
        return true;
    }
}
