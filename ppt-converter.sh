#!/usr/bin/env bash

scriptRepo=$(dirname "$0")
currentDirectry=$PWD

java -jar "$scriptRepo/ppt-notes-extractor/dist/standalone.jar" $1
java -jar "$scriptRepo/ppt-to-images/dist/standalone.jar" $1