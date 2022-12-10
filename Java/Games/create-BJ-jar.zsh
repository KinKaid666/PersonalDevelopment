#!/bin/zsh
# Processing Needs Java < 1.8
javac card/*.java card/deck/*.java card/game/blackjack/*.java card/game/blackjack/Strategies/*.java card/game/blackjack/GeneticAlgorithm/*.java && jar cf BlackjackSimulator.jar card/*.class card/deck/*.class card/game/blackjack/*.class card/game/blackjack/Strategies/*.class card/game/blackjack/GeneticAlgorithm/*.class
