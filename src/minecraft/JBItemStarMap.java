package net.minecraft.src;

import java.util.List;

public class JBItemStarMap extends Item {
	public JBItemStarMap(int var1) {
		super(var1);
		this.maxStackSize = 1;
		this.setUnlocalizedName("jbItemStarMap");
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer ep, World world, int x, int y, int z, int var7, float var8, float var9, float var10) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }

		if (world.provider.dimensionId != 0) {
			return false;
		}
		
		TileEntity auxTe = world.getBlockTileEntity(x,y,z);
		if (auxTe==null)
			return false;
		
		if (auxTe instanceof JBTileEntityAstrolabe) {
			JBTileEntityAstrolabe te = (JBTileEntityAstrolabe)auxTe;
			if (te.hasMap())
				return false;
			te.setMap(stack.getDisplayName(),stack.stackTagCompound.getFloat("MapX"),
																		stack.stackTagCompound.getFloat("MapZ"));
			stack.stackSize--;
			world.markBlockForUpdate(x,y,z);
		}
		
		return true;
	}
	
	@Override
  public void onCreated(ItemStack stack, World world, EntityPlayer ep) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }
  }
	
	public void setMapCoords(ItemStack stack, float x, float z) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }

		stack.stackTagCompound.setFloat("MapX",x);
		stack.stackTagCompound.setFloat("MapZ",z);
	}
	
	public float getMapX(ItemStack stack) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }
    return stack.stackTagCompound.getFloat("MapX");
	}
	
	public float getMapZ(ItemStack stack) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }
    return stack.stackTagCompound.getFloat("MapZ");
	}
	
	public String getMapName(ItemStack stack) {
    if( stack.stackTagCompound == null ){
    	stack.setTagCompound(new NBTTagCompound());
	  }
    return stack.getItemName();
	}
}