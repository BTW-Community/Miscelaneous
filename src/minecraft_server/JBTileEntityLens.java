package net.minecraft.src;

public class JBTileEntityLens extends TileEntity implements FCITileEntityDataPacketHandler {
	private int type;
	private boolean skyClear;
	private boolean isRedstonePowered;

	public JBTileEntityLens() {
		super();
		this.type = -1;
		this.skyClear = false;
		isRedstonePowered = false;
	}
	
	public JBTileEntityLens(boolean skyClear) {
		super();
		this.type = -1;
		this.skyClear = skyClear;
		isRedstonePowered = false;
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound t) {
		super.writeToNBT(t);
		t.setInteger("tType",this.type);
		t.setBoolean("tSkyClear",this.skyClear);
		t.setBoolean("isRedstonePowered",isRedstonePowered);
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound t) {
		super.readFromNBT(t);

		if (t.hasKey("tType")) {
			this.type = t.getInteger("tType");
		}
		else {
			this.type = -1;
		}
		
		if (t.hasKey("tSkyClear")) {
			this.skyClear = t.getBoolean("tSkyClear");
		}
		else {
			this.skyClear = false;
		}
		
		if (t.hasKey("isRedstonePowered")) {
			this.isRedstonePowered = t.getBoolean("isRedstonePowered");
		}
		else {
			this.isRedstonePowered = false;
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setShort("t", (short)this.type);
		tag.setBoolean("s", this.skyClear);
		tag.setBoolean("irp",this.isRedstonePowered);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
	}

	@Override
	public void readNBTFromPacket(NBTTagCompound tag) {
		this.type = tag.getShort("t");
		this.skyClear = tag.getBoolean("s");
		this.isRedstonePowered = tag.getBoolean("irp");
		this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord,type);
		TileEntity te = worldObj.getBlockTileEntity(this.xCoord, this.yCoord-2, this.zCoord);
		if (te!=null && te instanceof JBTileEntityAstrolabe) {
			int auxScale = 5;
			if ((type==0)||(type==3))
				auxScale = 5;
			else if ((type==1)||(type==4))
				auxScale = 100;
			else if ((type==2)||(type==5))
				auxScale = 1000;
			((JBTileEntityAstrolabe)te).setScale(auxScale);			}
		else {
			this.worldObj.destroyBlock(this.xCoord, this.yCoord, this.zCoord,true);
		}
	}

	public void updateEntity() {
    if ((this.worldObj.getTotalWorldTime()) % 80L != 0L)
    	return;

		checkConditions(this.worldObj,this.xCoord, this.yCoord, this.zCoord);
	}
	
	public void clearTileEntity() {
		TileEntity te = worldObj.getBlockTileEntity(this.xCoord, this.yCoord-2, this.zCoord);
		if (te!=null && te instanceof JBTileEntityAstrolabe) {
			((JBTileEntityAstrolabe)te).setHasLens(false);
			((JBTileEntityAstrolabe)te).setPosXLens(0);
			((JBTileEntityAstrolabe)te).setPosZLens(0);
		}

    this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
    this.invalidate();
	}
	
	public boolean isSkyClear() {
		return skyClear;
	}

	public void setSkyClear(boolean skyClear) {
		this.skyClear = skyClear;
	}
	
	public boolean isRedstonePowered() {
		return isRedstonePowered;
	}

	public void setRedstonePowered(boolean b) {
		isRedstonePowered = b;
	}
	
	public void checkConditions(World world, int x, int y, int z) {
		if (type==-1) {
			int meta = worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			this.type = meta;
			this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);

			TileEntity te = worldObj.getBlockTileEntity(this.xCoord, this.yCoord-2, this.zCoord);
			if (te!=null && te instanceof JBTileEntityAstrolabe) {
				int toRemove = 0;
				int auxScale = 5;
				if ((type==0)||(type==3))
					auxScale = 5;
				else if ((type==1)||(type==4))
					auxScale = 100;
				else if ((type==2)||(type==5))
					auxScale = 1000;
				((JBTileEntityAstrolabe)te).setScale(auxScale);			}
			else {
				this.worldObj.destroyBlock(this.xCoord, this.yCoord, this.zCoord,true);
			}
		}
		
		boolean middleClear = false;
		
		if (world.canBlockSeeTheSky(x, y, z) && !world.isRaining()) {
			skyClear = true;
		}
		else
			skyClear = false;

		if (world.isAirBlock(x,y-1,z))
			middleClear = true;
		
		TileEntity auxTe = world.getBlockTileEntity(x,y-2,z);
		if (auxTe==null)
			return;
		
		if (auxTe instanceof JBTileEntityAstrolabe) {
			JBTileEntityAstrolabe te = (JBTileEntityAstrolabe)auxTe;

			if (world.provider.dimensionId == 0) {
				if (skyClear && middleClear && isRedstonePowered())
					te.setHasLens(true);
				else
					te.setHasLens(false);
			}
			
			else if (world.provider.dimensionId == -1) {
				if (middleClear && isRedstonePowered())
					te.setHasLens(true);
				else
					te.setHasLens(false);
			}

			world.markBlockForUpdate(x,y,z);
			world.markBlockForUpdate(x,y-2,z);
		}
	}
}