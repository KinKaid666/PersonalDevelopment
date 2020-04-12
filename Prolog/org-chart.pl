job( bob, ceo ).
job( jane, cio ).
job( john, coo ).
job( alice, cto ).
job( eric, cfo ).
job( rasputin, 'vp operations' ).
job( genghis, 'vp operations' ).
job( carol, secretary ).
job( phil, secretary ).
job( mordock, 'it lackey' ).
boss( bob, jane ).
boss( bob, john ).
boss( bob, alice ).
boss( bob, eric ).
boss( john, rasputin ).
boss( john, genghis ).
boss( john, mordock ).
boss( alice, mordock ).
boss( eric, phil ).
boss( rasputin, carol ).
higherlevel( BOSS, SUBORDINATE ) :-
    boss( BOSS, SUBORDINATE ).
higherlevel( BOSS, SUBORDINATE ) :-
    boss( ANYBOSS, SUBORDINATE ),
    higherlevel( BOSS, ANYBOSS ).
