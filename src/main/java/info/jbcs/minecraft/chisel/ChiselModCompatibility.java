package info.jbcs.minecraft.chisel;

import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import info.jbcs.minecraft.chisel.carving.Carving;
import net.minecraft.block.Block;

public class ChiselModCompatibility
{


    abstract class ClassCompat
    {
        Class cl;

        public ClassCompat(String name)
        {
            try
            {
                if((cl = Class.forName(name)) != null)
                    action();
            } catch(ClassNotFoundException e)
            {
                //e.printStackTrace();
            } catch(IllegalArgumentException e)
            {
                e.printStackTrace();
            } catch(IllegalAccessException e)
            {
                e.printStackTrace();
            } catch(NoSuchFieldException e)
            {
                e.printStackTrace();
            } catch(SecurityException e)
            {
                e.printStackTrace();
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        abstract void action() throws Exception;

        abstract class BlockCompat
        {
            Block block;

            public BlockCompat(String variableName) throws Exception
            {
                block = (Block) ClassCompat.this.cl.getField(variableName).get(null);

                if(block != null)
                    action();
            }

            abstract void action() throws Exception;
        }

        ;
    }

    ;

    abstract class ClassBlockCompat
    {
        Block block;

        ClassBlockCompat(final String className, final String blockName)
        {
            new ClassCompat(className)
            {
                @Override
                void action() throws Exception
                {
                    new BlockCompat(blockName)
                    {
                        @Override
                        void action() throws Exception
                        {
                            ClassBlockCompat.this.doAction(block);
                        }
                    };
                }
            };
        }

        void doAction(Block b)
        {
            block = b;

            action();
        }

        abstract void action();
    }


    public void postInit(FMLPostInitializationEvent event)
    {
        new ClassBlockCompat("num.numirp.block.ModBlocks", "blockDecor")
        {
            @Override
            void action()
            {
                Carving.chisel.addVariation("marble", block, 0, 99);
            }
        };
        new ClassCompat("Reika.GeoStrata.GeoStrata")
        {
            @Override
            void action() throws Exception
            {
                Block[] blocks = (Block[]) cl.getField("blocks").get(null);

                Carving.chisel.addVariation("marble", blocks[0], 2, 99);

                Carving.chisel.addVariation("limestone", blocks[0], 3, 99);
            }
        };
    }

}
