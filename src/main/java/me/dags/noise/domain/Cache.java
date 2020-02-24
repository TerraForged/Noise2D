/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.dags.noise.domain;

public class Cache implements Domain {

    private final Domain domain;
    private boolean cached = false;
    private float cachedX;
    private float cachedY;
    private float x;
    private float y;

    public Cache(Domain domain) {
        this.domain = domain;
    }

    @Override
    public float getOffsetX(float x, float y) {
        if (cached && x == this.x && y == this.y) {
            return cachedX;
        }
        this.x = x;
        this.y = y;
        this.cachedX = domain.getOffsetX(x, y);
        return cachedX;
    }

    @Override
    public float getOffsetY(float x, float y) {
        if (cached && x == this.x && y == this.y) {
            return cachedY;
        }
        this.x = x;
        this.y = y;
        this.cachedY = domain.getOffsetY(x, y);
        return cachedY;
    }
}
