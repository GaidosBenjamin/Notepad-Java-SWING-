import java.io.*;

public class IOFile {
    public String fileName;

    IOFile(String fName){
        this.fileName = fName;
    }

    public boolean createFile(String text){
        try{
            File myObj = new File(fileName);
            if(myObj.createNewFile()){
                FileWriter writer = new FileWriter(myObj);
                BufferedWriter bw = new BufferedWriter(writer);
                bw.write(text);
                bw.close();
                return true;
            }
            else{
                return false;
            }
        } catch(IOException ex){
            return false;
        }
    }

    public static String openFile(String filepath){
        try{
            FileReader fr = new FileReader(filepath);
            BufferedReader br = new BufferedReader(fr);
            String x, result = "";
            while((x = br.readLine()) != null){
                result += x + "\n";
            }
            br.close();
            return result;
        }catch (FileNotFoundException ex){
            return "";
        }catch (IOException ex){
            return "";
        }

    }
}
