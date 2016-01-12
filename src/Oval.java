import java.awt.Color;
public class Oval {
	int x,y,width,height;
	Color colour;

	public Oval(int x, int y, int width, int height, Color colour){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colour = colour;
	}

	public void updateX(int x){
		this.x = x;
	}

	public void updateY(int y){
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getColour() {
		return colour;
	}

}