package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class JBNetServerHandler extends CustomBTWAddonNetServerHandler{

	public final MinecraftServer mcServer;
	public EntityPlayerMP playerEntity;

	public JBNetServerHandler(MinecraftServer aServer, INetworkManager aNetManager, EntityPlayerMP aPlayer) {
		super(aServer, aNetManager, aPlayer);
		
		this.mcServer = aServer;
		this.playerEntity = aPlayer;

		JBJorgesMiscellaneous.sendJBMiscTest(aPlayer);

		if (aServer.getCommandManager() != null && aServer.getCommandManager() instanceof ServerCommandManager) {
			JBCommandServerHardcoreDay jbCommandServerHardcoreDay = new JBCommandServerHardcoreDay();
			((ServerCommandManager)aServer.getCommandManager()).registerCommand(jbCommandServerHardcoreDay);
		}			
	}

	public void handleCustomPayload(Packet250CustomPayload aPacket) {
		JBJorgesMiscellaneous.serverCustomPacketReceived(this.mcServer, this.playerEntity, aPacket);
	}
}