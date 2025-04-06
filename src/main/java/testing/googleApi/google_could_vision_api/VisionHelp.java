package testing.googleApi.google_could_vision_api;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public abstract class VisionHelp {

	public void analysisPicture(String inputDir, String outputDir) throws Exception {
		ImageAnnotatorClient vision = ImageAnnotatorClient.create(); // 建立Google client端
		List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>(); // 建立一個一個ArrayList 存放要讀取的images

		// 讀取資料夾，取得所有圖片的路徑
		File input = new File(inputDir);
		String[] inputFileNames = input.list();
		for (String inputName : inputFileNames) {
			String fileName = inputDir + "/" + inputName;

			Path path = Paths.get(fileName);
			byte[] data = Files.readAllBytes(path); // 將img檔轉成byte陣列
			ByteString imgBytes = ByteString.copyFrom(data);

			Image img = Image.newBuilder().setContent(imgBytes).build(); // 用imgBytes 建立一個Image物件
			Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build(); // 這裡是建立一個有標籤敘述及比對的分數

			AnnotateImageRequest request = AnnotateImageRequest.newBuilder() // 建立一個AnnotateImageRequest
					.addFeatures(feat) // 要回傳的功能
					.setImage(img) // 要分析的img檔
					.build();

			requests.add(request); // 將AnnotateImageRequest傳入到requests的ArrayList 中

		}

		List<List<String[]>> dataList = new ArrayList<>(); // 存取所有的回傳的資料

		BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests); // 向google api發送請求
		List<AnnotateImageResponse> responses = response.getResponsesList(); // 取得response (google api回傳的資料)

		// 讀取responses的資料如有錯誤回傳提示並跳出forEach
		for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
				System.out.println(res.getError().getMessage());
				return;
			}

			List<String[]> datas = new ArrayList<>(); // 這個ArrayList是用來存放每一張img的資料

			for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
				String[] data = new String[2]; // 建立一個長度為2的字串陣列
				data[0] = annotation.getDescription(); // 第一個放label
				data[1] = String.valueOf(annotation.getScore()); // 第二個放label的對應分數

				datas.add(data); // 將資料加到datas

			}
			dataList.add(datas); // 將每張img的資料加到dataList
		}

		writeData(outputDir, inputFileNames, dataList);

	}

	public abstract void writeData(String outdir, String[] inputFileNames, List<List<String[]>> dataList);

}
