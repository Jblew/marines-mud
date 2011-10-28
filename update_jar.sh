#!/bin/bash

echo "Delete ./MarinesMUD4.jar"
rm MarinesMUD4.jar

echo "Delete files in ./lib"
for file in ./lib/*
do
   rm $file
done

echo "Copy ./dist/MarinesMUD4.jar to ./MarinesMUD4.jar"
cp ./dist/MarinesMUD4.jar ./MarinesMUD4.jar

echo "Copy files from ./dist/lib to ./lib"
for file in ./dist/lib/*
do
   cp $file ./lib/`basename $file`
   svn add ./lib/`basename $file`
done

echo "Done"