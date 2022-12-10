package dice.craps ;

import dice.* ;

// A class to wrap a craps roll
//
// 2 =  2.78% or 1/36 or 1-1
// 3 =  5.55% or 2/36 or 1-2,2-1
// 4 =  8.33% or 3/36 or 1-3,2-3,3-1
// 5 = 11.11% or 4/36 or 1-4,2-3,3-2,4-1
// 6 = 13.89% or 5/36 or 1-5,2-4,3-3,4-2,5-1
// 7 = 16.67% or 6/36 or 1-6,2-5,3-4,4-3,5-2,6-1
// 8 = 13.89% or 5/36 or 2-6,3-5,4-4,5-3,6-2
// 9 = 11.11% or 4/36 or 3-6,4-5,5-4,6-3
//10 =  8.33% or 3/36 or 4-6,5-5,6-4
//11 =  5.56% or 2/36 or 5-6,6-5
//12 =  2.78% or 1/36 or 6-6
public class CrapsRoll implements Comparable<CrapsRoll> {
    private Dice dice_ ;
    private Type type_ ;

    public CrapsRoll(Dice dice) {
        dice_ = dice.clone() ;
        type_ = rollType(dice_) ;
    }

    public enum Type {
        TWO,
        THREE,
        FOUR_EASY,
        FOUR_HARD,
        FIVE,
        SIX_EASY,
        SIX_HARD,
        SEVEN,
        EIGHT_EASY,
        EIGHT_HARD,
        NINE,
        TEN_EASY,
        TEN_HARD,
        ELEVEN,
        TWELVE,
        INVALID
    }

    public String toString() {
        StringBuilder sb = new StringBuilder() ;
        sb.append("CrapsRoll<").append(value()).append(",").append(type()).append(">") ;
        return sb.toString() ;
    }

    @Override
    public int hashCode() {
        return type_.ordinal() ;
    }

    @Override
    public int compareTo(CrapsRoll other){
        if(this.dice_.value() == other.dice_.value())
            return 0 ;
        else if(this.dice_.value() > other.dice_.value())
            return 1 ;
        else
            return -1 ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CrapsRoll))
            return false;
        CrapsRoll other = (CrapsRoll)o;
        return this.type_ == other.type_ && this.dice_.value() == other.dice_.value() ;
    }

    // 11.11% of the time
    public boolean isComeOutCraps() {
        return value() == 2 || value() == 3 || value() == 12 ;
    }

    // 22.22% of the time
    public boolean isComeOutNatural() {
        return value() == 7 || value() == 11 ;
    }

    // 66.66% of the time
    public boolean isPoint() {
        return !(isComeOutNatural() || isComeOutCraps()) ;
    }

    // 47.22% of the time
    public boolean isField() {
        return value() ==  2 || value() ==  3 || value() ==  4 ||
               value() ==  9 || value() == 10 || value() == 11 || value() == 12 ;
    }

    public boolean isHardway() {
        return isHardwayValue() && isPair() ;
    }

    public boolean isHardwayValue() {
        return value() == 4 || value() == 6 || value() == 8 || value() == 10 ;
    }

    public boolean isPair() {
        return dice_.value(0) == dice_.value(1) ;
    }

    public Integer value() { return dice_.value() ; }
    public Type type() { return type_ ; }

    public static Type rollType(Dice dice) {
        Type type = Type.TWO ;
        switch(dice.value()) {
        case 2:
            type = Type.TWO ;
            break ;
        case 3:
            type = Type.THREE ;
            break ;
        case 4:
            if(dice.value(0) == 2)
                type = Type.FOUR_HARD ;
            else
                type = Type.FOUR_EASY ;
            break ;
        case 5:
            type = Type.FIVE ;
            break ;
        case 6:
            if(dice.value(0) == 3)
                type = Type.SIX_HARD ;
            else
                type = Type.SIX_EASY ;
            break ;
        case 7:
            type = Type.SEVEN ;
            break ;
        case 8:
            if(dice.value(0) == 4)
                type = Type.EIGHT_HARD ;
            else
                type = Type.EIGHT_EASY ;
            break ;
        case 9:
            type = Type.NINE ;
            break ;
        case 10:
            if(dice.value(0) == 5)
                type = Type.TEN_HARD ;
            else
                type = Type.TEN_EASY ;
            break ;
        case 11:
            type = Type.ELEVEN ;
            break ;
        case 12:
            type = Type.TWELVE ;
            break ;
        }
        return type ;
    }
} ;
