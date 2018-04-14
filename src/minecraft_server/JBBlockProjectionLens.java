package net.minecraft.src;

import java.util.Random;

public class JBBlockProjectionLens extends BlockContainer{

	public JBBlockProjectionLens(int i) {
		super(i, Material.iron);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setLightOpacity(0);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundMetalFootstep);
		this.setUnlocalizedName("jbBlockProjectionLens");
	}
	
	public int tickRate(World var1) {
		return 4;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new JBTileEntityLens(false);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	private boolean IsReceivingRedstonePower(World var1, int var2, int var3, int var4) {
		return var1.isBlockIndirectlyGettingPowered(var2, var3, var4);
	}

	public int onBlockPlaced(World world, int x, int y, int z, int var5, float var6, float var7, float var8, int m) {
		world.scheduleBlockUpdate(x,y,z, this.blockID, this.tickRate(world));
		return m;
	}

	public int idDropped(int var1, Random var2, int var3) {
		return -1;
	}

	@Override
  public void breakBlock(World world, int x, int y, int z, int var5, int var6) {
		TileEntity te = world.getBlockTileEntity(x,y,z);

    if (te!=null && te instanceof JBTileEntityLens) {
        ((JBTileEntityLens)te).checkConditions(world, x, y, z);
    		FCUtilsItem.DropSingleItemAsIfBlockHarvested(world, x, y, z,
    				JBJorgesMiscellaneous.jbItemProjectionLens.itemID,
    				((JBTileEntityLens)te).getType());
        ((JBTileEntityLens)te).clearTileEntity();
    }
    super.breakBlock(world,x,y,z,var5,var6);
  }
	
	public void updateTick(World world, int x, int y, int z, Random var5) {
		TileEntity auxTE = world.getBlockTileEntity(x,y,z);
		if ((auxTE!=null && auxTE instanceof JBTileEntityLens)) {
			JBTileEntityLens te = (JBTileEntityLens)auxTE;
			if (IsReceivingRedstonePower(world,x,y,z)) {
				if (!te.isRedstonePowered()) { 
					te.setRedstonePowered(true);
					world.markBlockForUpdate(x,y,z);
				}
			}
			else {
				if (te.isRedstonePowered()) { 
					te.setRedstonePowered(false);
					world.markBlockForUpdate(x,y,z);
				}
			}
			te.checkConditions(world, x, y, z);
		}
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
	}
	
	public void RandomUpdateTick(World var1, int var2, int var3, int var4, Random var5) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
  }
}