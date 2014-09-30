package info.jbcs.minecraft.chisel.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class RenderBlocksCTM extends RenderBlocks
{

    RenderBlocksCTM()
    {
        super();

        resetVertices();
    }

    Tessellator tessellator;
    private final static double[] X = new double[26];
    private final static double[] Y = new double[26];
    private final static double[] Z = new double[26];
    private final static double[] U = new double[26];
    private final static double[] V = new double[26];
    private final static int[] L = new int[26];
    private final static float[] R = new float[26];
    private final static float[] G = new float[26];
    private final static float[] B = new float[26];
    TextureSubmap submap;
    TextureSubmap submapSmall;
    RenderBlocks rendererOld;

    int bx, by, bz;

    @Override
    public boolean renderStandardBlock(Block block, int x, int y, int z)
    {
        bx = x;
        by = y;
        bz = z;

        tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        tessellator.addTranslation(x, y, z);
        resetVertices();

        boolean res = super.renderStandardBlock(block, x, y, z);

        tessellator.addTranslation(-x, -y, -z);

        return res;
    }

    void setupSides(int a, int b, int c, int d, int xa, int xb, int xc, int xd, int e)
    {
        L[a] = brightnessBottomLeft;
        L[b] = brightnessBottomRight;
        L[c] = brightnessTopRight;
        L[d] = brightnessTopLeft;
        L[e] = (brightnessBottomLeft + brightnessTopLeft + brightnessTopRight + brightnessBottomRight) / 4;
        L[xa] = (L[a] + L[b]) / 2;
        L[xb] = (L[b] + L[c]) / 2;
        L[xc] = (L[c] + L[d]) / 2;
        L[xd] = (L[d] + L[a]) / 2;

        R[a] = colorRedBottomLeft;
        R[b] = colorRedBottomRight;
        R[c] = colorRedTopRight;
        R[d] = colorRedTopLeft;
        R[e] = (colorRedBottomLeft + colorRedTopLeft + colorRedTopRight + colorRedBottomRight) / 4;
        R[xa] = (R[a] + R[b]) / 2;
        R[xb] = (R[b] + R[c]) / 2;
        R[xc] = (R[c] + R[d]) / 2;
        R[xd] = (R[d] + R[a]) / 2;

        G[a] = colorGreenBottomLeft;
        G[b] = colorGreenBottomRight;
        G[c] = colorGreenTopRight;
        G[d] = colorGreenTopLeft;
        G[e] = (colorGreenBottomLeft + colorGreenTopLeft + colorGreenTopRight + colorGreenBottomRight) / 4;
        G[xa] = (G[a] + G[b]) / 2;
        G[xb] = (G[b] + G[c]) / 2;
        G[xc] = (G[c] + G[d]) / 2;
        G[xd] = (G[d] + G[a]) / 2;

        B[a] = colorBlueBottomLeft;
        B[b] = colorBlueBottomRight;
        B[c] = colorBlueTopRight;
        B[d] = colorBlueTopLeft;
        B[e] = (colorBlueBottomLeft + colorBlueTopLeft + colorBlueTopRight + colorBlueBottomRight) / 4;
        B[xa] = (B[a] + B[b]) / 2;
        B[xb] = (B[b] + B[c]) / 2;
        B[xc] = (B[c] + B[d]) / 2;
        B[xd] = (B[d] + B[a]) / 2;
    }

    void side(int a, int b, int c, int d, int iconIndex, boolean flip)
    {
        IIcon icon = iconIndex >= 16 ? submapSmall.icons[iconIndex - 16] : submap.icons[iconIndex];

        double u0 = icon.getMaxU();
        double u1 = icon.getMinU();
        double v0 = icon.getMaxV();
        double v1 = icon.getMinV();

        U[a] = flip ? u1 : u1;
        U[b] = flip ? u0 : u1;
        U[c] = flip ? u0 : u0;
        U[d] = flip ? u1 : u0;

        V[a] = flip ? v1 : v1;
        V[b] = flip ? v1 : v0;
        V[c] = flip ? v0 : v0;
        V[d] = flip ? v0 : v1;

        vert(a);
        vert(b);
        vert(c);
        vert(d);
    }

    void vert(int index)
    {
        if(enableAO)
        {
            tessellator.setColorOpaque_F(R[index], G[index], B[index]);
            tessellator.setBrightness(L[index]);
        }

        tessellator.addVertexWithUV(X[index], Y[index], Z[index], U[index], V[index]);
    }

    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
            tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 4);

            setupSides(1, 0, 4, 5, 14, 19, 17, 23, 9);
            side(1, 14, 9, 23, tex[0], false);
            side(23, 9, 17, 5, tex[1], false);
            side(9, 19, 4, 17, tex[3], false);
            side(14, 0, 19, 9, tex[2], false);
        }
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
            tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMinU(), i.getMinV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 5);

            setupSides(3, 2, 6, 7, 15, 25, 16, 21, 11);
            side(11, 21, 3, 15, tex[3], false);
            side(16, 7, 21, 11, tex[2], false);
            side(25, 11, 15, 2, tex[1], false);
            side(6, 16, 11, 25, tex[0], false);
        }
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
            tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMaxV());
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 2);

            setupSides(2, 3, 0, 1, 15, 18, 14, 22, 8);
            side(2, 15, 8, 22, tex[0], false);
            side(15, 3, 18, 8, tex[2], false);
            side(8, 18, 0, 14, tex[3], false);
            side(22, 8, 14, 1, tex[1], false);
        }
    }


    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMinV());
            tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMinV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 3);

            setupSides(4, 7, 6, 5, 20, 16, 24, 17, 10);
            side(17, 4, 20, 10, tex[2], false);
            side(5, 17, 10, 24, tex[0], false);
            side(24, 10, 16, 6, tex[1], false);
            side(10, 20, 7, 16, tex[3], false);
        }
    }

    @Override
    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(0.0, 0.0, 1.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, i.getMinU(), i.getMinV());
            tessellator.addVertexWithUV(1.0, 0.0, 0.0, i.getMaxU(), i.getMinV());
            tessellator.addVertexWithUV(1.0, 0.0, 1.0, i.getMaxU(), i.getMaxV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 0);

            setupSides(0, 3, 7, 4, 18, 21, 20, 19, 13);
            side(13, 21, 7, 20, tex[3], true);
            side(19, 13, 20, 4, tex[2], true);
            side(0, 18, 13, 19, tex[0], true);
            side(18, 3, 21, 13, tex[1], true);
        }
    }

    @Override
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon)
    {
        if(rendererOld != null && rendererOld.hasOverrideBlockTexture())
        {
            IIcon i = rendererOld.overrideBlockTexture;

            tessellator.addVertexWithUV(0.0, 1.0, 0.0, i.getMinU(), i.getMinV());
            tessellator.addVertexWithUV(0.0, 1.0, 1.0, i.getMinU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 1.0, 1.0, i.getMaxU(), i.getMaxV());
            tessellator.addVertexWithUV(1.0, 1.0, 0.0, i.getMaxU(), i.getMinV());
        } else
        {
            int tex[] = CTM.getSubmapIndices(blockAccess, bx, by, bz, 1);

            setupSides(2, 1, 5, 6, 22, 23, 24, 25, 12);
            side(12, 24, 6, 25, tex[3], false);
            side(22, 12, 25, 2, tex[1], false);
            side(1, 23, 12, 22, tex[0], false);
            side(23, 5, 24, 12, tex[2], false);
        }
    }

    void resetVertices()
    {

        X[0] = this.renderMinX;
        Z[0] = this.renderMinZ;
        Y[0] = this.renderMinY;

        X[1] = this.renderMinX;
        Z[1] = this.renderMinZ;
        Y[1] = this.renderMaxY;

        X[2] = this.renderMaxX;
        Z[2] = this.renderMinZ;
        Y[2] = this.renderMaxY;

        X[3] = this.renderMaxX;
        Z[3] = this.renderMinZ;
        Y[3] = this.renderMinY;

        X[4] = this.renderMinX;
        Z[4] = this.renderMaxZ;
        Y[4] = this.renderMinY;

        X[5] = this.renderMinX;
        Z[5] = this.renderMaxZ;
        Y[5] = this.renderMaxY;

        X[6] = this.renderMaxX;
        Z[6] = this.renderMaxZ;
        Y[6] = this.renderMaxY;

        X[7] = this.renderMaxX;
        Z[7] = this.renderMaxZ;
        Y[7] = this.renderMinY;

        X[8] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[8] = this.renderMinZ;
        Y[8] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[9] = this.renderMinX;
        Z[9] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[9] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[10] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[10] = this.renderMaxZ;
        Y[10] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[11] = this.renderMaxX;
        Z[11] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[11] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[12] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[12] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[12] = this.renderMaxY;

        X[13] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[13] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[13] = this.renderMinY;

        X[14] = this.renderMinX;
        Z[14] = this.renderMinZ;
        Y[14] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[15] = this.renderMaxX;
        Z[15] = this.renderMinZ;
        Y[15] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[16] = this.renderMaxX;
        Z[16] = this.renderMaxZ;
        Y[16] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[17] = this.renderMinX;
        Z[17] = this.renderMaxZ;
        Y[17] = (this.renderMaxY - this.renderMinY) / 2.0f;

        X[18] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[18] = this.renderMinZ;
        Y[18] = this.renderMinY;

        X[19] = this.renderMinX;
        Z[19] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[19] = this.renderMinY;

        X[20] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[20] = this.renderMaxZ;
        Y[20] = this.renderMinY;

        X[21] = this.renderMaxX;
        Z[21] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[21] = this.renderMinY;

        X[22] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[22] = this.renderMinZ;
        Y[22] = this.renderMaxY;

        X[23] = this.renderMinX;
        Z[23] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[23] = this.renderMaxY;

        X[24] = (this.renderMaxX - this.renderMinX) / 2.0f;
        Z[24] = this.renderMaxZ;
        Y[24] = this.renderMaxY;

        X[25] = this.renderMaxX;
        Z[25] = (this.renderMaxZ - this.renderMinZ) / 2.0f;
        Y[25] = this.renderMaxY;
    }
}
