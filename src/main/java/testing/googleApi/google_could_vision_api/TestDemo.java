package testing.googleApi.google_could_vision_api;

public class TestDemo {

	public static void main(String[] args) throws Exception {
		String input = "/Users/chrisxiao/eclipse-workspace/google-could-vision-api/src/img";
		String output = "/Users/chrisxiao/eclipse-workspace/google-could-vision-api/src/outputFile";

		VisionHelp help = new ExcelHelp();
		help.analysisPicture(input, output);
	}

}
