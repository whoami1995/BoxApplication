package doos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.Collections;

public class BoxApplication {

	private static ArrayList<Box<?>> boxList = new ArrayList<>();
	private static ArrayList<Box<?>> yellowBox = new ArrayList<>();
	private static ArrayList<Box<?>> brownBox = new ArrayList<>();

	public static void main(String[] args) throws IOException, InterruptedException {

		BoxApplication box = new BoxApplication();

		Thread readThread = new Thread(new Runnable() {
			public void run() {
				BoxApplication.boxList = box.readFunction("Boxes.txt");
			}
		});

		Thread splitBoxThread = new Thread(new Runnable() {
			public void run() {
				box.splitBoxes(BoxApplication.boxList, BoxApplication.yellowBox, BoxApplication.brownBox);
			}
		});

		Thread sortYellow = new Thread(() -> Collections.sort(yellowBox));
		Thread sortBrown = new Thread(() -> Collections.sort(brownBox));
		Thread sortAll = new Thread(() -> Collections.sort(boxList));

		Thread writeBoxesToFileThread = new Thread(new Runnable() {
			public void run() {
				box.writeBoxesToFile(BoxApplication.yellowBox, "BoxYellow.txt");
				box.writeBoxesToFile(BoxApplication.brownBox, "BoxBrown.txt");
			}
		});

		Thread writeWeightThread = new Thread(new Runnable() {
			public void run() {
				box.writeMethode(BoxApplication.boxList, "Heavy.txt", 50, "Heavy");
				box.writeMethode(BoxApplication.boxList, "Light.txt", 50, "Light");
			}
		});

		Thread writeFilePropertiesThread = new Thread(new Runnable() {
			public void run() {
				box.printFileSpecs("Heavy.txt", "FileProperties.txt");
			}
		});

		readThread.start();
		readThread.join();
		splitBoxThread.start();
		splitBoxThread.join();
		sortYellow.start();
		sortBrown.start();
		sortAll.start();
		sortYellow.join();
		sortBrown.join();
		
		Thread flipBrown = new Thread(() -> Collections.reverse(brownBox));
		Thread flipYellow = new Thread(() -> Collections.reverse(yellowBox));
		Thread flipAll = new Thread(() -> Collections.reverse(boxList));
		flipYellow.start();
		flipBrown.start();
		flipAll.start();
		flipYellow.join();
		flipBrown.join();
		flipAll.join();
		writeBoxesToFileThread.start();
		sortAll.join();
		writeWeightThread.start();
		writeFilePropertiesThread.join();

		box.printYellowBoxesTenHeigthWidth(BoxApplication.yellowBox);
		box.printDangerBoxes(BoxApplication.boxList);
	}
		
	private void writeMethode(ArrayList<Box<? extends Package>> boxList, String file,int aantal, String functie)
	{
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
		{
			switch(functie)
			{
			case "Heavy":
				for(int i = boxList.size() - aantal; i<boxList.size();i++)
				{
					writer.write(boxList.get(i).toString());
					writer.newLine();
				}
				break;
			case "Light":
				for (int i = 0; i < aantal; i++) {
					writer.write(boxList.get(i).toString());
					writer.newLine();
				}	
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private void writeBoxesToFile(ArrayList<Box<? extends Package>> boxList, String file) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (Box<? extends Package> d : boxList) {
				writer.write(d.toString());
				writer.newLine();
			}
		} 
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	private void splitBoxes(ArrayList<Box<?>> boxesList, ArrayList<Box<?>> yellowBoxList,
			ArrayList<Box<?>> brownBoxList) {
		for (Box<?> d : boxesList) {
			if (d.getColor() == Color.YELLOW) {
				yellowBoxList.add(d);
			} else {
				brownBoxList.add(d);
			}
		}
	}
	
	private <T extends Package> Box<T> makeBox(String[] array, Color color) {
        return new Box<T>(Double.parseDouble(array[1]), Double.parseDouble(array[2]),
                color, Double.parseDouble(array[4]), Boolean.parseBoolean(array[5]));
    }
	
	 private Box<? extends Package> boxFunction(String line, Color color) {
	        String[] array = line.split(";");
	        switch (array[0]) {
	        case "Wood":
	            return this.<Wood>makeBox(array, color);
	        case "Metal":
	            return this.<Metal>makeBox(array, color);
	        case "Plastic":
	            return this.<Plastic>makeBox(array, color);
	        case "Paper":
	            return this.<Paper>makeBox(array, color);
	        }
	        throw new IllegalArgumentException("Invalid Packaging: " + array[0]);
	    }


	private ArrayList<Box<? extends Package>> readFunction(String bestand) {
		ArrayList<Box<? extends Package>> boxesList = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(bestand))) {
			String[] boxArray = new String[6];
			String line = reader.readLine();
			while(line != null)
			{
				boxArray = line.split(";");
				if (boxArray[3].equals("Yellow")) {
					boxesList.add(boxFunction(line, Color.YELLOW));
				} else {
					boxesList.add(boxFunction(line, Color.BROWN));
				}
				line = reader.readLine();
			}
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		catch (InvalidPathException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return boxesList;
	}

	private void printFileSpecs(String fileNameToGetPath, String fileNameToWrite) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileNameToWrite))) {
			Path p1 = Paths.get(fileNameToGetPath);
			DosFileAttributes attr = Files.readAttributes(p1, DosFileAttributes.class);
			writer.write("Hidden: " + attr.isHidden());
			writer.newLine();
			writer.write("Size: " + attr.size());
			writer.newLine();
			writer.write("Create date: " + attr.creationTime());
			writer.newLine();
			writer.write("Readonly: " + attr.isReadOnly());
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} catch (InvalidPathException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} catch (IOException ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(ex.getMessage());
		}
	}

	private void printYellowBoxesTenHeigthWidth(ArrayList<Box<? extends Package>> yboxList) {
		yboxList.stream().filter(s -> s.getWidth() == 10.0).filter(s -> s.getHeight() == 10.0)
				.forEach(System.out::println);
	}

	private void printDangerBoxes(ArrayList<Box<? extends Package>> dangerBoxes) {
		dangerBoxes.stream().filter(s -> s.isDanger() == true).forEach(System.out::println);
	}
}
