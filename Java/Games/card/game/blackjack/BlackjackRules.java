package card.game.blackjack ;

import java.util.logging.Logger ;

public class BlackjackRules
{
    private static Logger logger = Logger.getLogger(BlackjackRules.class.getName()) ;

    private int     numberOfDecks_       ;
    private boolean splitAces_           ;
    private boolean hitOnSoft17_         ;
    private boolean allowEarlySurrender_ ;
    private boolean allowLateSurrender_  ;
    private boolean resplitToNN_         ;
    private boolean resplitAces_         ;
    private boolean doubleAfterSplit_    ;
    private boolean doubleOn10Or11Only_  ;
    private boolean insurance_           ;

    public BlackjackRules( int     numberOfDecks      ,
                           boolean splitAces          ,
                           boolean hitOnSoft17        ,
                           boolean allowEarlySurrender,
                           boolean allowLateSurrender ,
                           boolean resplitToNN        ,
                           boolean resplitAces        ,
                           boolean doubleAfterSplit   ,
                           boolean doubleOn10Or11Only ,
                           boolean insurance          )
    {
        numberOfDecks_       = numberOfDecks       ;
        splitAces_           = splitAces           ;
        hitOnSoft17_         = hitOnSoft17         ;
        allowEarlySurrender_ = allowEarlySurrender ;
        allowLateSurrender_  = allowLateSurrender  ;
        resplitToNN_         = resplitToNN         ;
        resplitAces_         = resplitAces         ;
        doubleAfterSplit_    = doubleAfterSplit    ;
        doubleOn10Or11Only_  = doubleOn10Or11Only  ;
        insurance_           = insurance           ;
    }

    public int     getNumberOfDecks()       { return numberOfDecks_       ; }
    public boolean getCanSplitAces()        { return splitAces_           ; }
    public boolean getDealerHitSoft17()     { return hitOnSoft17_         ; }
    public boolean getAllowEarlySurrender() { return allowEarlySurrender_ ; }
    public boolean getAllowLateSurrender()  { return allowLateSurrender_  ; }
    public boolean getCanResplitPairs()     { return resplitToNN_         ; }
    public boolean getCanResplitAces()      { return resplitAces_         ; }
    public boolean getCanDoubleAfterSplit (){ return doubleAfterSplit_    ; }
    public boolean getOnly10or11Double()    { return doubleOn10Or11Only_  ; }
    public boolean getOfferInsurance()      { return insurance_           ; }
}
