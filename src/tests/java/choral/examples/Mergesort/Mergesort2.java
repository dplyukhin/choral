package choral.examples.Mergesort;
import org.choral.lang.Channels.SymChannel1;
import org.choral.lang.Channels.SymChannel2;

import java.util.List;
import org.choral.lang.Unit;
import org.choral.annotations.Choreography;

@Choreography( role = "B", name = "Mergesort" )
public class Mergesort2 {
	SymChannel2 < Object > ch_AB;
	SymChannel1 < Object > ch_BC;

	public Mergesort2( SymChannel2 < Object > ch_AB, SymChannel1 < Object > ch_BC, Unit ch_CA ) {
		this( ch_AB, ch_BC );
	}

	public Mergesort2( SymChannel2 < Object > ch_AB, SymChannel1 < Object > ch_BC ) {
		this.ch_AB = ch_AB;
		this.ch_BC = ch_BC;
	}

	public Unit sort( Unit a ) {
		return sort();
	}

	private Unit merge( List < Integer > lhs, Unit rhs ) {
		if( lhs.size() > 0 ){
			ch_AB.< MChoice >select( MChoice.L );
			ch_BC.< MChoice >select( MChoice.L );
			{
				switch( ch_BC.< MChoice >select( Unit.id ) ){
					case R -> {
						return ch_AB.< List < Integer > >com( lhs );
					}
					default -> {
						throw new RuntimeException( "Received unexpected label from select operation" );
					}
					case L -> {
						if( lhs.get( 0 ) <= ch_BC.< Integer >com( Unit.id ) ){
							ch_AB.< MChoice >select( MChoice.L );
							ch_BC.< MChoice >select( MChoice.L );
							ch_AB.< Integer >com( lhs.get( 0 ) );
							merge( lhs.subList( 1, lhs.size() ), Unit.id );
							return Unit.id;
						} else {
							ch_AB.< MChoice >select( MChoice.R );
							ch_BC.< MChoice >select( MChoice.R );
							merge( lhs, Unit.id );
							return Unit.id;
						}
					}
				}
			}
		} else {
			ch_AB.< MChoice >select( MChoice.R );
			ch_BC.< MChoice >select( MChoice.R );
			return Unit.id;
		}
	}

	public Unit sort() {
		{
			switch( ch_AB.< MChoice >select( Unit.id ) ){
				case R -> {
					return Unit.id;
				}
				default -> {
					throw new RuntimeException( "Received unexpected label from select operation" );
				}
				case L -> {
					Mergesort1 mb;
					mb = new Mergesort1( ch_BC, Unit.id, ch_AB );
					Mergesort3 mc;
					mc = new Mergesort3( Unit.id, ch_AB, ch_BC );
					List < Integer > lhs;
					lhs = mb.sort( ch_AB.< List < Integer > >com( Unit.id ) );
					mc.sort( Unit.id );
					return merge( lhs, Unit.id );
				}
			}
		}
	}

}
