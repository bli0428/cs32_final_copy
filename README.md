All 3 ideas approved by Joe, Adam, and Alex

# s18-klee50-rmerzbac-plong1-bli31-ccutting

# README

## Project
Bughouse (https://en.wikipedia.org/wiki/Bughouse_chess) is a multiplayer chess variant.  Usually, it
consists of two teams of two players each, one of each color.  Teammates sit next to each other such
and each play on separate boards against a player on the opposing team.  The games progress
simultaneously, but with a twist.  When players capture pieces, they pass them to their teammate
(who is playing the color of the captured piece), who can spend a move to place the piece somewhere on
the board.  Usually, the games occur simultaneously, but with a move timer or game timer to prevent a
player stalling for their partner to pass them pieces.  Generally, the game can be expanded to be
played across n boards with n players on each team.

While there are a lot of chess AIs out there and it's unlikely we could build a better one within
the scope of this project, we could build an AI that can play bughouse.  Not only is there an additional
element of strategy involved in placing pieces, but it's possible for players on a team to communicate.  So,
if an AI player could do something really nice with a placable knight, it should be able to "ask" for
a knight from its teammate who's passing to it, and that player should place a higher priority on moves
that capture and pass a knight.

This needs a fairly sophisticated UI for constructing teams of human and AI players and running the boards
simultaneously.

For the regular chess part of the AI, we could look into ML libraries, existing chess AIs with good APIs,
or write a simple DFS/Minimax algorithm.  Either way, the bughouse components would be the more
interesting part of the project, since regular chess AIs have been done lots of times.

## Project Slides
https://docs.google.com/presentation/d/1CBy9VAo5s7LM1R_RUezRUQE3lgKl-V4_hFOz-FBYCYQ/edit?usp=sharing

## Project Specifications and Design
https://docs.google.com/document/d/1gDhoG916Igd_hjO9kmuDQJCmVno8Z-vhBxpSAFTRscQ/edit


## MEMBERS
### Karolyn Lee

### Reid Merzbacher

### Charles Cutting
Intro Seq: CS 19

Strengths: I'm sorta ok at backend, am pretty good at testing, and have no life,
and thus a lot of time on my hands to spend on 32.

Weakness: No frontend experience prior to 32, never worked on a project on this scale.

### Peiran Long
Intro Seq: CS 15/16

Strengths: Backend

Weakness: Minimal front end experience

### Brandon Li
Intro Seq: CS 19

Strengths: I have experience with both front end and backend outside of the classroom

Weakness: Gets stuck for very long times on small bugs
