bubblesort(L1,L2) :-
	append(X,[A,B | Y],L1),
	B < A,
	append(X,[B,A | Y],Z),
	bubblesort(Z,L2).
bubblesort(L,L) :- !.

fac(1,1) :- !.
fac(X,Y) :-
	X1 is X-1, 
	fac(X1,Res),
	Y is Res*X.
	
foo(A,B) :- B is A + 4.

rember(X, [], []).
rember(X, [X | Xs], NewBs) :- rember(X, Xs, NewBs).
rember(X, [C | Cs], [C | NewCs]) :- not(C is X), rember(X, Cs, NewCs).

list_length([], 0).
list_length([X | Rest], Y) :- list_length(Rest, Z), Y is 1 + Z.

puzzledata(H, W, Z) :- 
same(H, [house(norwegian, _, _, _, _), _, house(_, _, _, milk, _), _, _]), 
member(house(englishman, _, _, _, red), H),                                
member(house(spaniard, dog, _, _, _), H),                                  
member(house(_, _, _, coffee, green), H),                                 
member(house(ukrainian, _, _, tea, _), H),                                
iright(house(_, _, _, _, ivory), house(_, _, _, _, green), H),            
member(house(_, snails, winston, _, _), H),                               
member(house(_, _, kools, _, yellow), H),                                  
nextto(house(_, _, chesterfield, _, _), house(_, fox, _, _, _), H),        
nextto(house(_, _, kools, _, _), house(_, horse, _, _, _), H),             
member(house(_, _, luckystrike, orange-juice, _), H),                      
member(house(japanese, _, parliaments, _, _), H),                          
nextto(house(norwegian, _, _, _, _), house(_, _, _, _, blue), H),          
member(house(W, _, _, water, _), H),                                       
member(house(Z, zebra, _, _, _), H).                                       

member(X, [X|Y]).
member(X, [_|Y]) :- member(X, Y).

iright(LEFT, RIGHT, [LEFT, RIGHT|_]).
iright(LEFT, RIGHT, [_|REST]) :- iright(LEFT, RIGHT, REST).

same(X, X).

nextto(X,Y,[X,Y | REST]).
nextto(X,Y,[Y,X | REST]).
nextto(X,Y,[_| REST]) :- nextto(X,Y,REST).

