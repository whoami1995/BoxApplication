package doos;

public class Box<T extends Package> implements Comparable<Box<? extends Package>> {

	@Override
	public String toString() {
		return "Doos [Height=" + height + ", Width=" + width + ", Color=" + color + ", Weight=" + weight + ", Danger="
				+ danger + "]";
	}

	private double height;
	private double width;
	private Color color;
	private double weight;
	private boolean danger;

	public Box(double height, double width, Color color, double weight, boolean danger) {
		this.setHeight(height);
		this.setWidth(width);
		this.setDanger(danger);
		this.setColor(color);
		this.setWeight(weight);
	}

	public Box() {

	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isDanger() {
		return danger;
	}

	public void setDanger(boolean danger) {
		this.danger = danger;
	}

	/*
	 * public int compareTo(Box<Package> o) { int comparage = (int)
	 * (this.getWeight()-o.getWeight()); return comparage; }
	 */

	@Override
	public int compareTo(Box<? extends Package> o) {
		if (this.weight > o.getWeight()) {
			return -1;
		} else {
			if (this.weight < o.getWeight()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}
