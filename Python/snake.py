#! /usr/bin/env python3
import pygame as pg
from random import randrange

WINDOW = 800
TILE_SIZE = 40
RANGE = (TILE_SIZE // 2, WINDOW - TILE_SIZE, TILE_SIZE)
get_random_position = lambda: [randrange(*RANGE), randrange(*RANGE)]
snake = pg.rect.Rect([0,0,TILE_SIZE - 2, TILE_SIZE - 2])
snake.center = get_random_position()
snake_dir = (0,0)
length = 1
segments = [snake.copy()]
time, time_step = 0, 110
food = snake.copy()
food.center = get_random_position()
screen = pg.display.set_mode([WINDOW]*2)
clock = pg.time.Clock()
available_dirs = {'UP':1,'DOWN':1,'LEFT':1,'RIGHT':1}

while True:
    for event in pg.event.get():
        if event.type == pg.QUIT:
            exit()
        if event.type == pg.KEYDOWN:
            if (event.key == pg.K_w or event.key == pg.K_UP) and available_dirs['UP']:
                snake_dir = (0, -TILE_SIZE)
                available_dirs = {'UP':1,'DOWN':0,'LEFT':1,'RIGHT':1}
            if (event.key == pg.K_s or event.key == pg.K_DOWN) and available_dirs['DOWN']:
                snake_dir = (0, TILE_SIZE)
                available_dirs = {'UP':0,'DOWN':1,'LEFT':1,'RIGHT':1}
            if (event.key == pg.K_a or event.key == pg.K_LEFT) and available_dirs['LEFT']:
                snake_dir = (-TILE_SIZE, 0)
                available_dirs = {'UP':1,'DOWN':1,'LEFT':1,'RIGHT':0}
            if (event.key == pg.K_d or event.key == pg.K_RIGHT) and available_dirs['RIGHT']:
                snake_dir = (TILE_SIZE, 0)
                available_dirs = {'UP':1,'DOWN':1,'LEFT':0,'RIGHT':1}
    screen.fill('black')
    # check border or selfeating
    self_eating = pg.Rect.collidelist(snake, segments[:-1]) != -1
    if snake.left < 0 or snake.right > WINDOW or snake.top < 0 or snake.bottom > WINDOW or self_eating:
        snake.center, food.center = get_random_position(), get_random_position()
        length, snake_dir = 1, (0,0)
        segments = [snake.copy()]
    # check food
    if snake.center == food.center:
        food.center = get_random_position()
        length += 1
    # draw food
    pg.draw.rect(screen, 'red', food)
    # draw snake
    [pg.draw.rect(screen, 'green', segment) for segment in segments]
    # move snake
    time_now = pg.time.get_ticks()
    if time_now - time > time_step:
        time = time_now
        snake.move_ip(snake_dir)
        segments.append(snake.copy())
        segments = segments[-length:]
    pg.display.flip()
    clock.tick(60)

