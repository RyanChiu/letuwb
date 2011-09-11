package weibo4android.http;

import weibo4android.WeiboException;

public class OAuthVerifier extends OAuthToken {
    private static final long serialVersionUID = -8344528374458826291L;
    private String verifier;
   
    OAuthVerifier(Response res) throws WeiboException {
        this(res.asString());
    }
 
    OAuthVerifier(String str) {
        super(str);
        String[] results = str.split(":");
        if(results.length >= 3) {
        verifier =results[2].substring(1, 7);
        } else {
        verifier = "";
        }
    }
 
    public OAuthVerifier(String token,String tokenSecret) {
        super(token, tokenSecret);
    }
 
    /**
     *
     * @return verifier
     * @since Weibo4android
     */
 
    public String getVerifier() {
       return verifier;
    }
}