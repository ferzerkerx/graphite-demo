# Graphite/Grafana demo

[![Build Status](https://travis-ci.org/ferzerkerx/graphite-demo.svg?branch=master)](https://travis-ci.org/ferzerkerx/graphite-demo)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.ferzerkerx.graphite-demo%3Agraphite-demo)](https://sonarcloud.io/dashboard/index/com.ferzerkerx.graphite-demo%3Agraphite-demo)

SpringBoot application that pushes metric data to graphite for learning purposes

![alt](https://github.com/ferzerkerx/graphite-demo/raw/master/graphite_graphana.png)

## Dependencies
- Graphite
- Grafana
I recommend using this image https://hub.docker.com/r/alexmercer/graphite-grafana/ follow the instructions on the page
I used:

````bash
sudo docker run -p 2003:2003 -p 3000:3000 -p 80:80 -it alexmercer/graphite-grafana
````

## Basic Grafana Setup 
http://www.dburkland.com/how-to-setup-grafana-with-graphite-and-create-basic-dashboards/