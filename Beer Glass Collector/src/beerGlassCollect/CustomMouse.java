package beerGlassCollect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.osbot.rs07.event.interaction.MouseMoveProfile;
import org.osbot.rs07.event.interaction.MouseMoveProfile.FlowVariety;
import org.osbot.rs07.script.MethodProvider;

public class CustomMouse extends MethodProvider {

	private String filePath;
	private File file;

	private final String fileInfo = "#Custom mouse movement values for Fury Scripts\r\n"
			+ "#These are currently set to the default values.\r\n"
			+ "#Experiment to find a mouse that you like and is unique!\r\n" + "#\r\n"
			+ "# Deviation - Sets the deviation factor. This is used to move the mouse away from a straight line to the target\r\n"
			+ "Deviation:7\r\n" + "#\r\n"
			+ "# Noise - Sets the noise factor. This is used to simulate shakiness in the movement\r\n"
			+ "Noise:2.5\r\n" + "#\r\n"
			+ "# Overshoots - Sets the max amount of overshoots before reaching the target\r\n" + "Overshoots:2\r\n"
			+ "#\r\n"
			+ "# Min Overshoot Time - Sets the minimum amount of time (in ms) the mouse needs to move before an overshoot can occur\r\n"
			+ "Min Overshoot Time:375\r\n" + "#\r\n"
			+ "# Min Overshoot Distance - Sets the minimum distance (in pixels) the mouse needs to move before an overshoot can occur\r\n"
			+ "Min Overshoot Distance:25\r\n" + "#\r\n" + "# Speed Base Time - Sets the speed base time\r\n"
			+ "Speed Base Time:185\r\n" + "#\r\n" + "# Flow Speed Modifier - Sets the flow speed modifier.\r\n"
			+ "Flow Speed Modifier:1\r\n" + "#\r\n"
			+ "# Flow Variety - Sets the flow variety. This is used to set the amount of variety in the mouse movement flow\r\n"
			+ "# Allows 0 for none, 1 for medium, or 2 for high.\r\n" + "Flow Variety:1";

	public void getCustomMouse(String directoryData) throws IOException {
		filePath = directoryData + File.separator + "Fury Mouse.txt";
		file = new File(filePath);
		if (file.exists()) {
			makeCustomMouse();
		} else {
			makeFile();
		}
	}

	private void makeFile() throws IOException {
		if (file.createNewFile()) {
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(file);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				bw.write(fileInfo);
				bw.close();
			} catch (FileNotFoundException e) {
				log(e);
			}

		}
	}

	private void makeCustomMouse() {
		MouseMoveProfile customMouse = new MouseMoveProfile();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				if (line.contains(":")) {
					String toSet = line.split(":")[0].toLowerCase();
					String value = line.split(":")[1];
					switch (toSet) {
					case "deviation":
						customMouse.setDeviation(Integer.parseInt(value));
						break;
					case "noise":
						customMouse.setNoise(Double.parseDouble(value));
						break;
					case "overshoots":
						customMouse.setOvershoots(Integer.parseInt(value));
						break;
					case "min overshoot time":
						customMouse.setMinOvershootTime(Long.parseLong(value));
						break;
					case "min overshoot distance":
						customMouse.setMinOvershootDistance(Integer.parseInt(value));
						break;
					case "speed base time":
						customMouse.setSpeedBaseTime(Integer.parseInt(value));
						break;
					case "flow speed modifier":
						customMouse.setFlowSpeedModifier(Double.parseDouble(value));
						break;
					case "flow variety":
						customMouse.setFlowVariety(FlowVariety.values()[Integer.parseInt(value)]);
						break;
					default:
						log("File error");
						break;
					}
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			log(e);
		}
		getBot().setMouseMoveProfile(customMouse);
	}

}
