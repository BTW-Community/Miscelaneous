package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class JBCommandServerHardcoreDay extends CommandBase {
	@Override
	public String getCommandName() {
		return "hardcoreday";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/hardcoreday";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		if (var1 instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) var1;
			
			if (!JBJorgesMiscellaneous.isHardcoreDayEnabled()) {
				var1.sendChatToPlayer("\u00a7e" + "This command is not currently enabled.");
				return;
			}
		
			long lastTimedOut = (JBJorgesMiscellaneous.getHardcoreDayTimedOut()==-1)?-1:FCUtilsWorld.GetOverworldTimeServerOnly()-JBJorgesMiscellaneous.getHardcoreDayTimedOut();
			if (lastTimedOut>=900) {
				JBJorgesMiscellaneous.setHardcoreDayTimedOut(-1);
			}
			else if (lastTimedOut>=0) {
				var1.sendChatToPlayer("\u00a7e" + "This command is on a timeout.");
				return;
			}
			
			if (FCBetterThanWolves.IsSinglePlayerNonLan()) {
				var1.sendChatToPlayer("\u00a7e" + "You don't need this command right now.");
			}
			else if (MinecraftServer.getServer().getCurrentPlayerCount() > 1) {
				var1.sendChatToPlayer("\u00a7e" + "Don't be afraid, you are not alone.");
				return;
			}
			else {
				long serverTime = FCUtilsWorld.GetOverworldTimeServerOnly();
				long timePassed = serverTime-player.m_lTimeOfLastSpawnAssignment;
				if (timePassed>=0 && timePassed<=800) {
					makeItDay();
				}
				else {
					var1.sendChatToPlayer("\u00a7e" + "You can't use this command right now.");	
				}
			}
		}
	}
	
	private void makeItDay() {
		long serverTime = FCUtilsWorld.GetOverworldTimeServerOnly();
		serverTime = (serverTime / 24000L + 1L) * 24000L;

    for (int server = 0; server < MinecraftServer.getServer().worldServers.length; ++server)
    {
        WorldServer var25 = MinecraftServer.getServer().worldServers[server];
        var25.setWorldTime(serverTime);

        if (var25.worldInfo.isThundering())
        {
            var25.worldInfo.setThundering(false);
            MinecraftServer.getServer().getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(8, 0));
        }
    }
    JBJorgesMiscellaneous.setHardcoreDayTimedOut(serverTime);
	}
	
	@Override
  public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender) {
		return true;
  }

	// public static void ServerOpenCustomInterface(EntityPlayerMP var0, int
	// scale, float x, float z) {
	// try {
	// ByteArrayOutputStream var4 = new ByteArrayOutputStream();
	// DataOutputStream var5 = new DataOutputStream(var4);
	// var5.writeInt(scale);
	// var5.writeFloat(x);
	// var5.writeFloat(z);
	// Packet250CustomPayload var6 = new Packet250CustomPayload("JB|OIS",
	// var4.toByteArray());
	// var0.playerNetServerHandler.sendPacketToPlayer(var6);
	// }
	// catch (Exception var7) {
	// var7.printStackTrace();
	// }
	// }
}
