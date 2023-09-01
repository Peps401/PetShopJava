package entiteti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static Datoteke.ReadingFiles.logger;

public interface PasswordHashing {

    public static String doHash(String password){
        try {
            MessageDigest messageDigest =  MessageDigest.getInstance("MD5");

            messageDigest.update(password.getBytes());

            byte[] resultByteArray = messageDigest.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : resultByteArray) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean SamePassword(User user) {
        try {
            List<String> lines = Files.readAllLines(Path.of("dat/users.txt"));

            String hashedPass = PasswordHashing.doHash(user.getPass());

            for (int i = 0; i < lines.size(); i = i + 3) {
                if(user.getName().equals(lines.get(i + 1))) {
                    if (hashedPass.equals(lines.get(i + 2))) {
                        System.out.println("Isti pass");
                        logger.info("Same password");
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
}
