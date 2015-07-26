#!/bin/bash

path=$1
currentpath=${PWD}
clear
now=$(date)
echo -e $now: BEGIN git log extraction: $path \\n 

cd $path

git config diff.renameLimit 999999 

#Extract commit information
git log --pretty=format:"%H;%an;%ae;%at;%cn;%ce;%ct;%s"  > commitinfo.log

#Extract and format commit files information
git log --name-status --pretty=format:"commit %H" --find-renames > log.log
awk -f $currentpath/log.awk log.log > commitfileinfo.log

#Remove temp file
rm log.log

git config --unset diff.renameLimit


now=$(date)
echo -e "It was generated log files (commitinfo.log and commitfileinfo.log) in $path folder:  \\n"
echo -e $now: END git log extraction: $path \\n 