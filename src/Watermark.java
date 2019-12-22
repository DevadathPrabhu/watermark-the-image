/**
 * @author dprabhu
 *
 */

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class Watermark {

	private final static Logger logger = Logger.getLogger(Watermark.class.getName());

	public static void main(String[] args) throws Exception {

		String directoryPath = "C:\\Users\\dprabhu\\Desktop\\Blog Pics\\Fucked up";
		File dir = new File(directoryPath);
		String[] files = dir.list();
		int screwdUpImageCount = 0;
		ArrayList<String> screwdUpFileNames = new ArrayList<String>();
		
		for(String file : files) {
			
			logger.log(Level.INFO, "Watermarking : " + file);
			
			final BufferedImage image = ImageIO.read(new File(directoryPath + "\\" + file));

			Graphics2D g = (Graphics2D) image.getGraphics();

			setGraphicProperties(g);

			int waterMarkWidth = g.getFontMetrics().stringWidth("DPTravelLogs");

			HashMap<String, Integer> coordinates = getWaterMarkCoordinates(image.getHeight(), image.getWidth(),
					waterMarkWidth);

			g.drawString("DPTravelLogs", coordinates.get("X"), coordinates.get("Y"));
			g.dispose();

			ImageIO.write(image, "jpeg", new File(directoryPath + "\\watermarked" + file ));

			if(waterMarkWidth + coordinates.get("X") > image.getWidth()) {
				logger.log(Level.WARNING, "Image size screwed up watermarking! :(");
				screwdUpImageCount++;
				screwdUpFileNames.add(file);
			}
			else {
				logger.log(Level.INFO, "Watermark embedded successfully!");				
			}

			TimeUnit.SECONDS.sleep(1);
			System.out.println("");
			
		}

		logger.log(Level.INFO, "Processed all " + files.length + " images!");
		
		if(screwdUpImageCount > 0) {
			logger.log(Level.WARNING, "Number of screwd up images : " + screwdUpImageCount);
			for(String file : screwdUpFileNames) {
				logger.log(Level.WARNING, "Screwd up file : " + file);
				System.out.println("");
			}
		}
		
	}

	private static void setGraphicProperties(Graphics2D g) {

		Composite comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .8f);
		g.setComposite(comp);
		g.setPaint(new GradientPaint(0, 0, Color.LIGHT_GRAY, 20, 20, new Color(211, 211, 211, 0), true));
		g.setFont(new Font("Mistral", Font.BOLD, 50));
		g.setStroke(new BasicStroke(15));

	}

	private static HashMap<String, Integer> getWaterMarkCoordinates(int height, int width, int waterMarkWidth) {

		HashMap<String, Integer> coordinatesMap = new HashMap<String, Integer>();

		if (width > height) {

			populateCoordinatesMap(coordinatesMap, width, height, 0.65, 0.78);

		} else {

			populateCoordinatesMap(coordinatesMap, width, height, 0.60, 0.80);

		}

		return coordinatesMap;
	}

	private static void populateCoordinatesMap(HashMap<String, Integer> coordinatesMap, int width, int height,
			double widthMultiplier, double heightMultiplier) {

		double xCoordinate = width * widthMultiplier;
		coordinatesMap.put("X", (int) xCoordinate);
		double yCoordinate = height * heightMultiplier;
		coordinatesMap.put("Y", (int) yCoordinate);

	}

}
