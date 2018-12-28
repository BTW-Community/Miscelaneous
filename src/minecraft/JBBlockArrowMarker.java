package net.minecraft.src;

public class JBBlockArrowMarker extends Block {
	public static final String[] colorDisplayNames = new String[] { "Black", "Red", "Green", "Brown", "Blue", "Purple",
			"Cyan", "Light Gray", "Dark Gray", "Pink", "Lime", "Yellow", "Light Blue", "Magenta", "Orange", "White" };
	public static final String[] colorTextureNames = new String[] { "Black", "Red", "DarkGreen", "Brown", "DarkBlue",
			"Purple", "Cyan", "LightGray", "DarkGray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White" };

	private int color = 0;

	public JBBlockArrowMarker(int ID, int color) {
		super(ID, Material.rock);
		this.setHardness(0.8F);
		this.setResistance(10.0F);
		this.color = color;
		this.InitBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F - 0.0625F, 0.0625F, 1.0F - 0.0625F);
		this.setUnlocalizedName("JBBlockArrowMarker" + colorTextureNames[color]);
		this.setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public void registerIcons(IconRegister var1) {
		this.blockIcon = var1.registerIcon("JBBlockArrowMarker" + colorTextureNames[color]);
	}

	public int GetFacing(IBlockAccess var1, int var2, int var3, int var4) {
		return var1.getBlockMetadata(var2, var3, var4);
	}

	public void SetFacing(World var1, int var2, int var3, int var4, int var5) {
		var1.setBlockMetadataWithNotify(var2, var3, var4, var5);
	}

	public int GetFacingFromMetadata(int Meta) {
		return Meta;
	}

	public int SetFacingInMetadata(int var1, int var2) {
		return var2;
	}

	public boolean CanRotateOnTurntable(IBlockAccess var1, int var2, int var3, int var4) {
		return true;
	}

	public boolean CanTransmitRotationHorizontallyOnTurntable(IBlockAccess var1, int var2, int var3, int var4) {
		return false;
	}

	public boolean CanTransmitRotationVerticallyOnTurntable(IBlockAccess var1, int var2, int var3, int var4) {
		return false;
	}

	public boolean RotateAroundJAxis(World var1, int var2, int var3, int var4, boolean var5) {
		return FCUtilsMisc.StandardRotateAroundJ(this, var1, var2, var3, var4, var5);
	}

	public int RotateMetadataAroundJAxis(int var1, boolean var2) {
		return FCUtilsMisc.StandardRotateMetadataAroundJ(this, var1, var2);
	}

	public boolean ToggleFacing(World var1, int var2, int var3, int var4, boolean var5) {
		this.RotateAroundJAxis(var1, var2, var3, var4, var5);
		return true;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	protected boolean canPlaceMarkerOn(World var1, int var2, int var3, int var4) {
		return FCUtilsWorld.DoesBlockHaveSmallCenterHardpointToFacing(var1, var2, var3, var4, 1);
	}

	public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
		FCUtilsBlockPos var6 = new FCUtilsBlockPos(var2, var3, var4, 0);
		return (FCUtilsWorld.DoesBlockHaveLargeCenterHardpointToFacing(var1, var6.i, var6.j, var6.k, 1));
	}

	public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
		int var7 = FCUtilsMisc.ConvertOrientationToFlatBlockFacingReversed(var5);
		this.SetFacing(var1, var2, var3, var4, var7);
	}

	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
		this.dropBlockIfCantStay(par1World, par2, par3, par4);
	}

	protected boolean dropBlockIfCantStay(World par1World, int par2, int par3, int par4) {
		if (!this.canPlaceBlockAt(par1World, par2, par3, par4)) {
			if (par1World.getBlockId(par2, par3, par4) == this.blockID) {
				this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
				par1World.setBlockToAir(par2, par3, par4);
			}

			return false;
		} else {
			return true;
		}
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
		return GetBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
		return GetBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
	}

	public AxisAlignedBB GetBlockBoundsFromPoolForItemRender(int iItemDamage) {
		return GetFixedBlockBoundsFromPool();
	}

	public AxisAlignedBB GetBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
		return GetFixedBlockBoundsFromPool();
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess var1, int var2, int var3, int var4, int var5) {
		return true;
	}

	public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7,
			float var8, float var9) {
		ItemStack var10 = var5.getCurrentEquippedItem();

		if (var10 != null) {
			return false;
		} else {
			if (!var1.isRemote) {
				this.ToggleFacing(var1, var2, var3, var4, false);
				FCUtilsMisc.PlayPlaceSoundForBlock(var1, var2, var3, var4);
			}

			return true;
		}
	}

	@Override
	public boolean RenderBlock(RenderBlocks var1, int var2, int var3, int var4) {
		super.RenderBlock(var1, var2, var3, var4);
		IBlockAccess var5 = var1.blockAccess;

		int metadata = var5.getBlockMetadata(var2, var3, var4);
		int facing = GetFacingFromMetadata(metadata);

		if (facing == 2) {
			var1.SetUvRotateTop(3);
		} else if (facing == 4) {
			var1.SetUvRotateTop(1);
		} else if (facing == 5) {
			var1.SetUvRotateTop(2);
		}

		var1.renderFaceYPos(this, (double) var2, (double) var3, (double) var4, this.blockIcon);
		var1.setRenderBounds( GetBlockBoundsFromPoolBasedOnState(var5, var2, var3, var4));
		var1.renderStandardBlock(this, var2, var3, var4);
		var1.ClearUvRotation();

		return true;
	}
}