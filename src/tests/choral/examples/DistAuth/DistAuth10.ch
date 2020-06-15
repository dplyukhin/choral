package choral.examples.DistAuth10;

import choral.examples.AuthResult.AuthResult1;
import choral.examples.AuthResult.AuthResult_B;
import choral.examples.DistAuthUtils.AuthToken;
import choral.examples.DistAuthUtils.Base64_Encoder;
import choral.examples.DistAuthUtils.ClientRegistry;
import choral.examples.DistAuthUtils.Credentials;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.choral.runtime.Enum.EnumBoolean;
import org.choral.runtime.TLSChannel.TLSChannel1;
import org.choral.runtime.TLSChannel.TLSChannel2;

public class DistAuth10@( Client, Service, S1, S2, S3, S4, S5, S6, S7, IP ){
	private TLSChannel@( Client, IP )< Object > ch_Client_IP;
	private TLSChannel@( Service, IP )< Object > ch_Service_IP;
	private TLSChannel@( S1, IP )< Object > ch_s1;
	private TLSChannel@( S2, IP )< Object > ch_s2;
	private TLSChannel@( S3, IP )< Object > ch_s3;
	private TLSChannel@( S4, IP )< Object > ch_s4;
	private TLSChannel@( S5, IP )< Object > ch_s5;
	private TLSChannel@( S6, IP )< Object > ch_s6;
	private TLSChannel@( S7, IP )< Object > ch_s7;

	public DistAuth10(
		TLSChannel@( Client, IP )< Object > ch_Client_IP,
		TLSChannel@( Service, IP )< Object > ch_Service_IP,
		TLSChannel@( S1, IP )< Object > ch_s1,
		TLSChannel@( S2, IP )< Object > ch_s2,
		TLSChannel@( S3, IP )< Object > ch_s3,
		TLSChannel@( S4, IP )< Object > ch_s4,
		TLSChannel@( S5, IP )< Object > ch_s5,
		TLSChannel@( S6, IP )< Object > ch_s6,
		TLSChannel@( S7, IP )< Object > ch_s7
	){
		this.ch_Client_IP = ch_Client_IP;
		this.ch_Service_IP = ch_Service_IP;
		this.ch_s1 = ch_s1;
		this.ch_s2 = ch_s2;
		this.ch_s3 = ch_s3;
		this.ch_s4 = ch_s4;
		this.ch_s5 = ch_s5;
		this.ch_s6 = ch_s6;
		this.ch_s7 = ch_s7;
	}

	private String@Client calcHash( String@Client salt, String@Client pwd ){
        String@Client salt_and_pwd = salt + pwd;
		try {
			MessageDigest@Client md = MessageDigest@Client.getInstance( "SHA3-256"@Client );
			return salt_and_pwd.getBytes( StandardCharsets@Client.UTF_8 )
			>> md::digest
			>> Base64_Encoder@Client::encodeToString;
		} catch ( NoSuchAlgorithmException@Client e ) {
			e.printStackTrace();
			return "Algorithm not found"@Client;
		}
	}

	public AuthResult@( Client, Service ) authenticate( Credentials@Client credentials ) {
		String@Client salt = credentials.username
			>> ch_Client_IP::< String >com
			>> ClientRegistry@IP::getSalt
			>> ch_Client_IP::< String >com;
		Boolean@IP valid = calcHash( salt, credentials.password )
			>> ch_Client_IP::< String >com
			>> ClientRegistry@IP::check;
		if( valid ){
			select( EnumBoolean@IP.True, ch_Client_IP );
			select( EnumBoolean@IP.True, ch_Service_IP );
			select( EnumBoolean@IP.True, ch_s1 );
			select( EnumBoolean@IP.True, ch_s2 );
			select( EnumBoolean@IP.True, ch_s3 );
			select( EnumBoolean@IP.True, ch_s4 );
			select( EnumBoolean@IP.True, ch_s5 );
			select( EnumBoolean@IP.True, ch_s6 );
			select( EnumBoolean@IP.True, ch_s7 );
			AuthToken@IP t = AuthToken@IP.create();
			return new AuthResult@( Client, Service )(
				ch_Client_IP.< AuthToken >com( t ),
				ch_Service_IP.< AuthToken >com( t )
			);
		} else {
			select( EnumBoolean@IP.False, ch_Client_IP );
			select( EnumBoolean@IP.False, ch_Service_IP );
			select( EnumBoolean@IP.False, ch_s1 );
			select( EnumBoolean@IP.False, ch_s2 );
			select( EnumBoolean@IP.False, ch_s3 );
			select( EnumBoolean@IP.False, ch_s4 );
			select( EnumBoolean@IP.False, ch_s5 );
			select( EnumBoolean@IP.False, ch_s6 );
			select( EnumBoolean@IP.False, ch_s7 );
			return new AuthResult@( Client, Service )();
		}
	}
}
