package net.minecraft.src;

import java.util.Random;

public class JBBlockAstrolabe extends BlockContainer{

	public JBBlockAstrolabe(int i) {
		super(i, Material.iron);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		this.setLightOpacity(0);
		this.setHardness(5.0F);
		this.setResistance(10.0F);
		this.setStepSound(soundMetalFootstep);
		this.setUnlocalizedName("jbBlockAstrolabe");
	}

	public int tickRate(World var1) {
		return 4;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new JBTileEntityAstrolabe();
	}

	@Override
	public boolean isOpaqueCube() {
		return true;
	}

	private boolean IsReceivingRedstonePower(World var1, int var2, int var3, int var4) {
		return var1.isBlockGettingPowered(var2, var3, var4) || var1.isBlockGettingPowered(var2, var3 + 1, var4);
	}

	public int onBlockPlaced(World world, int x, int y, int z, int var5, float var6, float var7, float var8, int m) {
		world.scheduleBlockUpdate(x,y,z, this.blockID, this.tickRate(world));
		return m;
	}
	
	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int var6, float var7, float var8, float var9) {
		JBTileEntityAstrolabe tileEntity = (JBTileEntityAstrolabe)world.getBlockTileEntity(x, y, z);
		boolean disallowBlockPlace =
				(ep.getCurrentEquippedItem()==null ||
				(ep.getCurrentEquippedItem()!=null && (
				ep.getCurrentEquippedItem().getItem().itemID!=JBJorgesMiscellaneous.jbItemProjectionLens.itemID &&
				ep.getCurrentEquippedItem().getItem().itemID!=JBJorgesMiscellaneous.jbItemStarMap.itemID &&
				ep.getCurrentEquippedItem().getItem().itemID!=JBJorgesMiscellaneous.jbItemNetherMap.itemID)
				));


		if (!disallowBlockPlace)
			return disallowBlockPlace;

		switch (var6) {
		case 1:
			tileEntity.removeMap(ep);
			break;
		case 4:
			tileEntity.decLensX(1);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			world.markBlockForUpdate(x,y,z);
			break;
		case 5:
			tileEntity.incLensX(1);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			world.markBlockForUpdate(x,y,z);
			break;
		case 2:
			tileEntity.decLensZ(1);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			world.markBlockForUpdate(x,y,z);
			break;
		case 3:
			tileEntity.incLensZ(1);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
			world.markBlockForUpdate(x,y,z);
			break;
		}
		return disallowBlockPlace;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int var5, int var6) {
		TileEntity te = world.getBlockTileEntity(x,y,z);

		if (te!=null && te instanceof JBTileEntityAstrolabe) {
			((JBTileEntityAstrolabe)te).clearTileEntity();
		}
		super.breakBlock(world,x,y,z,var5,var6);
	}

	public void updateTick(World world, int x, int y, int z, Random var5) {
		TileEntity auxTE = world.getBlockTileEntity(x,y,z);
		if ((auxTE!=null && auxTE instanceof JBTileEntityAstrolabe)) {
			JBTileEntityAstrolabe te = (JBTileEntityAstrolabe)auxTE;
			if (IsReceivingRedstonePower(world,x,y,z)) {
				if (!te.isRedstonePowered()) { 
					te.setRedstonePowered(true);
				}
			}
			else {
				if (te.isRedstonePowered()) { 
					te.setRedstonePowered(false);
				}
			}
			te.setInTheNether(world.provider.dimensionId==-1);
			world.markBlockForUpdate(x,y,z);
		}
	}

	public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
	}
	
	public void RandomUpdateTick(World var1, int var2, int var3, int var4, Random var5) {
		var1.scheduleBlockUpdate(var2, var3, var4, this.blockID, this.tickRate(var1));
  }
}