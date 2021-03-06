package info.jbcs.minecraft.chisel.client.render;

import info.jbcs.minecraft.chisel.block.BlockTexturedOre;
import info.jbcs.minecraft.utilities.Drawing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class BlockTexturedOreRenderer implements ISimpleBlockRenderingHandler
{
    static final float bot = -0.001f, top = 1.0f - bot;
    public static int id;
    BlockAdvancedMarbleRenderer rendererAdvanced = new BlockAdvancedMarbleRenderer();

    public BlockTexturedOreRenderer()
    {
        id = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block blck, int meta, int modelID, RenderBlocks renderer)
    {
        if(blck == null || !(blck instanceof BlockTexturedOre))
            return;

        BlockTexturedOre block = (BlockTexturedOre) blck;

        if(block.icon != null)
        {
            renderer.overrideBlockTexture = block.icon;
            renderer.renderBlockAsItem(Blocks.stone, meta, 1.0f);
            renderer.overrideBlockTexture = null;
        } else if(block.base != null)
        {
            renderer.renderBlockAsItem(block.base, meta, 1.0f);
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        renderer.setRenderBounds(bot, bot, bot, top, top, top);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        Drawing.drawBlock(block, meta, renderer);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block blck, int modelId, RenderBlocks renderer)
    {
        if(blck == null || !(blck instanceof BlockTexturedOre))
            return false;
        BlockTexturedOre block = (BlockTexturedOre) blck;
        boolean result = false;

        if(block.currentPass == 0)
        {
            if(block.icon != null)
            {
                renderer.overrideBlockTexture = block.icon;
                result = renderer.renderStandardBlock(block, x, y, z);
                renderer.overrideBlockTexture = null;
            } else if(block.base != null)
            {
                // TODO use renderBlockByRenderType for block.getRenderType != 0
                result = rendererAdvanced.renderWorldBlock(world, x, y, z, block, -1, renderer);
            }
        } else
        {
            renderer.setRenderBounds(bot, bot, bot, top, top, top);
            result = rendererAdvanced.renderWorldBlock(world, x, y, z, block, -1, renderer);
        }

        return result;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return id;
    }
}
