#!/usr/bin/env bash

usage() {
  printf "Usage: %s [OPTION...]\n" "$0"
  cat >&2 << EOF

Convert ppt files to images and speech

OPTIONs
 -h, --help                       show this help manuel
 -r, --recompile-dependencies     recompile dependencies
  
EOF
  exit 0
}

scriptRepo=$(dirname "$0")
currentDirectry=$PWD
recompileDependencies=false;

OPTS=$(getopt -o hr --long help,recompile-dependencies -n 'parse-options' -- "$@")
if [ $? != 0 ]; then
  echo "Failed parsing options." >&2; exit 1
fi
eval set -- "$OPTS"

while true; do
  case "$1" in
    -h | --help )
      usage; shift ;;
    -r | --recompile-dependencies )
      recompileDependencies=true; shift ;;
    -- )
      shift; break ;;
    * )
      break ;;
  esac
done

if [ "$recompileDependencies" = "true" ]; then
  echo "recomile dependencies..."
  cd $scriptRepo
  mvn clean install > /dev/null
  cd $currentDirectry
fi

echo "remove not ppt files present in the current foolder..."
find . -type f ! -name '*.pptx' -delete

for ppt in *.pptx; do
  echo "extract $ppt notes..."
  java -jar "$scriptRepo/ppt-notes-extractor/dist/standalone.jar" "$ppt"
  echo "convert $ppt to images..."
  java -jar "$scriptRepo/ppt-to-images/dist/standalone.jar" "$ppt"
done
