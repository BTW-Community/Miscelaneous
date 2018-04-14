package net.minecraft.src;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class JBTileEntityLensRenderer extends TileEntitySpecialRenderer {
	private int temp = 0;
	//This method is called when minecraft renders a tile entity
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
		GL11.glPushMatrix();
		//This will move our renderer so that it will be on proper place in the world
		GL11.glTranslatef((float)d, (float)d1, (float)d2);
		JBTileEntityLens te = (JBTileEntityLens)tileEntity;
		/*Note that true tile entity coordina tes (tileEntity.xCoord, etc) do not match to render coordinates (d, etc) that are calculated as [true coordinates] - [player coordinates (camera coordinates)]*/
		renderBlock(te, tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, JBJorgesMiscellaneous.jbBlockAstrolabe);
		GL11.glPopMatrix();
	}
	//And this method actually renders your tile entity
	public void renderBlock(JBTileEntityLens te, World world, int i, int j, int k, Block block) {
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
		
		int nLenses = 1;
		
		if ((te.getType() == 2) || (te.getType() == 5)) {
			nLenses = 3;
			drawLensProtuberance(tessellator, 1, world.provider.dimensionId);
			drawLensProtuberance(tessellator, 2, world.provider.dimensionId);
			drawLensProtuberance(tessellator, 3, world.provider.dimensionId);			
		}
		else if ((te.getType() == 1)||(te.getType() == 4)) {
			nLenses = 2; 
			drawLensProtuberance(tessellator, 1, world.provider.dimensionId);
			drawLensProtuberance(tessellator, 2, world.provider.dimensionId);
		}
		else if ((te.getType() == 0)||(te.getType() == 3)) {
			nLenses = 1;
			drawLensProtuberance(tessellator, 1, world.provider.dimensionId);
		}
		
		if (world.provider.dimensionId == 0) {
			if (te.isSkyClear()) {
				if (te.isRedstonePowered()) {
					drawLensBody(tessellator,0,1,nLenses);
				}
				else {
					drawLensBody(tessellator,0,0,nLenses);
				}
			}
			else {
				drawLensBody(tessellator,0,0,nLenses);
			}
		}
		else if (world.provider.dimensionId == -1) {
			if (te.isRedstonePowered()) {
				drawLensBody(tessellator,-1,1,nLenses);
			}
			else {
				drawLensBody(tessellator,-1,0,nLenses);
			}
		}


	}
	
	// Location: Nether=-1, Overworld=0
	private void drawLensBody(Tessellator tessellator, int location, int state, int nLenses) {
		double r2 = 7d/16d;
		double edge = 3d/16d;
		
		if (location == 0) {
			if (nLenses == 1)
				bindTextureByName("/jbastromodtex/jbLens1NormalBody.png");
			else if (nLenses == 2)
				bindTextureByName("/jbastromodtex/jbLens2NormalBody.png");
			else
				bindTextureByName("/jbastromodtex/jbLens3NormalBody.png");
		}
		else {
			if (nLenses == 1)
				bindTextureByName("/jbastromodtex/jbLens1NetherBody.png");
			else if (nLenses == 2)
				bindTextureByName("/jbastromodtex/jbLens2NetherBody.png");
			else
				bindTextureByName("/jbastromodtex/jbLens3NetherBody.png");
		}
		
		
		// Lens body sides
		tessellator.startDrawing(9); 
		tessellator.addVertexWithUV(edge       , 1    ,r2     , 0.0    , 0.0  );
		tessellator.addVertexWithUV(edge       , 1    ,1-r2   , 2d/16   , 0.0  );
		tessellator.addVertexWithUV(edge       , 0    ,1-r2   , 2d/16   , 1 );
		tessellator.addVertexWithUV(edge       , 0    ,r2     , 0.0    , 1  );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(edge    , 1    ,1-r2   , (2d/16)         , 0.0  );
		tessellator.addVertexWithUV(r2      , 1    ,1-edge , (2d/16) + 5d/16 , 0.0  );
		tessellator.addVertexWithUV(r2      , 0    ,1-edge , (2d/16) + 5d/16 , 1 );
		tessellator.addVertexWithUV(edge    , 0    ,1-r2   , (2d/16)         , 1 );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(r2      , 1    ,1-edge      , 0.0    , 0.0  );
		tessellator.addVertexWithUV(1-r2    , 1    ,1-edge      , 2d/16      , 0.0  );
		tessellator.addVertexWithUV(1-r2    , 0    ,1-edge      , 2d/16      , 1 );
		tessellator.addVertexWithUV(r2      , 0    ,1-edge      , 0.0    , 1  );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(1-r2    , 1    ,1-edge , (2d/16)         , 0.0  );
		tessellator.addVertexWithUV(1-edge  , 1    ,1-r2   , (2d/16) + 5d/16 , 0.0  );
		tessellator.addVertexWithUV(1-edge  , 0    ,1-r2   , (2d/16) + 5d/16 , 1 );
		tessellator.addVertexWithUV(1-r2    , 0    ,1-edge , (2d/16)         , 1  );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(1-edge       , 1    ,1-r2   , 0.0    , 0.0  );
		tessellator.addVertexWithUV(1-edge       , 1    ,r2     , 2d/16      , 0.0  );
		tessellator.addVertexWithUV(1-edge       , 0    ,r2     , 2d/16      , 1 );
		tessellator.addVertexWithUV(1-edge       , 0    ,1-r2   , 0.0    , 1  );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(1-edge       , 1    ,r2     , (2d/16)         , 0.0  );
		tessellator.addVertexWithUV(1-r2         , 1    ,edge   , (2d/16) + 5d/16 , 0.0  );
		tessellator.addVertexWithUV(1-r2         , 0    ,edge   , (2d/16) + 5d/16 , 1 );
		tessellator.addVertexWithUV(1-edge       , 0    ,r2     , (2d/16)         , 1  );
		tessellator.draw();					
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(1-r2    , 1    ,edge      , 0.0    , 0.0  );
		tessellator.addVertexWithUV(r2      , 1    ,edge      , 2d/16      , 0.0  );
		tessellator.addVertexWithUV(r2      , 0    ,edge      , 2d/16      , 1 );
		tessellator.addVertexWithUV(1-r2    , 0    ,edge      , 0.0    , 1  );
		tessellator.draw();
		
		tessellator.startDrawing(9);
		tessellator.addVertexWithUV(r2      , 1    ,edge      , (2d/16)         , 0.0  );
		tessellator.addVertexWithUV(edge    , 1    ,r2        , (2d/16) + 5d/16 , 0.0  );
		tessellator.addVertexWithUV(edge    , 0    ,r2        , (2d/16) + 5d/16 , 1 );
		tessellator.addVertexWithUV(r2      , 0    ,edge      , (2d/16)         , 1  );
		tessellator.draw();
	// Lens body sides - END
		
		bindTextureByName("/jbastromodtex/jbLensNormalBodyTop.png");
		
		// Lens body top
		tessellator.startDrawing(9);			
		tessellator.addVertexWithUV(edge         , 1    ,r2          , 1-r2   , edge );
		tessellator.addVertexWithUV(edge         , 1    ,1-r2        , r2     , edge );
		tessellator.addVertexWithUV(r2           , 1    ,1-edge      , edge   , r2   );
		tessellator.addVertexWithUV(1-r2         , 1    ,1-edge      , edge   , 1-r2 );
		tessellator.addVertexWithUV(1-edge       , 1    ,1-r2        , r2     , 1-edge );
		tessellator.addVertexWithUV(1-edge       , 1    ,r2          , 1-r2   , 1-edge );
		tessellator.addVertexWithUV(1-r2         , 1    ,edge        , 1-edge , 1-r2   );
		tessellator.addVertexWithUV(r2           , 1    ,edge        , 1-edge , r2  );			
		tessellator.draw();
		
		
		if (location == 0) {
			if (state == 1)
				bindTextureByName("/jbastromodtex/jbLensNormalBodyBottom.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNormalBodyBottomOff.png");
		}
		else {
			if (state == 1)
				bindTextureByName("/jbastromodtex/jbLensNetherBodyBottom.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBodyBottomOff.png");
		}
		
		// Lens body bottom
		tessellator.startDrawing(9);			
		tessellator.addVertexWithUV(edge         , 0    ,r2          , 1-r2   , edge );
		tessellator.addVertexWithUV(edge         , 0    ,1-r2        , r2     , edge   );
		tessellator.addVertexWithUV(r2           , 0    ,1-edge      , edge   , r2    );
		tessellator.addVertexWithUV(1-r2         , 0    ,1-edge      , edge   , 1-r2    );
		tessellator.addVertexWithUV(1-edge       , 0    ,1-r2        , r2     , 1-edge   );
		tessellator.addVertexWithUV(1-edge       , 0    ,r2          , 1-r2   , 1-edge );
		tessellator.addVertexWithUV(1-r2         , 0    ,edge        , 1-edge , 1-r2    );
		tessellator.addVertexWithUV(r2           , 0    ,edge        , 1-edge , r2    );			
		tessellator.draw();
	}
	
	// Location: Nether=-1, Overworld=0
	// l: Number of lenses
	private void drawLensProtuberance(Tessellator tessellator, int l, int location) {
		double r2 = 5d / 16d;

		if (l == 1) {
			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeSides.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeSides.png");

			// Lens protuberance
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 4d / 16, r2, 0.0, 0.0);
			tessellator.addVertexWithUV(0, 4d / 16, 1 - r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(0, 2d / 16, 1 - r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(0, 2d / 16, r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 4d / 16, 1 - r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(r2, 4d / 16, 1, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(r2, 2d / 16, 1, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(0, 2d / 16, 1 - r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 4d / 16, 1, 0.0, 0.0);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 1, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 1, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(r2, 2d / 16, 1, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 1, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1, 4d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1, 2d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 1, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 4d / 16, 1 - r2, 0.0, 0.0);
			tessellator.addVertexWithUV(1, 4d / 16, r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1, 2d / 16, r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1, 2d / 16, 1 - r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 4d / 16, r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 0, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 0, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1, 2d / 16, r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 0, 0.0, 0.0);
			tessellator.addVertexWithUV(r2, 4d / 16, 0, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(r2, 2d / 16, 0, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 0, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 4d / 16, 0, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(0, 4d / 16, r2, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(0, 2d / 16, r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(r2, 2d / 16, 0, (1 - 2 * r2), 2d / 16);
			tessellator.draw();
			// Lens protuberance - END

			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeTop.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeTop.png");
			
			// Lens protuberance top
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 4d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 4d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 4d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 4d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 4d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 4d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 4d / 16, 0, 1, r2);
			tessellator.draw();

			// Lens protuberance bottom
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 2d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 2d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 2d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 2d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 2d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 2d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 2d / 16, 0, 1, r2);
			tessellator.draw();
		} else if (l == 2) {
			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeSides.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeSides.png");

			// Lens protuberance
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 8d / 16, r2, 0.0, 0.0);
			tessellator.addVertexWithUV(0, 8d / 16, 1 - r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(0, 6d / 16, 1 - r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(0, 6d / 16, r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 8d / 16, 1 - r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(r2, 8d / 16, 1, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(r2, 6d / 16, 1, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(0, 6d / 16, 1 - r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 8d / 16, 1, 0.0, 0.0);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 1, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 1, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(r2, 6d / 16, 1, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 1, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1, 8d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1, 6d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 1, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 8d / 16, 1 - r2, 0.0, 0.0);
			tessellator.addVertexWithUV(1, 8d / 16, r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1, 6d / 16, r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1, 6d / 16, 1 - r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 8d / 16, r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 0, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 0, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1, 6d / 16, r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 0, 0.0, 0.0);
			tessellator.addVertexWithUV(r2, 8d / 16, 0, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(r2, 6d / 16, 0, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 0, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 8d / 16, 0, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(0, 8d / 16, r2, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(0, 6d / 16, r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(r2, 6d / 16, 0, (1 - 2 * r2), 2d / 16);
			tessellator.draw();
			// Lens protuberance - END

			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeTop.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeTop.png");

			// Lens protuberance top
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 8d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 8d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 8d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 8d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 8d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 8d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 8d / 16, 0, 1, r2);
			tessellator.draw();

			// Lens protuberance bottom
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 6d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 6d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 6d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 6d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 6d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 6d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 6d / 16, 0, 1, r2);
			tessellator.draw();
		} else if (l == 3) {
			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeSides.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeSides.png");

			// Lens protuberance
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 12d / 16, r2, 0.0, 0.0);
			tessellator.addVertexWithUV(0, 12d / 16, 1 - r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(0, 10d / 16, 1 - r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(0, 10d / 16, r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 12d / 16, 1 - r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(r2, 12d / 16, 1, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(r2, 10d / 16, 1, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(0, 10d / 16, 1 - r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 12d / 16, 1, 0.0, 0.0);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 1, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 1, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(r2, 10d / 16, 1, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 1, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1, 12d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1, 10d / 16, 1 - r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 1, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 12d / 16, 1 - r2, 0.0, 0.0);
			tessellator.addVertexWithUV(1, 12d / 16, r2, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(1, 10d / 16, r2, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1, 10d / 16, 1 - r2, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1, 12d / 16, r2, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 0, (1 - 2 * r2) + 7d / 16,
					0.0);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 0, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(1, 10d / 16, r2, (1 - 2 * r2), 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 0, 0.0, 0.0);
			tessellator.addVertexWithUV(r2, 12d / 16, 0, 1 - 2 * r2, 0.0);
			tessellator.addVertexWithUV(r2, 10d / 16, 0, 1 - 2 * r2, 2d / 16);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 0, 0.0, 2d / 16);
			tessellator.draw();

			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(r2, 12d / 16, 0, (1 - 2 * r2), 0.0);
			tessellator.addVertexWithUV(0, 12d / 16, r2, (1 - 2 * r2) + 7d / 16, 0.0);
			tessellator.addVertexWithUV(0, 10d / 16, r2, (1 - 2 * r2) + 7d / 16,
					2d / 16);
			tessellator.addVertexWithUV(r2, 10d / 16, 0, (1 - 2 * r2), 2d / 16);
			tessellator.draw();
			// Lens protuberance - END

			if (location == 0)
				bindTextureByName("/jbastromodtex/jbLensNormalBulgeTop.png");
			else
				bindTextureByName("/jbastromodtex/jbLensNetherBulgeTop.png");

			// Lens protuberance top
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 12d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 12d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 12d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 12d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 12d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 12d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 12d / 16, 0, 1, r2);
			tessellator.draw();

			// Lens protuberance bottom
			tessellator.startDrawing(9);
			tessellator.addVertexWithUV(0, 10d / 16, r2, 1 - r2, 0.0);
			tessellator.addVertexWithUV(0, 10d / 16, 1 - r2, r2, 0.0);
			tessellator.addVertexWithUV(r2, 10d / 16, 1, 0, r2);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 1, 0, 1 - r2);
			tessellator.addVertexWithUV(1, 10d / 16, 1 - r2, r2, 1);
			tessellator.addVertexWithUV(1, 10d / 16, r2, 1 - r2, 1);
			tessellator.addVertexWithUV(1 - r2, 10d / 16, 0, 1, 1 - r2);
			tessellator.addVertexWithUV(r2, 10d / 16, 0, 1, r2);
			tessellator.draw();
		}
	}
}