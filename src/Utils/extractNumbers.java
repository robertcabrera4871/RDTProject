package Utils;

public class extractNumbers {
    public static int extract(String toExtract){
        String[] stringArray = toExtract.split("");
        StringBuffer stringBuf = new StringBuffer();
        for(int i = 0; i < toExtract.length() -1; i++){
            if(stringArray[i].matches("[0-9]+")){
                stringBuf.append(stringArray[i]);
            } else{
                break;
            }
        }
        return Integer.parseInt(stringBuf.toString());
    }
}
