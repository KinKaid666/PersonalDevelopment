package tests ;

import java.util.* ;
import java.text.DecimalFormat ;
import java.math.RoundingMode ;

import card.Card ;
import card.game.blackjack.* ;
import card.deck.* ;
import card.* ;

public class CountableShoeTest {
    public static void main(String args[]) {
        Integer iterations = Integer.valueOf(100) ;
        if(args.length > 0) {
            String iterationsAsString = args[0] ;
            try {
                if(iterationsAsString.contains("_")) {
                    iterationsAsString = iterationsAsString.replace("_","") ;
                }
                iterations = Integer.parseInt(iterationsAsString) ;
            } catch(Exception e) {
                // do nothing
            }
        }
        Map<Integer,Integer> maxCountHistogram = new HashMap<Integer,Integer>() ;
        Map<Integer,Integer> minCountHistogram = new HashMap<Integer,Integer>() ;
        System.out.printf("\n*** CountableShoeTest %,d iterations ***\n", iterations) ;

        DecimalFormat df = new DecimalFormat("###") ;
        df.setRoundingMode(RoundingMode.HALF_UP) ;
        CountableShoe shoe = new CountableShoe(8) ;
        for(int i = 0 ; i < iterations ; i++) {
            if((i > 80) && i % (iterations / 80) == 0) {
                System.out.print(".") ;
            }
            Integer minCount = Integer.MAX_VALUE ;
            Integer maxCount = Integer.MIN_VALUE ;
            while(!shoe.reshuffleNeeded()) {
                Double roundedDouble = Double.valueOf(df.format(shoe.getTrueCount())) ;
                Integer count = roundedDouble.intValue() ;
                // force getting a card
                Card c = shoe.getNextCard() ;
                if(count > maxCount) {
                    maxCount = count ;
                }
                if(count < minCount) {
                    minCount = count ;
                }
            }
            maxCountHistogram.put(maxCount, maxCountHistogram.getOrDefault(maxCount, 0)+1) ;
            minCountHistogram.put(minCount, minCountHistogram.getOrDefault(minCount, 0)+1) ;
            shoe.reshuffle() ;
            //System.out.println("min = " + minCount + " max = " + maxCount) ;
        }
        System.out.println("\n") ;
        //System.out.println(minCountHistogram) ;
        //System.out.println(maxCountHistogram) ;
        {
            Set<Integer> counts = minCountHistogram.keySet() ;
            int min = Collections.min(counts) ;
            int max = Collections.max(counts) ;
            for(int i = min ; i <= max ; ++i) {
                Integer count = minCountHistogram.getOrDefault(i,0) ;
                System.out.printf("%" + String.valueOf(min).length() +"d = %," + (String.valueOf(iterations).length()+2) + "d / %5.1f%%\n", i, count, count * 100.00 / iterations) ;
            }
        }
        {
            Set<Integer> counts = maxCountHistogram.keySet() ;
            int min = Collections.min(counts) ;
            int max = Collections.max(counts) ;
            for(int i = min ; i <= max ; ++i) {
                Integer count = maxCountHistogram.getOrDefault(i,0) ;
                System.out.printf("%" + (String.valueOf(max).length()+1) +"d = %," + (String.valueOf(iterations).length()+2) + "d / %5.1f%%\n", i, count, count * 100.00 / iterations) ;
            }
        }
    }
}
