package info.jbcs.minecraft.chisel.item;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.ChiselBlocks;
import info.jbcs.minecraft.chisel.Configurations;
import info.jbcs.minecraft.chisel.block.BlockSnakestone;
import info.jbcs.minecraft.chisel.carving.CarvableHelper;
import info.jbcs.minecraft.chisel.carving.Carving;
import info.jbcs.minecraft.chisel.carving.CarvingVariation;
import info.jbcs.minecraft.chisel.client.GeneralChiselClient;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;

public class ItemChisel extends ItemTool
{
    Random random = new Random();
    public static Carving carving = Carving.chisel;

    public ItemChisel()
    {
        super(1, ToolMaterial.IRON, CarvableHelper.getChiselBlockSet());

        setMaxStackSize(1);
        setMaxDamage(-1);
        efficiencyOnProperMaterial = 0.5f * super.efficiencyOnProperMaterial;
        setHarvestLevel(Chisel.toolclass, ToolMaterial.IRON.getHarvestLevel());

        setUnlocalizedName("chisel");
        setTextureName("chisel:chisel");

        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, final int x, final int y, final int z, int w, float par8, float par9, float par10)
    {
        if (player.isSneaking())
        {
            player.openGui(Chisel.instance, 0, world, 0, 0, 0);
            return false;
        }
        else
        {

            final Block block = world.getBlock(x, y, z);
            final int blockMeta = world.getBlockMetadata(x, y, z);

            ItemStack chiselTarget = null;

            if (stack.stackTagCompound != null)
            {
                chiselTarget = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("chiselTarget"));
            }
            int meta = blockMeta;
            if (block instanceof BlockSnakestone)
                meta = blockMeta <= 12 ? 1 : 13;
            boolean chiselHasBlockInside = true;

            if (chiselTarget == null)
            {
                final CarvingVariation[] variations = carving.getVariations(block, meta);
                if (variations == null || variations.length < 2)
                    return false;

                int index = random.nextInt(variations.length - 1);
                if (variations[index].block.equals(block) && variations[index].meta == meta)
                {
                    index++;
                    if (index >= variations.length)
                        index = 0;
                }
                final CarvingVariation var = variations[index];
                chiselTarget = new ItemStack(var.block, 1, var.damage);

                chiselHasBlockInside = false;
            }

            final Item targetItem = chiselTarget.getItem();
            final Block targetBlock = Block.getBlockFromItem(targetItem);
            final int targetMeta = chiselTarget.getItemDamage();

            boolean match = carving.isVariationOfSameClass(targetBlock, targetMeta, block, meta);

            /* special case: stone can be carved to cobble and bricks */
            if (Configurations.chiselStoneToCobbleBricks)
            {
                if (!match && block.equals(Blocks.stone) && targetBlock.equals(ChiselBlocks.blockCobblestone))
                    match = true;
                else if (!match && block.equals(Blocks.stone) && targetBlock.equals(ChiselBlocks.stoneBrick))
                    match = true;
            }
            if (!match)
                return false;;

                if (!world.isRemote || chiselHasBlockInside)
                {

                    world.setBlock(x, y, z, targetBlock, targetMeta, 2);
                    world.markBlockForUpdate(x, y, z);
                }

                switch (FMLCommonHandler.instance().getEffectiveSide()) {
                    case SERVER:
                        break;

                    case CLIENT:
                        final String sound = carving.getVariationSound(targetItem, chiselTarget.getItemDamage());
                        GeneralChiselClient.spawnChiselEffect(x, y, z, sound);
                        break;

                    default:
                        break;
                }
                stack.damageItem(1, player);
                if (stack.stackSize == 0)
                {
                    player.inventory.mainInventory[player.inventory.currentItem] =
                            chiselHasBlockInside ?
                                    chiselTarget :
                                        null;
                }
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.isSneaking())
            entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

        return itemstack;
    }
}
