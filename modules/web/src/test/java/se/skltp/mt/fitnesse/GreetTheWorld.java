package se.skltp.mt.fitnesse;

//import fitlibrary.DoFixture;


public class GreetTheWorld {

	private String name;
	private String text;
	
	
	
	public void execute() {
		text = "Hello " + name;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}
	
	public String text() {
		return text;
	}
}
