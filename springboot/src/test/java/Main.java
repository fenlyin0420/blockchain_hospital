import com.example.entity.Doctor;
import com.example.utils.JwtSm.MySM2Util;

public class Main {
    public static void main(String[] args) {
        try{
            MySM2Util.getKey();
            String plainText = "我爱中国！";
            String cipherText = MySM2Util.encryption(MySM2Util.pub2str(MySM2Util.getBcPubKey()), plainText);
            System.out.println(cipherText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
