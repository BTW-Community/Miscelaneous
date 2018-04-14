package net.minecraft.src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import net.minecraft.server.MinecraftServer;

public class JBJorgesMiscellaneous extends FCAddOn {
	public static final String jbVersionString = "3.1b Starry Expanse";
	public static JBJorgesMiscellaneous m_instance = new JBJorgesMiscellaneous();

	private static Block[] JBBlockArrowMarkerArray = new Block[16];

	private static int[] JBBlockArrowMarkerIDArray = new int[16];

	private static boolean hardcoreDayEnabled = false;
	private static long hardcoreDayTimedOut = 0;
	
	public static Block jbBlockAstrolabe;
	private static int jbBlockAstrolabeID = 170;
	public static Block jbBlockProjectionLens;
	private static int jbBlockProjectionLensID = 171;
	
	public static Item jbItemAstrolabeFrame;
	private static int jbItemAstrolabeFrameID = 22522;
	public static Item jbItemProjectionLens;
	private static int jbItemProjectionLensID = 22219;
	public static Item jbItemEmptyStarMap;
	private static int jbItemEmptyStarMapID = 22220;
	public static Item jbItemStarMap;
	private static int jbItemStarMapID = 22221;
	public static Item jbItemEmptyNetherMap;
	private static int jbItemEmptyNetherMapID = 22218;
	public static Item jbItemNetherMap;
	private static int jbItemNetherMapID = 22217;
	
	private static HashMap<String,Timer> testingPlayerTimerMap = new HashMap<String,Timer>();
	
	@Override
	public void Initialize() {
		FCAddOnHandler.LogMessage("Jorge's Miscellaneous Version " + jbVersionString + " Initializing...");

		try {
			JBBlockArrowMarkerIDArray[0] = 4000;
			JBBlockArrowMarkerIDArray[1] = 4001;
			JBBlockArrowMarkerIDArray[2] = 4002;
			JBBlockArrowMarkerIDArray[3] = 4003;
			JBBlockArrowMarkerIDArray[4] = 4004;
			JBBlockArrowMarkerIDArray[5] = 4005;
			JBBlockArrowMarkerIDArray[6] = 4006;
			JBBlockArrowMarkerIDArray[7] = 4007;
			JBBlockArrowMarkerIDArray[8] = 4008;
			JBBlockArrowMarkerIDArray[9] = 4009;
			JBBlockArrowMarkerIDArray[10] = 4010;
			JBBlockArrowMarkerIDArray[11] = 4011;
			JBBlockArrowMarkerIDArray[12] = 4012;
			JBBlockArrowMarkerIDArray[13] = 4013;
			JBBlockArrowMarkerIDArray[14] = 4014;
			JBBlockArrowMarkerIDArray[15] = 4015;
			
			readConfigFile();
			
			for (int i=0;i<=15;i++) {
				JBBlockArrowMarkerArray[i] = new JBBlockArrowMarker(JBBlockArrowMarkerIDArray[i],i);
				CreateAssociatedItemForBlock(JBBlockArrowMarkerArray[i]);
			}
			
			for (int color=0;color<=15;color++) {
				if (color!=4 && color!=15) {
					AddVanillaRecipe(new ItemStack(JBBlockArrowMarkerArray[color],6,0),
							new Object[] {" D ","E F","SSS",
									'D', new ItemStack(Item.dyePowder, 1, color),
									'E', new ItemStack(Item.dyePowder, 1, color),
									'F', new ItemStack(Item.dyePowder, 1, color),
									'S', new ItemStack(Block.stone, 1)});
				}
				else {
					int maxDyeMetaMultiplier = 0;
					if (DecoInstalled())
						maxDyeMetaMultiplier = 1;
					
					for (int i=0;i<=maxDyeMetaMultiplier;i++) {
						for (int j=0;j<=maxDyeMetaMultiplier;j++) {
							for (int k=0;k<=maxDyeMetaMultiplier;k++) {
								int d = color+16*i;
								int e = color+16*j;
								int f = color+16*k;
								
								AddVanillaRecipe(new ItemStack(JBBlockArrowMarkerArray[color],6,0),
										new Object[] {" D ","E F","SSS",
												'D', new ItemStack(Item.dyePowder, 1, d),
												'E', new ItemStack(Item.dyePowder, 1, e),
												'F', new ItemStack(Item.dyePowder, 1, f),
												'S', new ItemStack(Block.stone, 1)});
							}
						}	
					}
				}
			}
			
			jbBlockAstrolabe = new JBBlockAstrolabe(jbBlockAstrolabeID);
			CreateAssociatedItemForBlock(jbBlockAstrolabe);

			jbBlockProjectionLens = new JBBlockProjectionLens(jbBlockProjectionLensID);
			CreateAssociatedItemForBlock(jbBlockProjectionLens);
			
			jbItemProjectionLens = new JBItemProjectionLens(jbItemProjectionLensID);

			TileEntity.addMapping(JBTileEntityAstrolabe.class, "jbBlockAstrolabe");

			TileEntity.addMapping(JBTileEntityLens.class, "jbBlockProjectionLens");
			
			jbItemStarMap = new JBItemStarMap(jbItemStarMapID);
			jbItemStarMap.SetBuoyancy(1.0F);
			jbItemEmptyStarMap = new JBItemEmptyStarMap(jbItemEmptyStarMapID);
			
			jbItemNetherMap = new JBItemNetherMap(jbItemNetherMapID);
			jbItemNetherMap.SetBuoyancy(1.0F);
			jbItemEmptyNetherMap = new JBItemEmptyNetherMap(jbItemEmptyNetherMapID);
			
			jbItemAstrolabeFrame = new JBItemAstrolabeFrame(jbItemAstrolabeFrameID);
			
			AddVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemAstrolabeFrame),
					new Object[] { " B ", "BIB", " B ", 'B', new ItemStack(Block.woodenButton), 'I',
							new ItemStack(Block.blockIron)});
			
			AddVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbBlockAstrolabe, 1, 0),
					new Object[] { " P ", "LF ", "RI ",
							'F', new ItemStack(JBJorgesMiscellaneous.jbItemAstrolabeFrame),
							'P' ,new ItemStack(Item.paper),
							'L', new ItemStack(FCBetterThanWolves.fcItemRedstoneLatch),
							'R', new ItemStack(Item.redstone),
							'I', new ItemStack(FCBetterThanWolves.fcLightBulbOff)});
			
			AddVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,0),
					new Object[] {"IDI","ILI","RNN",
								'I', new ItemStack(Item.ingotIron),
								'D', new ItemStack(Item.diamond),
								'L', new ItemStack(FCBetterThanWolves.fcLens),
								'R', new ItemStack(FCBetterThanWolves.fcItemRedstoneLatch),
								'N', new ItemStack(FCBetterThanWolves.fcItemNuggetIron)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,1), new Object[] {
					new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,0),
					new ItemStack(Item.diamond)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,2), new Object[] {
					new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,1),
					new ItemStack(Item.diamond)});
			
			AddVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,3),
					new Object[] {"IWI","ILI","RNN",
								'I', new ItemStack(Item.ingotIron),
								'W', new ItemStack(Item.skull.itemID, 1, 1),
								'L', new ItemStack(FCBetterThanWolves.fcLens),
								'R', new ItemStack(FCBetterThanWolves.fcItemRedstoneLatch),
								'N', new ItemStack(FCBetterThanWolves.fcItemNuggetIron)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,4), new Object[] {
					new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,3),
					new ItemStack(Item.skull.itemID, 1, 1)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,5), new Object[] {
					new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,4),
					new ItemStack(Item.skull.itemID, 1, 1)});
			
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyStarMap,1,0),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,0)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyStarMap,1,1),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,1)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyStarMap,1,2),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,2)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyNetherMap,1,3),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,3)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyNetherMap,1,4),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,4)});
			AddShapelessVanillaRecipe(new ItemStack(JBJorgesMiscellaneous.jbItemEmptyNetherMap,1,5),
					new Object[] {new ItemStack(Item.emptyMap),new ItemStack(JBJorgesMiscellaneous.jbItemProjectionLens,1,5)});
		} 
		catch (Exception e) {
      String var2 = "***Jorge's Miscellaneous Addon has not been properly installed. Please consult the readme.txt file for installation instructions***";
      FCAddOnHandler.LogMessage(e.getMessage());
      FCAddOnHandler.LogMessage(var2);
      e.printStackTrace();
      throw new RuntimeException(e);
		}
	}

	private boolean DecoInstalled() {
		try {
			Class.forName("AddonManager");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void vanillaConstruct() {
	}

	public void CreateAssociatedItemForBlock(Block var1) {
		int var2 = var1.blockID;
		ItemBlock var3 = null;
		var3 = new ItemBlock(var2 - 256);

		if (Block.blocksList[var2] != null && Item.itemsList[var2] == null) {
			Item.itemsList[var2] = var3;
		}
	}

	public void OnLanguageLoaded(StringTranslate var1) {
		for (int i = 0; i <= 15; i++) {
			this.AddBlockName(var1, JBBlockArrowMarkerArray[i], JBBlockArrowMarker.colorDisplayNames[i] + " Arrow Marker");
		}
		
	 	this.AddItemName(var1, jbItemProjectionLens, "Projection Lens");
  	this.AddItemName(var1, jbItemEmptyStarMap, "Empty Star Map");
  	this.AddItemName(var1, jbItemStarMap, "Star Map");
  	this.AddItemName(var1, jbItemEmptyNetherMap, "Empty Soul Map");
  	this.AddItemName(var1, jbItemAstrolabeFrame, "Astrolabe Frame");
  	this.AddItemName(var1, jbItemNetherMap, "Soul Map");
  	this.AddBlockName(var1, jbBlockAstrolabe, "Astrolabe");
  	this.AddBlockName(var1, jbBlockProjectionLens, "Projection Lens");
	}

	private void AddBlockName(StringTranslate var1, Block var2, String var3) {
		String var4 = var2.getUnlocalizedName() + ".name";
		var1.GetTranslateTable().put(var4, var3);
	}

	public static void AddVanillaRecipe(ItemStack var0, Object[] var1) {
		CraftingManager.getInstance().addRecipe(var0, var1);
	}

	private static void readConfigFile() {
		File file = new File(new File("."), "JorgesMiscellaneousConfig.txt");

		try {
			if (!file.exists()) {
				FCAddOnHandler.LogMessage("Jorge's Miscellaneous config file not found...");
				return;
			}

			BufferedReader buffer = new BufferedReader(new FileReader(file));
			String line = "";

			// Arrow Marker Block IDs
			while ((line = buffer.readLine()) != null) {
				String[] value = line.split("=");

				for (int i = 0; i < value.length; ++i) {
					value[i] = value[i].trim();
				}

				for (int i = 0; i <= 15; ++i) {
					if (value[0].equals("jbBlockArrowMarker" + JBBlockArrowMarker.colorTextureNames[i] + "ID")) {
						JBBlockArrowMarkerIDArray[i] = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1])
								: JBBlockArrowMarkerIDArray[i];
						break;
					}
				}

				// Hardcore Day command
				if (value[0].equals("enableHardcoreDayCommand")) {
					hardcoreDayEnabled = (Integer.parseInt(value[1]) == 1) ? true : false;
				}

				// Astrolabe Block IDs

				for (int i = 0; i < value.length; ++i) {
					value[i] = value[i].trim();
				}

				if (value[0].equals("jbBlockAstrolabeID")) {
					jbBlockAstrolabeID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1]) : jbBlockAstrolabeID;
				}
				if (value[0].equals("jbBlockProjectionLensID")) {
					jbBlockProjectionLensID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1])
							: jbBlockProjectionLensID;
				}
				if (value[0].equals("jbItemProjectionLensID")) {
					jbItemProjectionLensID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1])
							: jbItemProjectionLensID;
				}
				if (value[0].equals("jbItemEmptyStarMapID")) {
					jbItemEmptyStarMapID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1]) : jbItemEmptyStarMapID;
				}
				if (value[0].equals("jbItemStarMapID")) {
					jbItemStarMapID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1]) : jbItemStarMapID;
				}
				if (value[0].equals("jbItemEmptyNetherMapID")) {
					jbItemEmptyNetherMapID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1])
							: jbItemEmptyNetherMapID;
				}
				if (value[0].equals("jbItemAstrolabeFrameID")) {
					jbItemAstrolabeFrameID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1])
							: jbItemAstrolabeFrameID;
				}
				if (value[0].equals("jbItemNetherMapID")) {
					jbItemNetherMapID = (Integer.parseInt(value[1]) > 0) ? Integer.parseInt(value[1]) : jbItemNetherMapID;
				}
			}

			buffer.close();
		} catch (IOException e) {
			System.out.println("Failed to load Jorge's Miscellaneous config file");
			e.printStackTrace();
		}
	}

	public static void serverCustomPacketReceived(MinecraftServer ms, EntityPlayerMP epmp,
			Packet250CustomPayload packet) {
		try {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(packet.data));

			if (packet.channel.equals("JB|JBMISCOK")) {
				int length = dis.readInt();
				byte[] array = new byte[length];
				dis.read(array);
				
				String playerUsername = new String(array);
				
				if (testingPlayerTimerMap.containsKey(playerUsername)) {
					testingPlayerTimerMap.get(playerUsername).stop();
					testingPlayerTimerMap.remove(playerUsername);
				}
			}
			else if (packet.channel.equals("JB|CSM")) {
				int length = dis.readInt();
				String name = "";
				for (int i = 0; i < length; i++) {
					name = name + dis.readChar();
				}
				float x = dis.readFloat();
				float z = dis.readFloat();

				ItemStack starMap = new ItemStack(JBJorgesMiscellaneous.jbItemStarMap.itemID, 1, 0);
				starMap.setItemName(name);
				((JBItemStarMap) starMap.getItem()).setMapCoords(starMap, x, z);

				EntityItem entity = epmp.dropPlayerItem(starMap);
				entity.delayBeforeCanPickup = 0;
			} else if (packet.channel.equals("JB|CNM")) {
				int length = dis.readInt();
				String name = "";
				for (int i = 0; i < length; i++) {
					name = name + dis.readChar();
				}
				float x = dis.readFloat();
				float z = dis.readFloat();

				ItemStack netherMap = new ItemStack(JBJorgesMiscellaneous.jbItemNetherMap.itemID, 1, 0);
				netherMap.setItemName(name);
				((JBItemNetherMap) netherMap.getItem()).setMapCoords(netherMap, x, z);

				EntityItem entity = epmp.dropPlayerItem(netherMap);
				entity.delayBeforeCanPickup = 0;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getHardcoreDayTimedOut() {
		return hardcoreDayTimedOut;
	}

	public static void setHardcoreDayTimedOut(long hardcoreDayTimedOut) {
		JBJorgesMiscellaneous.hardcoreDayTimedOut = hardcoreDayTimedOut;
	}

	public static boolean isHardcoreDayEnabled() {
		return hardcoreDayEnabled;
	}

	private void AddItemName(StringTranslate var1, Item var2, String var3) {
		String var4 = var2.getUnlocalizedName() + ".name";
		var1.GetTranslateTable().put(var4, var3);
	}

	public static void AddShapelessVanillaRecipe(ItemStack var0, Object[] var1) {
		CraftingManager.getInstance().addShapelessRecipe(var0, var1);
	}

	public static void AddShapelessAnvilRecipe(ItemStack var0, Object[] var1) {
		FCCraftingManagerAnvil.getInstance().addShapelessRecipe(var0, var1);
	}
	
  public static void AddAnvilRecipe(ItemStack var0, Object[] var1) {
    FCCraftingManagerAnvil.getInstance().addRecipe(var0, var1);
  }
  
	public static void sendJBMiscTest(EntityPlayerMP var0) {
		final EntityPlayerMP player = var0;
		Timer t = new Timer(5000,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HashMap<String,Timer> map = JBJorgesMiscellaneous.getTestingPlayerTimerMap();
				if (map.containsKey(player.username)) {
					player.sendChatToPlayer("§4This server requires all clients to have Jorge's Miscelaneous addon. Please install it before joining.");
					player.sendChatToPlayer("§aAddon Link: http://tinyurl.com/jorgesmisc");
					testingPlayerTimerMap.remove(player.username);
				}
			}
		});
		
		testingPlayerTimerMap.put(player.username,t);
		t.setRepeats(false);
		t.start();
		
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
			byte[] data = new byte[0];
			DataOutputStream var5 = new DataOutputStream(var4);
			var5.writeInt(player.username.length());
			var5.writeBytes(player.username);
			Packet250CustomPayload var6 = new Packet250CustomPayload("JB|JBMISC", var4.toByteArray());
			var0.playerNetServerHandler.sendPacket(var6);
		}
		catch(Exception e) {}
	}
	
	public static HashMap<String,Timer> getTestingPlayerTimerMap() {
		return testingPlayerTimerMap;
	}
}