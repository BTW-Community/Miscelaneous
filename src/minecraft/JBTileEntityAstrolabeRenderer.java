package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class JBTileEntityAstrolabeRenderer extends TileEntitySpecialRenderer {
	private int temp = 0;
	//This method is called when minecraft renders a tile entity
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
		GL11.glPushMatrix();
		//This will move our renderer so that it will be on proper place in the world
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		JBTileEntityAstrolabe te = (JBTileEntityAstrolabe)tileEntity;
		/*Note that true tile entity coordina tes (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
		renderBlock(te, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, JBJorgesMiscellaneous.jbBlockAstrolabe);
		GL11.glPopMatrix();
	}
	//And this method actually renders your tile entity
	public void renderBlock(JBTileEntityAstrolabe te, World world, int i, int j, int k, Block block) {
		Tessellator tessellator = Tessellator.instance;

		RenderHelper.disableStandardItemLighting();
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);

		if (Minecraft.isAmbientOcclusionEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
		}
		else {
			GL11.glShadeModel(GL11.GL_FLAT);
		}

		bindTextureByName("/jbastromodtex/jbBlockAstrolabeSide.png");

		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0,0,0, 1.0, 1.0);
		tessellator.addVertexWithUV(0,1,0, 1.0, 0.0);
		tessellator.addVertexWithUV(1,1,0, 0.0, 0.0);
		tessellator.addVertexWithUV(1,0,0, 0.0, 1.0);

		tessellator.addVertexWithUV(1,0,0, 1.0, 1.0);
		tessellator.addVertexWithUV(1,1,0, 1.0, 0.0);
		tessellator.addVertexWithUV(1,1,1, 0.0, 0.0);
		tessellator.addVertexWithUV(1,0,1, 0.0, 1.0);

		tessellator.addVertexWithUV(0,1,1, 0.0, 0.0);
		tessellator.addVertexWithUV(0,0,1, 0.0, 1.0);
		tessellator.addVertexWithUV(1,0,1, 1.0, 1.0);
		tessellator.addVertexWithUV(1,1,1, 1.0, 0.0);

		tessellator.addVertexWithUV(0,0,1, 1.0, 1.0);
		tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
		tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
		tessellator.addVertexWithUV(0,0,0, 0.0, 1.0);

		tessellator.draw();

		bindTextureByName("/jbastromodtex/jbBlockAstrolabeBottom.png");		
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0,0,0, 0.0, 0.0);
		tessellator.addVertexWithUV(1,0,0, 1.0, 0.0);
		tessellator.addVertexWithUV(1,0,1, 1.0, 1.0);
		tessellator.addVertexWithUV(0,0,1, 0.0, 1.0);
		tessellator.draw();

		if (!te.isRedstonePowered()) {
			if (te.hasLens()) {
				if (!te.isInTheNether()) {
					bindTextureByName("/jbastromodtex/jbStarBackground.png");
				}
				else {
					bindTextureByName("/jbastromodtex/jbSoulBackground.png");
				}
			}
			else {
				bindTextureByName("/jbastromodtex/jbBlockAstrolabeTopOff.png");	
			}
		}
		else {
			if (te.hasMap()) {
				if (!te.isInTheNether()) {
					bindTextureByName("/jbastromodtex/jbStarBackground.png");
				}
				else {
					bindTextureByName("/jbastromodtex/jbSoulBackground.png");
				}
			}
			else {
				if (te.hasLens()) {
					if (!te.isInTheNether()) {
						bindTextureByName("/jbastromodtex/jbStarBackground.png");
					}
					else {
						bindTextureByName("/jbastromodtex/jbSoulBackground.png");
					}				
				}
				else {
					bindTextureByName("/jbastromodtex/jbBlockAstrolabeTop.png");	
				}
			}			
		}


		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
		tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
		tessellator.addVertexWithUV(1,1,1, 1.0, 1.0);
		tessellator.addVertexWithUV(1,1,0, 0.0, 1.0);
		tessellator.draw();

		if (!te.isInTheNether()) {
			bindTextureByName("/jbastromodtex/jbStarmap.png");

			if (te.hasMap()) {
				if (!te.isRedstonePowered()) {
					if (IsReceivingRedstonePower(world,
							te.xCoord,te.yCoord,te.zCoord))
						te.setRedstonePowered(true);
				}
				if (te.isRedstonePowered()) {
					double xMap = 7.8125f*0.064f - 0.032f + te.getMapX();
					double zMap = 7.8125f*0.064f - 0.032f	+ te.getMapZ();

					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0,1,0, xMap+0.064, zMap+0.064);
					tessellator.addVertexWithUV(0,1,1, xMap+0.064, zMap);
					tessellator.addVertexWithUV(1,1,1, xMap, zMap);
					tessellator.addVertexWithUV(1,1,0, xMap, zMap+0.064);
					tessellator.draw();
				}
			}

			if (te.hasLens()) {
				double x = 0;
				double z = 0;
				if (te.getScale()==1000) {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord)/1000000f
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord)/1000000f
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}
				else if (te.getScale()==100) {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord)/100000f 
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord)/100000f 
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}
				else {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord)/5000f
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord)/5000f
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0,1,0, x+0.064, z+0.064);
				tessellator.addVertexWithUV(0,1,1, x+0.064, z);
				tessellator.addVertexWithUV(1,1,1, x, z);
				tessellator.addVertexWithUV(1,1,0, x, z+0.064);
				tessellator.draw();
			}

			if (te.hasLens()) {
				GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
				bindTextureByName("/jbastromodtex/jbBlockAstrolabeTopOverlay.png");
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
				tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
				tessellator.addVertexWithUV(1,1,1, 1.0, 1.0);
				tessellator.addVertexWithUV(1,1,0, 0.0, 1.0);
				tessellator.draw();

				int time = (int)(world.getWorldTime()%24000);
				if((time>23500) || (time<12500)) {
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
				}
				else if (time>22500) {
					GL11.glColor4f(1.0f, 1.0f, 1.0f, (float)((time-22500f)/1000f));
				}
				else if (time<13500){
					GL11.glColor4f(1.0f, 1.0f, 1.0f, (float)(1-(time-12500f)/1000f));
				}
				else {
					GL11.glColor4f(1.0f, 1.0f, 1.0f, 0f);
				}

				if (time>22500 || time<13500) {
					bindTextureByName("/jbastromodtex/jbBlockAstrolabeSunOverlay.png");
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
					tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
					tessellator.addVertexWithUV(1,1,1, 1.0, 1.0);
					tessellator.addVertexWithUV(1,1,0, 0.0, 1.0);
					tessellator.draw();
				}
			}
			else {
				bindTextureByName("/jbastromodtex/jbBlockAstrolabeTopOverlay.png");
				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
				tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
				tessellator.addVertexWithUV(1,1,1, 1.0, 1.0);
				tessellator.addVertexWithUV(1,1,0, 0.0, 1.0);
				tessellator.draw();
			}
		}
		else {
			bindTextureByName("/jbastromodtex/jbNethermap.png");
			if (te.hasMap()) {
				if (!te.isRedstonePowered()) {
					if (IsReceivingRedstonePower(world,
							te.xCoord,te.yCoord,te.zCoord))
						te.setRedstonePowered(true);
				}
				if (te.isRedstonePowered()) {
					double xMap = 7.8125f*0.064f - 0.032f + te.getMapX() - world.getSpawnPoint().posX/1000;
					double zMap = 7.8125f*0.064f - 0.032f	+ te.getMapZ() - world.getSpawnPoint().posY/1000;

					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0,1,0, xMap+0.064, zMap+0.064);
					tessellator.addVertexWithUV(0,1,1, xMap+0.064, zMap);
					tessellator.addVertexWithUV(1,1,1, xMap, zMap);
					tessellator.addVertexWithUV(1,1,0, xMap, zMap+0.064);
					tessellator.draw();
				}
			}

			if (te.hasLens()) {
				double x = 0;
				double z = 0;
				if (te.getScale()==1000) {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord - world.getSpawnPoint().posX/8)/1000000f
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord - world.getSpawnPoint().posZ/8)/1000000f
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}
				else if (te.getScale()==100) {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord - world.getSpawnPoint().posX/8)/100000f 
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord - world.getSpawnPoint().posZ/8)/100000f 
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}
				else {
					x = 7.8125f*0.064f - 0.032f + (te.xCoord - world.getSpawnPoint().posX/8)/5000f
							+ te.getPosXLens()*0.001/(float)te.getScale();
					z = 7.8125f*0.064f - 0.032f + (te.zCoord - world.getSpawnPoint().posZ/8)/5000f
							+ te.getPosZLens()*0.001/(float)te.getScale();
				}

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(0,1,0, x+0.064, z+0.064);
				tessellator.addVertexWithUV(0,1,1, x+0.064, z);
				tessellator.addVertexWithUV(1,1,1, x, z);
				tessellator.addVertexWithUV(1,1,0, x, z+0.064);
				tessellator.draw();
			}

			bindTextureByName("/jbastromodtex/jbBlockAstrolabeTopOverlay.png");
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0,1,0, 0.0, 0.0);
			tessellator.addVertexWithUV(0,1,1, 1.0, 0.0);
			tessellator.addVertexWithUV(1,1,1, 1.0, 1.0);
			tessellator.addVertexWithUV(1,1,0, 0.0, 1.0);
			tessellator.draw();

			RenderHelper.enableStandardItemLighting();
		}
	}

	private boolean IsReceivingRedstonePower(World var1, int var2, int var3, int var4) {
		return var1.isBlockGettingPowered(var2, var3, var4) || var1.isBlockGettingPowered(var2, var3 + 1, var4);
	}
}