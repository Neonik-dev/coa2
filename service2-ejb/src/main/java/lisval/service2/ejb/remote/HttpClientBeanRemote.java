package lisval.service2.ejb.remote;

import java.security.NoSuchAlgorithmException;

public interface HttpClientBeanRemote {
    String get(String url) throws NoSuchAlgorithmException;
    String post(String url, String jsonBody) throws NoSuchAlgorithmException;
    String put(String url, String jsonBody);
    boolean delete(String url) throws NoSuchAlgorithmException;
}
