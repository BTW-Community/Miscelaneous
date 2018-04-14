package net.minecraft.src;

public class JBTileEntityAstrolabe extends TileEntity implements FCITileEntityDataPacketHandler {
	private int posXLens;
	private int posZLens;
	private int scale;
	private boolean hasMap;
	private String mapName;
	private float mapX;
	private float mapZ;
	private boolean isRedstonePowered;
	private boolean hasLens;
	private boolean inTheNether;

	public JBTileEntityAstrolabe() {
		posXLens = 0;
		posZLens = 0;
		scale = 5;
		hasMap = false;
		mapName = "";
		mapX = 0;
		mapZ = 0;
		hasLens = false;
		inTheNether = false;
		isRedstonePowered = false;
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	public void writeToNBT(NBTTagCompound t) {
		super.writeToNBT(t);
		t.setInteger("posXLens",posXLens);
		t.setInteger("posZLens",posZLens);
		t.setInteger("scale",scale);
		t.setBoolean("hasMap",hasMap);
		t.setBoolean("hasLens",hasLens);
		t.setBoolean("inTheNether",inTheNether);
		t.setString("mapName", mapName);
		t.setFloat("mapX",mapX);
		t.setFloat("mapZ",mapZ);
		t.setBoolean("isRedstonePowered",isRedstonePowered);
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	public void readFromNBT(NBTTagCompound t) {
		super.readFromNBT(t);

		if (t.hasKey("posXLens")) {
			this.posXLens = t.getInteger("posXLens");
		}
		else {
			this.posXLens = 0;
		}

		if (t.hasKey("posZLens")) {
			this.posZLens = t.getInteger("posZLens");
		}
		else {
			this.posZLens = 0;
		}

		if (t.hasKey("scale")) {
			this.scale = t.getInteger("scale");
		}
		else {
			this.scale = 5;
		}

		if (t.hasKey("hasLens")) {
			this.hasLens = t.getBoolean("hasLens");
		}
		else {
			this.hasLens = false;
		}
		
		if (t.hasKey("inTheNether")) {
			this.inTheNether = t.getBoolean("inTheNether");
		}
		else {
			this.inTheNether = false;
		}
		
		if (t.hasKey("hasMap")) {
			this.hasMap = t.getBoolean("hasMap");
		}
		else {
			this.hasMap = false;
		}

		if (t.hasKey("mapName")) {
			this.mapName = t.getString("mapName");
		}
		else {
			this.mapName = "";
		}

		if (t.hasKey("mapX")) {
			this.mapX = t.getFloat("mapX");
		}
		else {
			this.mapX = 0;
		}

		if (t.hasKey("mapZ")) {
			this.mapZ = t.getFloat("mapZ");
		}
		else {
			this.mapZ = 0;
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
		tag.setInteger("x", this.posXLens);
		tag.setInteger("z", this.posZLens);
		tag.setInteger("s", this.scale);
		tag.setBoolean("l", (this.hasLens));
		tag.setBoolean("itn", (this.inTheNether));
		tag.setBoolean("m", (this.hasMap));
		tag.setString("mn", (this.mapName));
		tag.setFloat("mx", this.mapX);
		tag.setFloat("mz", this.mapZ);
		tag.setBoolean("irp",this.isRedstonePowered);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
	}

	@Override
	public void readNBTFromPacket(NBTTagCompound tag) {
		this.posXLens = tag.getInteger("x");
		this.posZLens = tag.getInteger("z");
		this.scale = tag.getInteger("s");
		this.hasLens = tag.getBoolean("l");
		this.inTheNether = tag.getBoolean("itn");
		this.hasMap = tag.getBoolean("m");
		this.mapName = tag.getString("mn");
		this.mapX = tag.getFloat("mx");
		this.mapZ = tag.getFloat("mz");
		this.isRedstonePowered = tag.getBoolean("irp");
		this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
	}

	public int getPosXLens() {
		return posXLens;
	}

	public void setPosXLens(int posXLens) {
		this.posXLens = posXLens;
	}

	public int getPosZLens() {
		return posZLens;
	}

	public void setPosZLens(int posZLens) {
		this.posZLens = posZLens;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public int getScale() {
		return this.scale;
	}

	public void resetLens() {
		posXLens = 0;
		posZLens = 0;
	}

	public void incLensX(int i) {
		if (!hasLens())
			return;
		posXLens +=(i*scale);
	}

	public void decLensX(int i) {
		if (!hasLens())
			return;
		posXLens -=(i*scale);
	}

	public void incLensZ(int i) {
		if (!hasLens())
			return;
		posZLens +=(i*scale);
	}

	public void decLensZ(int i) {
		if (!hasLens())
			return;
		posZLens -=(i*scale);
	}

	public boolean hasMap() {
		return this.hasMap;
	}

	public float getMapX() {
		return mapX;
	}

	public float getMapZ() {
		return mapZ;
	}

	public void removeMap(EntityPlayer ep) {
		if (this.hasMap) {
			ItemStack is = null;
			if (this.worldObj.provider.dimensionId==0) {
				is = new ItemStack(JBJorgesMiscellaneous.jbItemStarMap.itemID,1,0);
				
				((JBItemStarMap)is.getItem()).setMapCoords(is,this.mapX,this.mapZ);
				is.setItemName(mapName);
				EntityItem entity = ep.dropPlayerItem(is);
				entity.delayBeforeCanPickup = 0;
			}
			else if (this.worldObj.provider.dimensionId==-1) {
				is = new ItemStack(JBJorgesMiscellaneous.jbItemNetherMap.itemID,1,0);
				
				((JBItemNetherMap)is.getItem()).setMapCoords(is,this.mapX,this.mapZ);
				is.setItemName(mapName);
				EntityItem entity = ep.dropPlayerItem(is);
				entity.delayBeforeCanPickup = 0;
			}

			this.hasMap = false;
			this.mapName = "";
			this.mapX = 0;
			this.mapZ = 0;
		}
	}
	
	public void breakMap() {
		if (this.hasMap) {
			if (this.worldObj.provider.dimensionId==0) {
				ItemStack is = new ItemStack(JBJorgesMiscellaneous.jbItemStarMap.itemID,1,0);
				((JBItemStarMap)is.getItem()).setMapCoords(is,this.mapX,this.mapZ);
				is.setItemName(mapName);
				FCUtilsItem.DropStackAsIfBlockHarvested(this.worldObj,
						this.xCoord,this.yCoord,this.zCoord,is);
				this.hasMap = false;
				this.mapName = "";
				this.mapX = 0;
				this.mapZ = 0;
			}
			else if (this.worldObj.provider.dimensionId==-1) {
				ItemStack is = new ItemStack(JBJorgesMiscellaneous.jbItemNetherMap.itemID,1,0);
				((JBItemNetherMap)is.getItem()).setMapCoords(is,this.mapX,this.mapZ);
				is.setItemName(mapName);
				FCUtilsItem.DropStackAsIfBlockHarvested(this.worldObj,
						this.xCoord,this.yCoord,this.zCoord,is);
				this.hasMap = false;
				this.mapName = "";
				this.mapX = 0;
				this.mapZ = 0;
			}
		}
	}

	public void setMap(String mn, float mx, float mz) {
		if (hasMap)
			return;

		this.hasMap = true;
		this.mapName = mn;
		this.mapX = mx;
		this.mapZ = mz;
	}

	public void clearTileEntity() {
		breakMap();
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord+2, this.zCoord) ==
				JBJorgesMiscellaneous.jbBlockProjectionLens.blockID) {
			this.worldObj.destroyBlock(this.xCoord, this.yCoord+2, this.zCoord,true);
		}		
		
		this.worldObj.removeBlockTileEntity(this.xCoord, this.yCoord, this.zCoord);
		this.invalidate();
	}

	public boolean isRedstonePowered() {
		return isRedstonePowered;
	}

	public void setRedstonePowered(boolean b) {
		isRedstonePowered = b;
	}

	public boolean hasLens() {
		return hasLens;
	}

	public void setHasLens(boolean hasLens) {
		this.hasLens = hasLens;
	}

	public boolean isInTheNether() {
		return inTheNether;
	}

	public void setInTheNether(boolean inTheNether) {
		this.inTheNether = inTheNether;
	}
}