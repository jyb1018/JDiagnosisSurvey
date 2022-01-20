import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Vector;

class SurveyController {
    private SurveyView view;
    private Vector<SurveyEntity> entities;
    private AES256 aes256;
    private File file = new File("./data.dat");

    SurveyController() {
        view = new SurveyView();
        entities = new Vector<>();
        aes256 = new AES256();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void start() {
        view.showMain();
    }



    // View 연동
    void entityUpdate() {

    }

    // File 연동
    void fileRead() throws Exception {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(isr);

        StringBuilder dataStr = new StringBuilder();
        String str;
        while((str = reader.readLine()) != null) {
            str = aes256.decrypt(str);
            dataStr.append(str);
        }
        String[] splits1 = dataStr.toString().split("\\$");
        for (String line : splits1) {
            String[] splits2 = line.split("\\^");
            String description = splits2[0];
            Vector<String> prescriptions = new Vector<>(Arrays.asList(splits2).subList(1, splits2.length));
            entities.add(new SurveyEntity(description, prescriptions));
        }

    }

    void fileWrite() throws Exception {
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        StringBuilder dataStr = new StringBuilder();
        for (SurveyEntity entity : entities) {
            dataStr.append(entity.toString());
        }
        bw.write(aes256.encrypt(dataStr.toString()));

    }

    // DB 연동
//    void insertDB() {
//
//    }
//
//    void deleteDB() {
//
//    }
//
//    void selectDB() {
//
//    }


}
