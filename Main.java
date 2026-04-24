import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String saltStr = Base64.getEncoder().encodeToString(salt);
        
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashed = md.digest("admin123".getBytes());
        String hashStr = Base64.getEncoder().encodeToString(hashed);
        
        System.out.println("HASH=" + saltStr + "$" + hashStr);
    }
}
