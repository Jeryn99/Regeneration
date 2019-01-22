package me.fril.regeneration.debugger;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.mojang.authlib.GameProfile;

@SuppressWarnings("serial")
class PanelHeader extends JPanel {
	
	private final JLabel lblSkinHead, lblPlayerName;
	
	public PanelHeader(GameProfile gp) {
		{
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 72, 0, 0};
			gridBagLayout.rowHeights = new int[]{14, 17, 0};
			gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0};
			gridBagLayout.rowWeights = new double[]{0.0, 0.0};
			setLayout(gridBagLayout);
		}
		
		lblSkinHead = new JLabel();
		lblSkinHead.setIcon(getSkinIconFor(gp));
		{
			GridBagConstraints gbc_lblSkinHead = new GridBagConstraints();
			gbc_lblSkinHead.insets = new Insets(0, 0, 5, 5);
			gbc_lblSkinHead.gridx = 1;
			gbc_lblSkinHead.gridy = 0;
			add(lblSkinHead, gbc_lblSkinHead);
		}
		
		lblPlayerName = new JLabel(gp.getName(), SwingConstants.CENTER);
		lblPlayerName.setForeground(Color.DARK_GRAY);
		lblPlayerName.setFont(new Font(lblPlayerName.getFont().getName(), Font.ITALIC, 14));
		lblPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
		{
			GridBagConstraints gbc_lblPlayerName = new GridBagConstraints();
			gbc_lblPlayerName.insets = new Insets(0, 0, 0, 5);
			gbc_lblPlayerName.gridx = 1;
			gbc_lblPlayerName.gridy = 1;
			add(lblPlayerName, gbc_lblPlayerName);
		}
	}
	
	private Icon getSkinIconFor(GameProfile gp) {
		File skinCache = new File("mods/regeneration/skincache");
		if (!skinCache.exists())
			skinCache.mkdirs();
		
		File skinImage = new File(skinCache, gp.getId().toString() + ".png");
		if (!skinImage.exists()) {
			try (ReadableByteChannel readableByteChannel = Channels.newChannel(new URL("https://crafatar.com/renders/head/" + gp.getId().toString() + "?size=100").openStream());
					FileOutputStream fileOutputStream = new FileOutputStream(skinImage);
					FileChannel fileChannel = fileOutputStream.getChannel()) {
				fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			} catch (IOException e) {
				System.err.println("Could not retrieve skin for " + gp.getName());
				e.printStackTrace();
				return getPlaceHolderImage();
			}
		}
		
		try {
			return new ImageIcon(ImageIO.read(skinImage));
		} catch (IOException e) {
			System.err.println("Could not load skin for " + gp.getName());
			e.printStackTrace();
			return getPlaceHolderImage();
		}
	}
	
	private Icon getPlaceHolderImage() {
		BufferedImage img = new BufferedImage(160, 160, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, 160, 160);
		return new ImageIcon(img);
	}
	
}
