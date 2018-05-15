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

/**
 * 卡牌游戏工厂的实现类，实现了
 * @author 87663
 *
 */
public class CardGameFactoryImpl implements CardGameFactory{

	@Override
	public CardGame getCardGame() {
		return getCardGame(Mode.THREE_CARD_MODE);//默认返回一个三牌模式，困难难度的游戏
	}

	@Override
	public CardGame getCardGame(Difficulty d) {
		return getCardGame(d,Mode.THREE_CARD_MODE);//返回一个指定难度，三牌模式的游戏
	}

	@Override
	public CardGame getCardGame(Mode mode) {
		return getCardGame(Difficulty.HARD,mode);//返回一个困难难度，指定模式的游戏
	}

	@Override
	public CardGame getCardGame(Difficulty d, Mode mode) {
		
		Timer timer=new TimerImpl();//构造计时器
		
		PointCounter pointCounter=new PointCounterImpl();//构造计分器
		
		if(d==Difficulty.EASY){//如果是简单难度的游戏的话
			
			SingleStepNextCardAnalyzerInitializer c=new SingleStepNextCardAnalyzerInitializer();//构造一个简单的单步发牌器
			
			Dealer dealer = new DealerImpl(mode, c);//构造一个指定模式的发牌器
			
			Box box_1=new BoxImpl();//构造box
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);//构造牌堆
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
			
			SimpleSingleStepCardGameAnalyzerImpl cardGameAnalyzer=new SimpleSingleStepCardGameAnalyzerImpl(false);//构造一个单步分析器
			
			//构造一个牌局管理器
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
			
			c.init(cardManagement);//用牌局管理器初始化单步分析器
			
			cardManagement.init();//初始化牌局分析器
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);//返回一个简单游戏
			
		}else if(d==Difficulty.HARD){//如果是困难游戏
			
			CardInitializer c=new SolvableGameCardInitializer();//构造一个可解游戏发牌器
			
			Dealer dealer = new DealerImpl(mode, c);//构造一个指定模式的发牌器
	
			Box box_1=new BoxImpl();//构造box
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);//构造牌堆
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
			
			CardGameAnalyzer cardGameAnalyzer=new SingleStepCardGameAnalyzerImpl(false);//构造一个单步分析器
			
			//构造一个牌局管理器
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
	
			cardManagement.init();//初始化牌局分析器
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);//返回一个困难游戏
			
		}else{//如果是大师级游戏
			
			CardInitializer c=new SimpleCardInitializerImpl();//构造一个一个简单随机发牌器
			
			Dealer dealer = new DealerImpl(mode, c);//构造一个指定模式的发牌器
	
			Box box_1=new BoxImpl();//构造box
			Box box_2=new BoxImpl();
			Box box_3=new BoxImpl();
			Box box_4=new BoxImpl();
	
			CardHeap cardHeap_1=new CardHeapImpl(1,c,Components.CARD_HEAP_1);//构造牌堆
			CardHeap cardHeap_2=new CardHeapImpl(2,c,Components.CARD_HEAP_2);
			CardHeap cardHeap_3=new CardHeapImpl(3,c,Components.CARD_HEAP_3);
			CardHeap cardHeap_4=new CardHeapImpl(4,c,Components.CARD_HEAP_4);
			CardHeap cardHeap_5=new CardHeapImpl(5,c,Components.CARD_HEAP_5);
			CardHeap cardHeap_6=new CardHeapImpl(6,c,Components.CARD_HEAP_6);
			CardHeap cardHeap_7=new CardHeapImpl(7,c,Components.CARD_HEAP_7);
	
			CardGameAnalyzer cardGameAnalyzer=new SingleStepCardGameAnalyzerImpl(false);//构造一个单步分析器
	
			//构造一个牌局管理器
			CardManagementImpl cardManagement= new CardManagementImpl(dealer, box_1, box_2, box_3, box_4, cardHeap_1, cardHeap_2, cardHeap_3, cardHeap_4, cardHeap_5, cardHeap_6, cardHeap_7, pointCounter, cardGameAnalyzer);
			
			cardManagement.init();//初始化牌局分析器
			
			return new CardGameImpl(cardManagement,cardGameAnalyzer,pointCounter,timer);//返回一个大师游戏
		}
	}

}
