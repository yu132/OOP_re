package core_refactoring.impl;

import core_refactoring.Box;
import core_refactoring.CardGame;
import core_refactoring.CardGameAnalyzer;
import core_refactoring.CardGameFactory;
import core_refactoring.CardHeap;
import core_refactoring.CardInitializer;
import core_refactoring.Components;
import core_refactoring.Dealer;
import core_refactoring.Difficulty;
import core_refactoring.Mode;
import core_refactoring.PointCounter;
import core_refactoring.Timer;
import core_refactoring.impl.easy.SimpleSingleStepCardGameAnalyzerImpl;
import core_refactoring.impl.easy.SingleStepNextCardAnalyzerInitializer;
import core_refactoring.impl.hard.SolvableGameCardInitializer;
import core_refactoring.impl.master.SimpleCardInitializerImpl;

public class CardGameFactoryImpl implements CardGameFactory{

	@Override
	public CardGame getCardGame() {
		return getCardGame(Mode.THREE_CARD_MODE);
	}

	@Override
	public CardGame getCardGame(Difficulty d) {
		return getCardGame(d,Mode.THREE_CARD_MODE);
	}

	@Override
	public CardGame getCardGame(Mode mode) {
		return getCardGame(Difficulty.HARD,mode);
	}

	@Override
	public CardGame getCardGame(Difficulty d, Mode mode) {
		
		Timer timer=new TimerImpl();
		
		PointCounter pointCounter=new PointCounterImpl();
		
		if(d==Difficulty.EASY){
			SingleStepNextCardAnalyzerInitializer c=new SingleStepNextCardAnalyzerInitializer();
			
			Dealer dealer = new DealerImpl(mode, c);
			
			Box box_1=new BoxImpl();
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
			
			SimpleSingleStepCardGameAnalyzerImpl cardGameAnalyzer=new SimpleSingleStepCardGameAnalyzerImpl(false);//xiuxiazheli
	
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
			
			c.init(cardManagement);
			
			cardManagement.init();
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);
		}else if(d==Difficulty.HARD){
			CardInitializer c=new SolvableGameCardInitializer();
			
			Dealer dealer = new DealerImpl(mode, c);
	
			Box box_1=new BoxImpl();
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
			
			CardGameAnalyzer cardGameAnalyzer=new SingleStepCardGameAnalyzerImpl(false);
			
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
	
			cardManagement.init();
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);
		}else{//MASTER
			CardInitializer c=new SimpleCardInitializerImpl();
			
			Dealer dealer = new DealerImpl(mode, c);
	
			Box box_1=new BoxImpl();
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
	
			CardGameAnalyzer cardGameAnalyzer=new SingleStepCardGameAnalyzerImpl(true);
	
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
			
			cardManagement.init();
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);
		}
	}

}
