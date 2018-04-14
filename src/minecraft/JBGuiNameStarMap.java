package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;

public class JBGuiNameStarMap extends GuiScreen {
	/**
	 * This String is just a local copy of the characters allowed in text rendering of minecraft.
	 */
	private static final String allowedCharacters = ChatAllowedCharacters.allowedCharacters;

	/** The title string that is displayed in the top-center of the screen. */
	protected String screenTitle = "";

	private String currentName = "";
	private EntityPlayer ep;

	/** Counts the number of screen updates. */
	private int updateCounter;

	private float x, z;
	private int scale;

	/** "Done" button for the GUI. */
	private GuiButton doneBtn;

	public JBGuiNameStarMap(EntityPlayer ep, int scale, float x, float z) {
		this.screenTitle = "Type Map Name ("+scale+"x) :";
		this.x = x;
		this.z = z;
		this.scale = scale;
		this.ep = ep;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.buttonList.clear();
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, "Done"));
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);

		if (this.currentName.trim().length()==0)
			this.currentName = "Star Map ("+scale+"x)";
		else 
			this.currentName = this.currentName+" ("+scale+"x)";
		
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
			DataOutputStream var5 = new DataOutputStream(var4);
			var5.writeInt(this.currentName.length());
			var5.writeChars(this.currentName);
			var5.writeFloat(x);
			var5.writeFloat(z);
			Packet250CustomPayload packet = new Packet250CustomPayload("JB|CSM", var4.toByteArray());
			Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
		}
		catch (Exception var7) {
			var7.printStackTrace();
		}
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.updateCounter;
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.enabled) {
			if (par1GuiButton.id == 0) {
				this.mc.displayGuiScreen((GuiScreen)null);
			}
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char c, int key) {
		if (key == 28) {
			this.actionPerformed(this.doneBtn);
		}

		if (key == 14 && this.currentName.length() > 0) {
			this.currentName = this.currentName.substring(0, this.currentName.length() - 1);
		}

		if (allowedCharacters.indexOf(c) >= 0 && this.currentName.length() < 15) {
			this.currentName = this.currentName + c;
		}

		if (key == 1) {
			this.actionPerformed(this.doneBtn);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 40, 16777215);
		this.drawCenteredString(this.fontRenderer, this.currentName, this.width / 2, 50, 16777215);
		super.drawScreen(par1, par2, par3);
	}
}
