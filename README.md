# SWexplorer

This is a toy project to explore Star Wars starships by film through a D3.js tree layout.

Data is aggregated from http://swapi.co and http://starwars.com

Demo: http://urma.com/sw/

The project constitutes of two parts:

1. A Scala script which generates a data.json from different data sources
2. A HTML/D3.js front-end on top of the generated data.json

## Requirements

* Scala 2.11
* Sbt 0.13+

## How to run
```
$ git clone git@github.com:raoulDoc/SWexplorer.git
$ cd SWexplorer
$ sbt test
$ sbt run
$ python -m SimpleHTTPServer &
$ open http://localhost:8000/starwars_explorer/
```
