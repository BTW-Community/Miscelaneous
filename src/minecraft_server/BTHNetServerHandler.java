package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BTHNetServerHandler extends NetServerHandler {

	public final MinecraftServer mcServer;

	public BTHNetServerHandler(MinecraftServer aServer, INetworkManager aNetManager, EntityPlayerMP aPlayer) {
		super(aServer, aNetManager, aPlayer);
		
		String prefix = getClassPackagePrefix();

		this.mcServer = aServer;

		try {
			if (Class.forName(prefix+"JBJorgesMiscellaneous") != null) {
				Class.forName(prefix+"JBJorgesMiscellaneous").getMethod("sendJBMiscTest", EntityPlayerMP.class).invoke(null, aPlayer);
				if (aServer.getCommandManager() != null && aServer.getCommandManager() instanceof ServerCommandManager) {
					Object object = Class.forName(prefix+"JBCommandServerHardcoreDay").newInstance();
					((ServerCommandManager)aServer.getCommandManager()).registerCommand((CommandBase)object);
				}			
			}
		} catch (Throwable e) {}
	}

	public void handleCustomPayload(Packet250CustomPayload aPacket) {
		super.handleCustomPayload(aPacket);
		
		String prefix = getClassPackagePrefix();

		String[] mods = new String[] { "JBJorgesMiscellaneous", "AddonManager" };
		for (String mod : mods) {
			try {
				Class.forName(prefix+mod).getMethod("serverCustomPacketReceived", MinecraftServer.class, EntityPlayerMP.class, Packet250CustomPayload.class).invoke(null, this.mcServer, this.playerEntity, aPacket);
			} catch (Throwable e) {}
		}
	}
	
	private String getClassPackagePrefix() {
		try {
			if (Class.forName("FCBetterThanWolves") != null) {
				return "";
			}
		}
		catch(Throwable e) {}
		try {
			if (Class.forName("net.minecraft.src.FCBetterThanWolves") != null) {
				return "net.minecraft.src.";
			}
		}
		catch(Throwable e) {}
		return "";
	}
}