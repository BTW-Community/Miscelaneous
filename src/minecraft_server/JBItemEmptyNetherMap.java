package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;




public class JBItemEmptyNetherMap extends Item {
	public JBItemEmptyNetherMap(int var1) {
		super(var1);
		this.setUnlocalizedName("jbItemEmptyNetherMap");
	}
	
  public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 70000;
  }
  
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer ep, int par4) {
  	if (world.isRemote)
  		return;
		if (world.provider.dimensionId != -1) {
			ep.addChatMessage("You can only create a Soul Map on the Nether.");
			return;
		}

		ItemStack roseStack = GetFirstStackInHotbar(ep,Item.dyePowder.itemID,1);
		if (roseStack == null) {
			ep.addChatMessage("You need rose red in your hotbar to create a Soul Map.");
			return;
		}
	
		ItemStack bonemealStack = GetFirstStackInHotbar(ep,Item.dyePowder.itemID,15);
		if (bonemealStack == null) {
			ep.addChatMessage("You need bone meal in your hotbar to create a Soul Map.");
			return;
		}
		
		ItemStack lensStack = GetFirstStackInHotbar(ep,JBJorgesMiscellaneous.jbItemProjectionLens.itemID);
		if (lensStack == null) {
			ep.addChatMessage("You need a single type of nether projection lens in your hotbar to create a Soul Map.");
			return;
		}
		int lensType = lensStack.getItemDamage();
		
		if (lensType<3) {
			ep.addChatMessage("You need a nether projection lens in your hotbar to create a Soul Map.");
			return;
		}
		
		for (int i=0; i<9; ++i) {
			ItemStack auxStack = ep.inventory.getStackInSlot(i);
			if (auxStack != null && auxStack.itemID==JBJorgesMiscellaneous.jbItemProjectionLens.itemID) {
				if (auxStack.getItemDamage()!=lensType) {
					ep.addChatMessage("You need a single type of projection lens in your hotbar to create a Soul Map.");
					return;
				}
			}
		}
		
		consumeInventoryItem(ep.inventory,roseStack.itemID,1);
		consumeInventoryItem(ep.inventory,bonemealStack.itemID,15);
		consumeInventoryItem(ep.inventory,stack.itemID);
		
		int scale = 5;
		if (lensType == 3)
			scale = 5;
		else if (lensType == 4)
			scale = 100;
		else if (lensType == 5)
			scale = 1000;
		
    displayJBGuiNameNetherMap(ep, scale,(float)(MathHelper.floor_double(ep.posX)-world.getSpawnPoint().posX/8)/(1000*scale),
				(float)(MathHelper.floor_double(ep.posZ)-world.getSpawnPoint().posZ/8)/(1000*scale));

		return;
	}
	
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer ep) {
		ep.setItemInUse(stack, getMaxItemUseDuration(stack));
		return stack;
	}

	public ItemStack GetFirstStackInHotbar(EntityPlayer ep, int ID) {
		return GetFirstStackInHotbar(ep,ID,-1);
	}

	public ItemStack GetFirstStackInHotbar(EntityPlayer ep, int ID, int type) {
		for (int i=0; i<9; ++i) {
			ItemStack stack = ep.inventory.getStackInSlot(i);

			if (stack != null && stack.itemID==ID) {
				if (type>=0) {
					if (stack.getItemDamage()==type)
						return stack;
				}
				else
					return stack;
			}
		}

		return null;
	}
	
	public boolean consumeInventoryItem(InventoryPlayer ip, int ID) {
		return consumeInventoryItem(ip, ID, -1);
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
			if (type==-1 && ip.mainInventory[var2] != null && ip.mainInventory[var2].itemID == ID)
				return var2;
			if (ip.mainInventory[var2] != null && ip.mainInventory[var2].itemID == ID &&
					ip.mainInventory[var2].getItemDamage()==type) {
				return var2;
			}
		}

		return -1;
	}
	
	private void displayJBGuiNameNetherMap(EntityPlayer ep,int scale, float x, float z) {
		ServerOpenCustomInterface((EntityPlayerMP)ep, scale,x,z);
	}
	
	public static void ServerOpenCustomInterface(EntityPlayerMP var0, int scale, float x, float z) {
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
			DataOutputStream var5 = new DataOutputStream(var4);
			var5.writeInt(scale);
			var5.writeFloat(x);
			var5.writeFloat(z);
			Packet250CustomPayload var6 = new Packet250CustomPayload("JB|OIN", var4.toByteArray());
			var0.playerNetServerHandler.sendPacket(var6);
		}
		catch (Exception var7) {
			var7.printStackTrace();
		}
	}
}