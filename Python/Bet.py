#!/usr/bin/env python
# coding: utf-8

# In[39]:


YW = 40     #Yankees Wins
AW = 40     #Astros Wins

YAW = 10     #Yankees Ace Wins (Cole)
AAW = 10    #Stros Ace Wins (Verlander)

YHR = 15    #Yankee HRs (Judge)
AHR = 15    #Stros HRs (Bregman)

#determine $100 winner
def Winner():
    if (YW-(YAW-AAW))-AW > 0:
        print ("Eric Wins with no tiebreaker")  
    elif (YW-(YAW-AAW))-AW < 0:
        print ("Matt Wins with no tiebreaker")
    elif YHR > AHR: 
        print ("Eric Wins with HR tiebreaker")
    elif YHR < AHR: 
        print ("Matt Wins with HR tiebreaker")
    else: 
        print("All tied up - No bet")


# In[40]:


print("Stats for the bet:        (Yankees are listed as positive, Astros as negative)")
print("Yankee Win Variance is " +str(YW-AW) + " (Yankees= " +str(YW)+ " Astros= " +str(AW)+ ")")
print("Yankee Ace Win Adjustment is " + str(-(YAW-AAW)) + " (Yankees= " +str(YAW)+ " Astros= " +str(AAW)+ ")")
print("Adjusted Variance is " +str((YW-AW)-(YAW-AAW)))
print("")
print("And the winner is:")
print(Winner())


# In[ ]:




