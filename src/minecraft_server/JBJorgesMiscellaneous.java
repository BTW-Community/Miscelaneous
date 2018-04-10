package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JBJorgesMiscellaneous extends FCAddOn {
	public static final String jbVersionString = "1.0 Orientation Sickness";
	public static JBJorgesMiscellaneous m_instance = new JBJorgesMiscellaneous();

	private static Block[] JBBlockArrowMarkerArray = new Block[16];

	private static int[] JBBlockArrowMarkerIDArray = new int[16];

	private static boolean hardcoreDayEnabled = false;
	private static long hardcoreDayTimedOut = 0;
	
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
    }
    	catch(Exception e) {
        return false;
    }    	
  }
	
	public static void vanillaConstruct() {}

	public void CreateAssociatedItemForBlock(Block var1) {
		int var2 = var1.blockID;
		ItemBlock var3 = null;
		var3 = new ItemBlock(var2 - 256);

		if (Block.blocksList[var2] != null && Item.itemsList[var2] == null) {
			Item.itemsList[var2] = var3;
		}
	}

	public void OnLanguageLoaded(StringTranslate var1) {
		for (int i=0;i<=15;i++) {
			this.AddBlockName(var1, JBBlockArrowMarkerArray[i], JBBlockArrowMarker.colorDisplayNames[i]+" Arrow Marker");	
		}
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

			while ((line = buffer.readLine()) != null) {
				String[] value = line.split("=");

				for (int i=0; i<value.length; ++i) {
					value[i] = value[i].trim();
				}

				for (int i=0; i<=15; ++i) {
					if (value[0].equals("jbBlockArrowMarker"+JBBlockArrowMarker.colorTextureNames[i]+"ID")) {
						JBBlockArrowMarkerIDArray[i] = (Integer.parseInt(value[1])>0)?Integer.parseInt(value[1]):JBBlockArrowMarkerIDArray[i];
						break;
					}	
				}

				if (value[0].equals("enableHardcoreDayCommand")) {
					hardcoreDayEnabled = (Integer.parseInt(value[1])==1)?true:false;					
				}
			}

			buffer.close();
		}
		catch (Exception e) {
			System.out.println("Failed to load Jorge's Miscellaneous config file");
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
}