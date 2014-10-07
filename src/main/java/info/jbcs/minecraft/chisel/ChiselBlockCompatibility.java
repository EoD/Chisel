package info.jbcs.minecraft.chisel;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import info.jbcs.minecraft.chisel.carving.Carving;

public class ChiselBlockCompatibility
{
    public static class ModBlock
    {
        String modName;
        Block blockObject;
        String carvableName;
        int metaData;

        public ModBlock(Block block, String carvablename, int metadata)
        {
            blockObject = block;
            carvableName = carvablename;
            metaData = metadata;
        }
    }

    static List<ModBlock> modBlocks = new ArrayList<ModBlock>();

    public static void loadModBlocks()
    {
        addBlockCandidate("ProjRed|Exploration", "projectred.exploration.stone", 0, "marble");
        addBlockCandidate("bluepower", "marble", 0, "marble");
        addBlockCandidate("UndergroundBiomes", "metamorphicStone", 2, "marble");
        addBlockCandidate("Artifice", "tile.artifice.marble", 0, "marble");
        // addBlockCandidate("Geostrata", "geostrata_rock_marble_smooth", 0, "marble"); Does not work currently.
        addBlockCandidate("UndergroundBiomes", "sedimentaryStone", 0, "limestone");
        addBlockCandidate("BiomesOPlenty", "rocks", 0, "limestone");
        addBlockCandidate("Mariculture", "limestone", 0, "limestone");
        addBlockCandidate("emashercore", "limestone", 0, "limestone");
        // addBlockCandidate("Geostrata", "geostrata_rock_limestone_smooth", 0, "limestone"); Does not work currently.
        addBlockCandidate("PFAAGeologica", "strongStone", 5, "marble");
        addBlockCandidate("PFAAGeologica", "mediumStone", 0, "limestone");

        addModVariations(modBlocks);
    }

    /**
     * Checks if the mod that the candidate block belongs to is loaded, then adds it to the List to be added to the variations of a CarvingGroup.
     * 
     * @param modname
     *            The name of the mod the candidate block belongs to.
     * @param blockname
     *            Full name of the candidate block.
     * @param metadata
     *            Metadata of the candidate block
     * @param carvablename
     *            Name of the Carvinggroup you are adding the candidate block to.
     */
    public static void addBlockCandidate(String modname, String blockname, int metadata, String carvablename)
    {
        if (Loader.isModLoaded(modname))
        {
            final Block block = GameRegistry.findBlock(modname, blockname);
            if (block != null)
            {
                modBlocks.add(new ModBlock(block, carvablename, metadata));
            }
        }
    }

    public static void addModVariations(List<ModBlock> blockList)
    {
        for (final ModBlock b : blockList)
        {
            final int order = Carving.chisel.getVariationCount(b.carvableName);
            Carving.chisel.addVariation(b.carvableName, b.blockObject, b.metaData, order);
        }
    }
}
