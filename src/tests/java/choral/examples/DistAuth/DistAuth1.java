package choral.examples.DistAuth;
import choral.examples.AuthResult.AuthResult1;
import choral.examples.DistAuthUtils.AuthToken;
import choral.examples.DistAuthUtils.Base64_Encoder;
import choral.examples.DistAuthUtils.Credentials;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.choral.runtime.Enum.EnumBoolean;
import org.choral.runtime.TLSChannel.TLSChannel1;
import org.choral.lang.Unit;
import org.choral.annotations.Choreography;

@Choreography( role = "Client", name = "DistAuth" )
public class DistAuth1 {
	private TLSChannel1 < Object > ch_Client_IP;

	public DistAuth1( TLSChannel1 < Object > ch_Client_IP, Unit ch_Service_IP ) {
		this( ch_Client_IP );
	}

	public DistAuth1( TLSChannel1 < Object > ch_Client_IP ) {
		this.ch_Client_IP = ch_Client_IP;
	}

	private String calcHash( String salt, String pwd ) {
		String salt_and_pwd;
		salt_and_pwd = salt + pwd;
		try {
			MessageDigest md;
			md = MessageDigest.getInstance( "SHA3-256" );
			return Base64_Encoder.encodeToString( md.digest( salt_and_pwd.getBytes( StandardCharsets.UTF_8 ) ) );
		}
		catch ( NoSuchAlgorithmException e ) {
			e.printStackTrace();
			return "Algorithm not found";
		}
	}

	public AuthResult1 authenticate( Credentials credentials ) {
		String salt;
		salt = ch_Client_IP.< String >com( ch_Client_IP.< String >com( credentials.username ) );
		ch_Client_IP.< String >com( calcHash( salt, credentials.password ) );
		{
			switch( ch_Client_IP.< EnumBoolean >select( Unit.id ) ){
				default -> {
					throw new RuntimeException( "Received unexpected label from select operation" );
				}
				case True -> {
					return new AuthResult1( ch_Client_IP.< AuthToken >com( Unit.id ), Unit.id );
				}
				case False -> {
					return new AuthResult1();
				}
			}
		}
	}

}
