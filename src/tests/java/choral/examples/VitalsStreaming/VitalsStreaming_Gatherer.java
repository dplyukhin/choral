package choral.examples.VitalsStreaming;
import choral.examples.VitalsStreamingUtils.Vitals;
import org.choral.lang.Unit;
import org.choral.annotations.Choreography;
import java.util.function.Consumer;
import choral.examples.VitalsStreamingUtils.VitalsMsg;
import org.choral.channels.SymChannel_B;

@Choreography( role = "Gatherer", name = "VitalsStreaming" )
public class VitalsStreaming_Gatherer {
	private SymChannel_B < Object > ch;

	public VitalsStreaming_Gatherer( SymChannel_B < Object > ch, Unit sensor ) {
		this( ch );
	}
	
	public VitalsStreaming_Gatherer( SymChannel_B < Object > ch ) {
		this.ch = ch;
	}

	public void gather( Consumer < Vitals > consumer ) {
		{
			switch( ch.< StreamState >select( Unit.id ) ){
				default -> {
					throw new RuntimeException( "Received unexpected label from select operation" );
				}
				case OFF -> {
					
				}
				case ON -> {
					VitalsMsg msg;
					msg = ch.< VitalsMsg >com( Unit.id );
					Boolean checkSignature;
					checkSignature = VitalsStreamingHelper.checkSignature( msg.signature() );
					if( checkSignature ){
						consumer.accept( VitalsStreamingHelper.pseudonymise( msg.content() ) );
					}
					gather( consumer );
				}
			}
		}
	}

}
