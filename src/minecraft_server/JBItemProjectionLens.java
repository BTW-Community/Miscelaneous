package net.minecraft.src;

import java.util.List;

public class JBItemProjectionLens extends Item{
	public static final String[] lensNames = new String[] {"Lens 5x", "Lens 100x", "Lens 1000x", "Nether Lens 5x", "Nether Lens 100x", "Nether Lens 1000x"};

	public JBItemProjectionLens(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName("jbItemProjectionLens");
	}

	@Override
	public void OnUsedInCrafting(EntityPlayer ep, ItemStack stack) {
		if (stack.getItem().itemID == JBJorgesMiscellaneous.jbItemEmptyStarMap.itemID) {
			ItemStack newItem = new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens.itemID,1,stack.getItemDamage());
	    EntityItem entity = ep.dropPlayerItem(newItem);
	    entity.delayBeforeCanPickup = 0;
	    stack.setItemDamage(0);
		}
		if (stack.getItem().itemID == JBJorgesMiscellaneous.jbItemEmptyNetherMap.itemID) {
			ItemStack newItem = new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens.itemID,1,stack.getItemDamage());
	    EntityItem entity = ep.dropPlayerItem(newItem);
	    entity.delayBeforeCanPickup = 0;
	    stack.setItemDamage(0);
		}
	}
	
	/**
	 * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
	 * different names based on their damage or NBT.
	 */
	public String getItemDisplayName(ItemStack par1ItemStack) {
		int damage = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 5);
		return lensNames[damage];
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer ep, World world,
													 int x, int y, int z,
													 int par7, float par8, float par9, float par10) {
		int damage = stack.getItemDamage();
		
		if (world.provider.dimensionId == 0 && damage >2) {
			return false;
		}
		else if (world.provider.dimensionId == -1 && damage <3) {
			return false;
		}
		
		int blockClickedID = world.getBlockId(x,y,z);
		if (blockClickedID == JBJorgesMiscellaneous.jbBlockAstrolabe.blockID) {
			if (world.isAirBlock(x,y+2,z)) {
				int meta = Block.blocksList[JBJorgesMiscellaneous.jbBlockProjectionLens.blockID].onBlockPlaced(world,x,y+2,z,0,par8,par9,par10,damage);
				world.setBlock(x,y+2,z,JBJorgesMiscellaneous.jbBlockProjectionLens.blockID, meta, 2);
				
				TileEntity te = world.getBlockTileEntity(x,y+2,z);
				if (te!=null && te instanceof JBTileEntityLens) {
					((JBTileEntityLens)te).setType(meta);
					((JBTileEntityLens)te).checkConditions(world, x, y+2, z);
				}
				consumeInventoryItem(ep.inventory,stack.itemID,damage);
				
				world.markBlockForUpdate(x,y,z);
			}
			else if (world.getBlockId(x,y+2,z) == JBJorgesMiscellaneous.jbBlockProjectionLens.blockID) {
				int meta = world.getBlockMetadata(x,y+2,z);
				if (meta != damage) {
					world.setBlockMetadataWithNotify(x,y+2,z,damage);
					TileEntity te = world.getBlockTileEntity(x,y+2,z);
					if (te!=null && te instanceof JBTileEntityLens) {
						((JBTileEntityLens)te).setType(damage);
						((JBTileEntityLens)te).checkConditions(world, x, y+2, z);
					}
					consumeInventoryItem(ep.inventory,stack.itemID,damage);
					world.markBlockForUpdate(x,y+2,z);
					ItemStack newItem = new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens.itemID,1,meta);
			    EntityItem entity = ep.dropPlayerItem(newItem);
			    entity.delayBeforeCanPickup = 0;
				}
			}
		}
		else if (blockClickedID == JBJorgesMiscellaneous.jbBlockProjectionLens.blockID) {
			int meta = world.getBlockMetadata(x,y,z);
			if (meta != damage) {
				world.setBlockMetadataWithNotify(x,y,z,damage);
				TileEntity te = world.getBlockTileEntity(x,y,z);
				if (te!=null && te instanceof JBTileEntityLens) {
					((JBTileEntityLens)te).setType(damage);
					((JBTileEntityLens)te).checkConditions(world, x, y, z);
				}
				consumeInventoryItem(ep.inventory,stack.itemID,damage);
				world.markBlockForUpdate(x,y,z);
				ItemStack newItem = new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens.itemID,1,meta);
		    EntityItem entity = ep.dropPlayerItem(newItem);
		    entity.delayBeforeCanPickup = 0;
			}
		}
		
		return true;
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	public void getSubItems(int ID, CreativeTabs par2CreativeTabs, List list) {
		for (int i=0; i<=5; ++i) {
			list.add(new ItemStack(ID, 1, i));
		}
	}

	public boolean consumeInventoryItem(InventoryPlayer ip, int ID, int type) {
		int var2 = this.getInventorySlotContainItem(ip,ID,type);
		if (var2 < 0) {
			return false;
		}
		else {
			if (--ip.mainInventory[var2].stackSize <= 0) {
				ip.mainInventory[var2] = null;
			}
			return true;
		}
	}
	
	private int getInventorySlotContainItem(InventoryPlayer ip, int ID, int type) {
		for (int var2 = 0; var2 < ip.mainInventory.length; ++var2) {
			if (ip.mainInventory[var2] != null && ip.mainInventory[var2].itemID == ID &&
					ip.mainInventory[var2].getItemDamage()==type) {
				return var2;
			}
		}

		return -1;
	}
}